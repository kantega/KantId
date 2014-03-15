package no.kantega.id.dk;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.time.LocalDate;
import java.util.Locale;

public class DanishIdNumber extends LocalIdNumber {

    private static final Locale LOCALE_DENMARK = new Locale("da", "DK");

    public DanishIdNumber(String idToken, Locale locale) {
        super(idToken, locale);
    }

    public static DanishIdNumber forId(String idString) {
        return new DanishIdNumber(idString, LOCALE_DENMARK);
    }

    public boolean isValid() {
        return super.isValid(DanishIdNumber::valid);
    }

    public Gender gender() {
        return super.gender(DanishIdNumber::determineGender);
    }

    public LocalDate birthday() {
        return null;
    }

    public static boolean valid(final IdNumber number) {
        return false;
    }

    public static Gender determineGender(final IdNumber number) {
        return (Character.getNumericValue(number.getIdToken().charAt(9)) & 1) == 0 ? Gender.FEMALE : Gender.MALE;
    }

}
