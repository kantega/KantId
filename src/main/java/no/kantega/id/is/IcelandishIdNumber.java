package no.kantega.id.is;

import no.kantega.id.api.LocalIdNumber;

import java.util.Locale;

/**
 * Created by kristofferskaret on 14.04.14.
 */
public class IcelandishIdNumber extends LocalIdNumber {

    public static final Locale LOCALE_ICELAND = new Locale("is", "IS");

    protected IcelandishIdNumber(String idToken) {
        super(idToken, LOCALE_ICELAND);
    }

    /**
     * Provide an instance of IdNumber with given locale (country Denmark is only supported)
     * and implementation for methods of {@link no.kantega.id.api.IdNumber}.
     *
     * @param idToken of idNumber.
     * @param locale for idNumber, must be supported.
     * @return instance of DanishIdNumber.
     * @throws IllegalArgumentException if locale is not supported. This class supports
     * only locales with country set as Denmark.
     */
    public static IcelandishIdNumber forId(String idToken) {
        return new IcelandishIdNumber(idToken);
    }

    @Override
    protected boolean supports(Locale locale) {
        return LOCALE_ICELAND.getCountry().equals(locale.getCountry());
    }
}
