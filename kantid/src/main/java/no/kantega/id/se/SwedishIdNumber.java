package no.kantega.id.se;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;
import static java.util.Optional.empty;
import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;
import static no.kantega.id.api.Gender.UNKNOWN;

public class SwedishIdNumber extends LocalIdNumber {

    public static final String SE_COUNTRY = "SE";

    public static final Locale SWEDEN = new Locale("se", SE_COUNTRY);

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
        boolean matches = id.matches("(\\d{6}||\\d{8})(\\+\\-?)(\\d{4})");
        return matches && new Interpreter(id).checkControl();
    }


    /**
     * Extracts the gender from the given person number follwing the
     * specification for the swedish person Number
     *
     * @param idNumber The IdNumber to consider
     * @return The gender associated to the given idNumber
     */
    public static Optional<Gender> gender(IdNumber idNumber) {
        try {
            return Optional.of(new Interpreter(idNumber.getIdToken()).calculateGender());
        } catch (NumberFormatException e) {
            return Optional.of(UNKNOWN);
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
                    boolean olderThen100 = token.charAt(7) == '+';
                    day = parseInt(token.substring(4, 6));
                    month = parseInt(token.substring(2, 4));
                    year = parseInt(token.substring(0, 2));
                    manageYear(olderThen100);
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
            day = manageSamordningsNumber(day);
        }

        LocalDate generateBday() {
            return LocalDate.of(year, month, day);
        }

        Gender calculateGender() {
            return personal % 2 == 0 ? FEMALE : MALE;
        }

        boolean checkControl() {
            int sum = 0;
            for (int i = 0; i < digits.length; i++) {
                sum += digits[i] * (((i + 1) % 2) + 1);
            }
            return control == sum % 10;
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

        private int manageSamordningsNumber(int dayOfBirth) {
            return dayOfBirth > 31 ? dayOfBirth - 60 : dayOfBirth;
        }

        private void fillDigits(String id, int start1, int start2) {
            for (int i = 0; i < digits.length; i++) {
                digits[i] = i < 6 ? id.charAt(start1 + i) : id.charAt(start2 + i);
            }
        }
    }
}
