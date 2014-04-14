package no.kantega.id.is;

import no.kantega.id.api.IdNumber;
import no.kantega.id.dk.DanishIdNumber;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertTrue;

/**
 * Created by kristofferskaret on 14.04.14.
 */
public class IcelandishIdNumberTest {

    @Test
    public void icelandishIdNumberSupportsLocale() throws Exception {
        assertTrue(IcelandishIdNumber.forId("312321").supports(IcelandishIdNumber.LOCALE_ICELAND));
    }
}
