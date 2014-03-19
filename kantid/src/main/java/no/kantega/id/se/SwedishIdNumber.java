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
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;
import static java.util.Optional.empty;
import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;

/**
 * <pre>
 * Provides utility methods for the swedish Id number.
 * If follows the specification given by <a href="http://sv.wikipedia.org/wiki/Personnummer_i_Sverige">Wikipedia</a>
 *
 * It supports id numbers provided in the following formats:
 * 1) yyMMdd-pppc
 * 2) yyMMddpppc
 * 3) yyyyMMddpppc
 *
 * where ppp is for the gender/location part and c is the control number.
 *
 * Support is also provided for the "samordningsnummer" in all 3 variations.
 * </pre>
 */
public class SwedishIdNumber extends LocalIdNumber {

    public static final String SE_COUNTRY = "SE";

    public static final Locale SWEDEN = new Locale("se", SE_COUNTRY);

    public static final String VALID_FORMT_SE = "(\\d{6}|\\d{8})(\\+|\\-?)(\\d{4})";

    public SwedishIdNumber(String idToken, Locale locale) {
        super(idToken, locale);
    }

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
     * @param token the idToken
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
        if (id.matches(VALID_FORMT_SE)) {
            Interpreter interpreter = new Interpreter(id);
            return interpreter.checkControl() && interpreter.validateDate();
        }
        return false;
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
     * Calculates the birthday date of the given IdNumber
     *
     * @param idNumber The IdNumber to consider
     * @return The birthday date associated to the given IdNumber
     */
    public static Optional<LocalDate> birthday(IdNumber idNumber) {
        try {
            return Optional.of(new Interpreter(idNumber.getIdToken()).generateBday());
        } catch (NumberFormatException nfe) {
            return empty();
        }
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
