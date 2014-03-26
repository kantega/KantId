package no.kantega.id.se;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static java.lang.Character.getNumericValue;
import static java.lang.Integer.parseInt;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoField.*;
import static java.util.Optional.empty;
import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;

/**
 * Representation of a Swedish <i>personnummer</i> which is issued by the Swedish <i>skatteverket</i>. Such a number
 * is represented by ten digits and a hyphen which is inserted after the first six digits that represent a person's
 * birthday. The birthday is represented in the format <i>YYMMDD</i>, where the year is only included by its last two digits.
 * The hyphen is represented by:
 * <ol>
 * <li>A {@code -} sign: For people under the age of 100</li>
 * <li>A {@code ß} sign: For people that have reached the age of 100</li>
 * </ol>
 * In order to avoid a change of the ID number by reaching a certain age, some authorities use 12 digit numbers instead
 * which represent the birthday in the format <i>YYYYMMDD</i> without using a hyphen. Both formats are understood
 * by this implementation.
 * <p>
 * The remaining digits of the <i>personnummer</i> can interpreted differently:
 * <ol>
 * <li>Up to 1990 the seventh and eight digit represented the country of birth of a person or alternatively the
 * country of residence, if a person was born before 1947.</li>
 * <li>The ninth digit represents a person's gender with even numbers being reserved for females and odd numbers for
 * males.</li>
 * <li>After 1967, the tenth digit was used to represent a checksum. Before this year, the Swedish <i>personnummer</i>
 * consisted of only 9 digits. The latter representation is not understood by this implementation.</li>
 * </ol>
 * The rule for computing a check sum to a <i>personnummer</i> is documented on for example
 * <a href="http://en.wikipedia.org/wiki/Personal_identity_number_%28Sweden%29">Wikipedia</a>.
 */
public class SwedishIdNumber extends LocalIdNumber {

    /**
     * A default country token for Sweden.
     */
    public static final String SE_COUNTRY = "SE";

    /**
     * A default Swedish locale.
     */
    public static final Locale SWEDEN = new Locale("se", SE_COUNTRY);

    /**
     * A regular expression for checking if a number is in the format of a Swedish ID number.
     */
    public static final String VALID_FORMAT_SE = "(\\d{6}|\\d{8})(\\+|\\-?)(\\d{4})";

    /**
     * Creates a new Swedish ID with the given locale.
     *
     * @param idToken The ID token to represent.
     * @param locale  The Locale for the current instance of SwedishIdNumber
     */
    public SwedishIdNumber(String idToken, Locale locale) {
        super(idToken, locale);
    }

    /**
     * Creates a Swedish ID with default locale.
     *
     * @param idToken The ID token to represent.
     */
    public SwedishIdNumber(String idToken) {
        super(idToken, SWEDEN);
    }

    @Override
    public boolean supports(Locale locale) {
        return SWEDEN.getCountry().equals(locale.getCountry());
    }

    /**
     * Provide an instance of IdNumber with swedish locale and provides
     * implementation of all the method supported.
     *
     * @param token The idToken
     * @return An instance of SwedishIdNumber with Locale set to "se", "SE"
     */
    public static SwedishIdNumber forId(String token) {
        return new SwedishIdNumber(token);
    }

    /**
     * Provide an instance of IdNumber with swedish locale and provides
     * implementation of all the method supported.
     *
     * @param token  the idToken
     * @param locale The Locale for the current instance of SwedishIdNumber
     * @return An instance of SwedishIdNumber for the provided locale
     */
    public static SwedishIdNumber forId(String token, Locale locale) {
        return new SwedishIdNumber(token, locale);
    }

    /**
     * Standard implementation of validity check for Swedish idNumbers.
     * Implementation is based on Wikipedia specification.
     *
     * @param idNumber The IdNumber to validate
     * @return True when the token respect the specification given by wikipedia
     * for the Swedish National Id number
     */
    public static boolean valid(IdNumber idNumber) {
        String id = idNumber.getIdToken();
        if (id.matches(VALID_FORMAT_SE)) {
            Interpreter interpreter = new Interpreter(id);
            return interpreter.checkControl() && interpreter.validateDate();
        }
        return false;
    }

    /**
     * Checks if the given instance represents a valid Swedish ID number.
     *
     * @return {@code true} if this instance represents a valid Swedish ID number.
     */
    public boolean isValid() {
        return valid(this);
    }

    /**
     * Extracts the gender from the given person number following the
     * specification for the swedish person Number
     *
     * @param idNumber The IdNumber to consider
     * @return The optional gender associated to the given idNumber or empty optional.
     * Optional.empty() in case it is not possible to calculate the Date
     */
    public static Optional<Gender> gender(IdNumber idNumber) {
        try {
            return Optional.of(new Interpreter(idNumber.getIdToken()).calculateGender());
        } catch (NumberFormatException e) {
            return empty();
        }
    }

    /**
     * Determines the gender of the person that holds this ID number, if applicable.
     *
     * @return The optional gender of the person holding this ID number.
     */
    public Optional<Gender> gender() {
        return gender(this);
    }

    /**
     * Calculates the birth date of the given IdNumber
     *
     * @param idNumber The IdNumber to consider
     * @return The birth date associated to the given IdNumber
     */
    public static Optional<LocalDate> birthday(IdNumber idNumber) {
        try {
            return Optional.of(new Interpreter(idNumber.getIdToken()).generateBday());
        } catch (NumberFormatException nfe) {
            return empty();
        }
    }

    /**
     * Determines the birth day of the person associated to this ID number, if applicable
     *
     * @return The optional birth day of the person holding this ID number.
     */
    public Optional<LocalDate> birthday() {
        return birthday(this);
    }

    private static class Interpreter {

        int day, month, year, control, personal;

        int[] digits = new int[9];

        Interpreter(String token) {
            control = parseInt(token.substring(token.length() - 1));
            personal = parseInt(token.substring(token.length() - 4, token.length() - 1));

            switch (token.length()) {
                case 11:
                    boolean olderThan100 = token.charAt(7) == '+';
                    day = parseInt(token.substring(4, 6));
                    month = parseInt(token.substring(2, 4));
                    year = parseInt(token.substring(0, 2));
                    manageYear(olderThan100);
                    fillDigits(token, 0, 1);
                    break;
                case 12:
                    day = parseInt(token.substring(6, 8));
                    month = parseInt(token.substring(4, 6));
                    year = parseInt(token.substring(0, 4));
                    fillDigits(token, 2, 2);
                    break;
                default:
                    day = parseInt(token.substring(4, 6));
                    month = parseInt(token.substring(2, 4));
                    year = parseInt(token.substring(0, 2));
                    manageYear(false);
                    fillDigits(token, 0, 0);
                    break;
            }
            day = manageCoordinationNumber(day);
        }

        LocalDate generateBday() {
            return LocalDate.of(year, month, day);
        }

        Gender calculateGender() {
            return personal % 2 == 0 ? FEMALE : MALE;
        }

        boolean validateDate() {
            try {
                generateBday();
            } catch (DateTimeException dte) {
                return false;
            }
            return true;
        }

        boolean checkControl() {
            int sum = 0;
            for (int i = 0; i < digits.length; i++) {
                int product = digits[i] * (((i + 1) % 2) + 1);
                sum += product % 10 + product / 10;
            }
            return control == (10 - (sum % 10)) % 10;
        }

        private void manageYear(boolean olderThen100) {
            int actulYearModulo100 = now().get(YEAR) % 100;
            if (actulYearModulo100 > year) {
                lastCentury(actulYearModulo100);
            } else if (actulYearModulo100 < year) {
                currentCentury(actulYearModulo100);
            } else if (now().get(MONTH_OF_YEAR) > month) {
                lastCentury(actulYearModulo100);
            } else if (now().get(MONTH_OF_YEAR) < month) {
                currentCentury(actulYearModulo100);
            } else if (now().get(DAY_OF_MONTH) > day) {
                lastCentury(actulYearModulo100);
            } else if (now().get(DAY_OF_MONTH) < day) {
                currentCentury(actulYearModulo100);
            } else {
                lastCentury(actulYearModulo100);
            }

            if (olderThen100) {
                year += 100;
            }
        }

        private void currentCentury(int actulYearModulo100) {
            year += now().get(YEAR) - actulYearModulo100;
        }

        private void lastCentury(int actulYearModulo100) {
            year += now().get(YEAR) - actulYearModulo100 - 100;
        }

        private int manageCoordinationNumber(int dayOfBirth) {
            return dayOfBirth > 31 ? dayOfBirth - 60 : dayOfBirth;
        }

        private void fillDigits(String id, int start1, int start2) {
            for (int i = 0; i < digits.length; i++) {
                digits[i] = getNumericValue(i < 6 ? id.charAt(start1 + i) : id.charAt(start2 + i));
            }
        }
    }
}
