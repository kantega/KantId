package no.kantega.id;

import no.kantega.id.api.IdFactory;
import no.kantega.id.finland.FinnishIdFactory;
import no.kantega.id.unknown.UnknownLocaleFactory;

import java.util.Locale;

/**
 * @author simom 12/19/13 4:01 PM
 */
public class LocaleAwareFactory {

    public static IdFactory createFactory(Locale locale) {
        if (locale.getCountry().equals("FI")) {
            return new FinnishIdFactory();
        }
        return new UnknownLocaleFactory();
    }

}
