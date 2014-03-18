package no.kantega.id.dk;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.time.LocalDate;
import java.util.Locale;

import static java.lang.Character.getNumericValue;
import static java.lang.Integer.parseInt;
import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;

public class DanishIdNumber extends LocalIdNumber {

    /**
     * Official documentation
     * https://cpr.dk/media/167692/personnummeret%20i%20cpr.pdf
     */
    private static final Locale LOCALE_DENMARK = new Locale("da", "DK");

    private static final int GENDER_BIT = 9;

    public DanishIdNumber(String idToken, Locale locale) {
        super(idToken, locale);
    }

    public static DanishIdNumber forId(String idString) {
        return new DanishIdNumber(idString, LOCALE_DENMARK);
    }

    public static boolean validate(final IdNumber number) {
        return number.getIdToken().matches("(\\d){10}");
    }

    public static boolean validate1968(final IdNumber number) {
        if (!validate(number)) {
            return false;
        }

        int controlSum =
            4 * getNumericValue(number.getIdToken().charAt(0)) +
            3 * getNumericValue(number.getIdToken().charAt(1)) +
            2 * getNumericValue(number.getIdToken().charAt(2)) +
            7 * getNumericValue(number.getIdToken().charAt(3)) +
            6 * getNumericValue(number.getIdToken().charAt(4)) +
            5 * getNumericValue(number.getIdToken().charAt(5)) +
            4 * getNumericValue(number.getIdToken().charAt(6)) +
            3 * getNumericValue(number.getIdToken().charAt(7)) +
            2 * getNumericValue(number.getIdToken().charAt(8)) +
            getNumericValue(number.getIdToken().charAt(9));
        return controlSum % 11 == 0;
    }

    public static Gender gender(final IdNumber number) {
        if (!validate(number)) {
            return Gender.UNKNOWN;
        }
        return (getNumericValue(number.getIdToken().charAt(GENDER_BIT)) & 1) == 0 ? FEMALE : MALE;
    }

    public static LocalDate birthday(final IdNumber number) {
        if (!validate(number)) {
            return null;
        }

        int day= parseInt(number.getIdToken().substring(0, 2));
        int month= parseInt(number.getIdToken().substring(2, 4));
        int shortYear= parseInt(number.getIdToken().substring(4, 6));

        int year = calculateYear(shortYear, getNumericValue(number.getIdToken().charAt(7)));

        return LocalDate.of(year, month, day);
    }

    private static int calculateYear(int shortYear, int yearCenturyPart) {
        int century;
        if (yearCenturyPart >= 0 && yearCenturyPart <= 3) {
            century = 1900;
        } else if (yearCenturyPart == 4 || yearCenturyPart == 9) {
            if (shortYear >= 0 || shortYear <= 36) {
                century = 2000;
            } else {
                century = 1900;
            }
        } else {
            if (shortYear >= 58 && shortYear <= 99) {
                century = 1800;
            } else {
                century = 2000;
            }
        }
        return century + shortYear;
    }
}
