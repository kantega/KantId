package no.kantega.id.is;

import no.kantega.id.api.LocalIdNumber;

import java.util.Locale;

/**
 * Created by kristofferskaret on 14.04.14.
 */
public class IcelandishIdNumber extends LocalIdNumber {

    public static final Locale LOCALE_ICELAND = new Locale("is", "IS");

    protected IcelandishIdNumber(String idToken, Locale locale) {
        super(idToken, locale);
    }

    public static IcelandishIdNumber forId(String idToken) {
        return new IcelandishIdNumber(idToken, LOCALE_ICELAND);
    }

    public static IcelandishIdNumber forId(String idToken, Locale locale) {
        return new IcelandishIdNumber(idToken, locale);
    }

    @Override
    protected boolean supports(Locale locale) {
        return LOCALE_ICELAND.getCountry().equals(locale.getCountry());
    }
}
