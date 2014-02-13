package no.kantega.id;

import no.kantega.id.api.IdNumber;
import no.kantega.id.api.factory.IdFactory;
import no.kantega.id.finland.FinnishIdFactory;

import java.util.Locale;

public class IdFactoryBuilder {

    public IdFactory<? extends IdNumber> forCountry(Locale locale) {
        if (locale.getCountry().equals("FI")) {
            return new FinnishIdFactory();
        }
        return null;
    }


}
