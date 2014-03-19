package no.kantega.id.api;

import java.util.Locale;

/**
 * Base class for id numbers supporting locale.
 */
public class LocalIdNumber extends IdNumber {

    public LocalIdNumber(final String idToken, Locale locale) {
        super(idToken);
        if (!supports(locale)) {
            throw new IllegalArgumentException("Locale " + locale + " not supported");
        }
    }

    /**
     * Provides an instance of {@link LocalIdNumber} with given {@link Locale}
     *
     * @param idToken of id number.
     * @param locale for the current instance.
     * @return An instance of {@link LocalIdNumber} with given locale.
     */
    public static LocalIdNumber forId(String idToken, Locale locale) {
        return new LocalIdNumber(idToken, locale);
    }

    /**
     * Defines support method for given locale. Users of this class
     * are supposed to override this method.
     *
     * @param locale given locale.
     * @return true if locale is supported, false otherwise.
     */
    protected boolean supports(Locale locale) {
        return locale != null;
    }
}
