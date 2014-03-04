package no.kantega.id.api;

import java.util.Locale;

public class LocalIdNumber extends IdNumber {

    public LocalIdNumber(final String idToken, Locale locale) {
        super(idToken);
        if (!supports(locale)) {
            throw new IllegalArgumentException("Locale " + locale + " not supported");
        }
    }

    public static LocalIdNumber forId(String idNumber, Locale locale) {
        return new LocalIdNumber(idNumber, locale);
    }

    protected boolean supports(Locale locale) {
        return locale != null;
    }
}
