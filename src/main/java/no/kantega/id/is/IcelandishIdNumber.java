package no.kantega.id.is;

import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static java.util.Optional.empty;

/**
 * Representation of an Icelandish ID-number (Kennitala/Kennitölur).
 * <p>
 * Kennitölur are composed of ten digits. For a personal kennitala, the first six of these are the individual's
 * date of birth in the format DDMMYY.[1] The seventh and eighth digits are randomly chosen when the kennitala
 * is allocated, the ninth is a check digit, and the tenth indicates the century of the individual's birth: '9' for
 * 1900–1999, '0' for 2000 and beyond. Kennitölur are often written with a hyphen following the first six digits, e.g.
 * 120174-3399.
 * <p>
 * The check digit equations is V = 11 - ((3a1 + 2a2 + 7a3 + 6a4 + 5a5 + 4a6 + 3a7 + 2a8) mod 11)
 * <p>
 * More information:
 * <p>
 * http://www.skra.is/thjodskra/um-thjodskra-/um-kennitolur/
 * http://bjss.bifrost.is/index.php/bjss/article/view/63/65
 * http://en.wikipedia.org/wiki/Kennitala
 * http://en.wikipedia.org/wiki/National_identification_number
 */
public class IcelandishIdNumber extends LocalIdNumber {

    public static final Locale LOCALE_ICELAND = new Locale("is", "IS");

    private static final int DIVIDER = 11;

    protected IcelandishIdNumber(final String idToken, final Locale locale) {
        super(idToken, locale);
    }

    public static IcelandishIdNumber forId(final String idToken) {
        return new IcelandishIdNumber(idToken, LOCALE_ICELAND);
    }

    public static IcelandishIdNumber forId(final String idToken, final Locale locale) {
        return new IcelandishIdNumber(idToken, locale);
    }

    @Override
    protected boolean supports(final Locale locale) {
        return LOCALE_ICELAND.getCountry().equals(locale.getCountry());
    }

    /**
     * The idNumber is valid if the following conditions is true:
     * <ul><li>it matches the correct pattern</li>
     * <li>it contains a valid birthday</li>
     * <li>it contaions a valid check digit</li>
     * </ul>
     *
     * @param idNumber ID number to validate
     * @return true if ID number is valid, else false.

     */
    public static boolean valid(final IdNumber idNumber) {
        return patternMatches(idNumber) &&
               birthday(idNumber).isPresent() &&
               checkDigitIsValid(idNumber);
    }

    /**
     * check if the IdNumber matches the correct pattern (like 20174-3389 or 1201743389)
     */
    private static boolean patternMatches(final IdNumber idNumber) {
        return idNumber.getIdToken().matches("\\d{6}-?\\d{4}");
    }

    /**
     * Formula:
     * V = 11 - ((3a1 + 2a2 + 7a3 + 6a4 + 5a5 + 4a6 + 3a7 + 2a8) mod 11)
     */
    private static boolean checkDigitIsValid(final IdNumber idNumber) {
        final int[] digit = digits(idNumber);
        final int v = DIVIDER - (3 * digit[0] +
                      2 * digit[1] +
                      7 * digit[2] +
                      6 * digit[3] +
                      5 * digit[4] +
                      4 * digit[5] +
                      3 * digit[6] +
                      2 * digit[7]) % DIVIDER;
        return digit[8] == v;
    }

    /**
     * @return IdNumber as array of digits
     */
    private static int[] digits(final IdNumber idNumber) {
        return idNumber.getIdToken().chars() // stream of chars
            .filter(c -> c != '-') // filter out hyphen
            .map(Character::getNumericValue) // convert to int
            .toArray();
    }

    /**
     * @return the birthday, or none if no valid birtday can be retrieved
     * @param idNumber ID number to validate
     */
    public static Optional<LocalDate> birthday(final IdNumber idNumber) {

        final String token = idNumber.getIdToken();

        final int day = parseInt(token.substring(0, 2));
        final int month = parseInt(token.substring(2, 4));
        final int shortYear = parseInt(token.substring(4, 6));

        final int centuryDigit = Integer.valueOf((token.substring(token.length() - 1)));
        final int year = calculateYear(shortYear, centuryDigit);

        try {
            return Optional.of(LocalDate.of(year, month, day));
        } catch (final DateTimeException e) {
            return empty();
        }
    }

    /**
     * @return the year based on shortYear (two digits) and centuryDigit
     */
    private static int calculateYear(final int shortYear, final int centuryDigit) {

        final int century;
        if (centuryDigit == 0) {
            century = 2000;
        } else {
            century = 1000 + 100 * centuryDigit;
        }
        return century + shortYear;
    }

}
