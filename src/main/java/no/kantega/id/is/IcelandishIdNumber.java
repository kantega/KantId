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
 * Created by kristofferskaret on 14.04.14.
 * <p>
 * Description of Icelandish ID-number:
 * <p>
 * http://en.wikipedia.org/wiki/Kennitala
 * http://en.wikipedia.org/wiki/National_identification_number
 */
public class IcelandishIdNumber extends LocalIdNumber {

    public static final Locale LOCALE_ICELAND = new Locale("is", "IS");

    protected IcelandishIdNumber(String idToken, Locale locale) {
        super(idToken, locale);
    }

    public static IcelandishIdNumber forId(String idToken) {
        return new IcelandishIdNumber(idToken, LOCALE_ICELAND);
    }

    public static IcelandishIdNumber forId(String idToken, Locale locale) {
        return new IcelandishIdNumber(idToken, locale);
    }

    @Override
    protected boolean supports(Locale locale) {
        return LOCALE_ICELAND.getCountry().equals(locale.getCountry());
    }

    /**
     * The idNumber is valid if the following conditions is true:
     * - it matches the correct pattern
     * - it contains a valid birthday
     * - it contaions a valid check digit
     */
    public static boolean valid(IdNumber idNumber) {
        return patternMatches(idNumber) &&
                birthday(idNumber).isPresent() &&
                checkDigitIsValid(idNumber);
    }

    /**
     * check if the IdNumber matches the correct pattern (like 20174-3389 or 1201743389)
     */
    private static boolean patternMatches(IdNumber idNumber) {
        return idNumber.getIdToken().matches("\\d{6}-?\\d{4}");
    }

    /**
     * Formula:
     * V = 11 - ((3a1 + 2a2 + 7a3 + 6a4 + 5a5 + 4a6 + 3a7 + 2a8) mod 11)
     */
    private static boolean checkDigitIsValid(IdNumber idNumber) {
        int v = 11 - (Math.floorMod(
                3 * digit(idNumber, 1) +
                        2 * digit(idNumber, 2) +
                        7 * digit(idNumber, 3) +
                        6 * digit(idNumber, 4) +
                        5 * digit(idNumber, 5) +
                        4 * digit(idNumber, 6) +
                        3 * digit(idNumber, 7) +
                        2 * digit(idNumber, 8), 11
        ));

        return digit(idNumber, 9) == v;
    }

    /**
     * @return the digit with the index given
     */
    private static int digit(IdNumber idNumber, int index) {
        return Character.getNumericValue(idNumber.getIdToken().replace("-", "").charAt(index));
    }

    /**
     * @return the birthday, or none if no valid birtday can be retrieved
     */
    public static Optional<LocalDate> birthday(final IdNumber idNumber) {

        String token = idNumber.getIdToken();

        int day = parseInt(token.substring(0, 2));
        int month = parseInt(token.substring(2, 4));
        int shortYear = parseInt(token.substring(4, 6));

        int centuryDigit = Integer.valueOf((token.substring(token.length() - 1)));
        int year = calculateYear(shortYear, centuryDigit);

        try {
            return Optional.of(LocalDate.of(year, month, day));
        } catch (DateTimeException e) {
            return empty();
        }
    }

    /**
     * @return the year based on shortYear (two digits) and centuryDigit
     */
    private static int calculateYear(int shortYear, int centuryDigit) {

        int century;
        if (centuryDigit == 0) {
            century = 2000;
        } else {
            century = 1000 + 100 * centuryDigit;
        }
        return century + shortYear;
    }

}
