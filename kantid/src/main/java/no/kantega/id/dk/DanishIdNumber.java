package no.kantega.id.dk;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.time.LocalDate;
import java.util.Locale;

public class DanishIdNumber extends LocalIdNumber {

    /**
     * Official documentation
     * https://cpr.dk/media/167692/personnummeret%20i%20cpr.pdf
     */
    private static final Locale LOCALE_DENMARK = new Locale("da", "DK");

    public DanishIdNumber(String idToken, Locale locale) {
        super(idToken, locale);
    }

    public static DanishIdNumber forId(String idString) {
        return new DanishIdNumber(idString, LOCALE_DENMARK);
    }

    public static boolean validate1968(final IdNumber number) {
        if (!number.getIdToken().matches("(\\d){10}")) {
            return false;
        }

        int controlSum =
            4 * Character.getNumericValue(number.getIdToken().charAt(0)) +
            3 * Character.getNumericValue(number.getIdToken().charAt(1)) +
            2 * Character.getNumericValue(number.getIdToken().charAt(2)) +
            7 * Character.getNumericValue(number.getIdToken().charAt(3)) +
            6 * Character.getNumericValue(number.getIdToken().charAt(4)) +
            5 * Character.getNumericValue(number.getIdToken().charAt(5)) +
            4 * Character.getNumericValue(number.getIdToken().charAt(6)) +
            3 * Character.getNumericValue(number.getIdToken().charAt(7)) +
            2 * Character.getNumericValue(number.getIdToken().charAt(8)) +
            Character.getNumericValue(number.getIdToken().charAt(9));
        return controlSum % 11 == 0;
    }

    public static Gender gender(final IdNumber number) {
        return (Character.getNumericValue(number.getIdToken().charAt(9)) & 1) == 0 ? Gender.FEMALE : Gender.MALE;
    }

    public static LocalDate birthday(final IdNumber number) {
        if (!validate1968(number)) {
            return null; // should throw exception
        }

        int day=Integer.parseInt(number.getIdToken().substring(0,2));
        int month=Integer.parseInt(number.getIdToken().substring(2,4));
        int shortYear=Integer.parseInt(number.getIdToken().substring(4,6));

        int year = shortYear + findYear(shortYear, Character.getNumericValue(number.getIdToken().charAt(7)));

        return LocalDate.of(year, month, day);
    }

    private static int findYear(int shortYear, int yearCenturyPart) {
        if (yearCenturyPart>=0 && yearCenturyPart <=3) {
            return 1900;
        }
        else if (yearCenturyPart==4||yearCenturyPart==9)   {
            if (shortYear >=0 || shortYear <=36) {
                return 2000;
            }
            else {
                return 1900;
            }
        }
        else {
            if (shortYear>=58 && shortYear<=99) {
                return 1800;
            }
            else {
                return 2000;
            }
        }
    }
}
