package no.kantega.id.fin;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.lang.IllegalArgumentException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;
import static java.util.Optional.empty;
import static java.util.regex.Pattern.compile;
import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;

/**
 * Representation of ID number in Finland.
 * Valid idNumber in Finland is expected to follow format: ddMMyy[-|+|A]DDDX
 * where 6 first characters represent birthday, followed by +, -, or A
 * representing century (1800, 1900 or 2000), followed by 3-digit running number
 * and control character.

 */
public class FinnishIdNumber extends LocalIdNumber {

    private static final int GENDER_BIT = 7;

    private static final String FINLAND = "FI";

    private static final Locale LOCALE_FI = new Locale("fi", FINLAND);

    private static final char[] CONTROL_CHARS = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'H', 'J', 'K', 'L',
        'M', 'N', 'P', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Z'
    };

    private static final Pattern IDNUMBER_PATTERN =
        compile("([0-3][0-9])([0-1][0-9])([0-9]{2})(\\-|[A]|\\+)([0-9]{3})([0-9]|[A-Z])");

    private static final int DAY = 1;

    private static final int MONTH = 2;

    private static final int YEAR = 3;

    private static final int SEPARATOR = 4;

    private static final int CONTROL_NUMBER = 5;

    private static final int CONTROL_CHAR = 6;

    private static final int DIVIDER = 31;

    public FinnishIdNumber(final String idToken) {
        super(idToken, LOCALE_FI);
    }

    public FinnishIdNumber(final String idToken, Locale locale) {
        super(idToken, locale);
    }

    /**
     * Provides an instance of IdNumber with Finnish locale (country Finland)
     * and implementation for methods of {@link IdNumber}.
     *
     * @param idToken of idNumber.
     * @return instance of FinnishIdNumber with locale set to "fi_FI".
     */
    public static FinnishIdNumber forId(String idToken) {
        return new FinnishIdNumber(idToken);
    }


    /**
     * Provide an instance of IdNumber with given locale (only Finland is supported)
     * and implementation for methods of {@link IdNumber}.
     *
     * @param idToken of idNumber.
     * @param locale for idNumber, must be supported.
     * @return instance of FinnishIdNumber.
     * @throws IllegalArgumentException if locale is not supported. This class supports
     * only locales with country set as Finland.
     */
    public static FinnishIdNumber forId(String idToken, Locale locale) {
        return new FinnishIdNumber(idToken, locale);
    }

    /**
     * Extracts the optional gender from the given IdNumber following the
     * specification for Finnish national identity number.
     * Note that this method doesn't check the validity of the idNumber, it merely
     * extracts gender part of idNumber and interprets it.
     *
     * @param idNumber where gender is taken from.
     * @return Optional gender (male or female), or empty in case of non-digit gender bit.
     */
    public static Optional<Gender> gender(IdNumber idNumber) {
        char genderBit = idNumber.getIdToken().charAt(GENDER_BIT);
        if (isDigit(genderBit)) {
            return genderBit % 2 == 0 ? Optional.of(FEMALE) : Optional.of(MALE);
        }
        return empty();
    }

    /**
     * Standard implementation of validity check for Finnish idNumbers.
     * Valid idNumber is expected to follow format: ddMMyy[-|+|A]DDDX
     * where 6 first characters represent birthday, followed by +, -, or A
     * representing century (1800, 1900 or 2000), followed by 3-digit running number
     * and control character.
     *
     * @param idNumber The idNumber to validate.
     * @return true when valid id number, based on a given specification for Finland.
     */
    public static boolean valid(final IdNumber idNumber) {
        FinnishIdNumber finnishIdNumber = forId(idNumber.getIdToken());
        Matcher format = IDNUMBER_PATTERN.matcher(finnishIdNumber.getIdToken());
        return format.matches()
               && finnishIdNumber.birthdayFor(format).isPresent()
               && finnishIdNumber.hasValidControl(format);
    }

    /**
     * Calculates optional birthday for the given idNumber. Calculation is based on the 7
     * first characters of idNumber from where the 6 first are birth date in format (ddMMyy), and the 7th
     * character represents century.
     *
     * @param idNumber where birthday is calculated from.
     * @return optional birth date, or empty in case of invalid format.
     */
    public static Optional<LocalDate> birthday(final IdNumber idNumber) {
        final Matcher format = IDNUMBER_PATTERN.matcher(idNumber.getIdToken());
        if (format.matches()) {
            return forId(idNumber.getIdToken()).birthdayFor(format);
        }
        return empty();

    }

    private boolean hasValidControl(Matcher idFormat) {
        int controlNumber = parseInt(idFormat.group(DAY) + idFormat.group(MONTH) +
                                     idFormat.group(YEAR) + idFormat.group(CONTROL_NUMBER));
        return idFormat.group(CONTROL_CHAR).charAt(0) == CONTROL_CHARS[controlNumber % DIVIDER];
    }

    private Optional<LocalDate> birthdayFor(Matcher format) {
        try {
            return Optional.of(
                LocalDate.of(centuryFrom(format.group(SEPARATOR).charAt(0)) + parseInt(format.group(YEAR)),
                    parseInt(format.group(MONTH)),
                    parseInt(format.group(DAY))));
        } catch (DateTimeException e) {
            return Optional.empty();
        }
    }

    private int centuryFrom(char separator) {
        switch (separator) {
            case '+':
                return 1800;
            case '-':
                return 1900;
        }
        return 2000;
    }

    @Override
    protected boolean supports(Locale locale) {
        return locale != null && FINLAND.equals(locale.getCountry());
    }

}
