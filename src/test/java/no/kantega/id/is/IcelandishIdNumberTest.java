package no.kantega.id.is;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by kristofferskaret on 14.04.14.
 */
public class IcelandishIdNumberTest {

    @Test
    public void icelandishIdNumberSupportsLocale() throws Exception {
        assertTrue(IcelandishIdNumber.forId("312321").supports(IcelandishIdNumber.LOCALE_ICELAND));
    }

    @Test
    public void icelandishIdNumberSupportsLocaleGiven() throws Exception {
        assertTrue(IcelandishIdNumber.forId("312321", IcelandishIdNumber.LOCALE_ICELAND).supports(IcelandishIdNumber.LOCALE_ICELAND));
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyValueIsNotSupported() {
        IcelandishIdNumber.forId(null);
    }
}
    