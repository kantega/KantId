package no.kantega.id.api;

import java.util.Locale;

/**
 * Base class for ID numbers supporting locale.
 */
public abstract class LocalIdNumber extends IdNumber {

    protected LocalIdNumber(final String idToken, Locale locale) {
        super(idToken);
        if (!supports(locale)) {
            throw new IllegalArgumentException("Locale " + locale + " not supported");
        }
    }

    /**
     * Check that a given locale is supported.
     *
     * Implementers of this class should check whether their implementation
     * supports the given locale.
     *
     * @param locale given locale.
     * @return true if locale is supported, false otherwise.
     */
    protected abstract boolean supports(Locale locale);
}
