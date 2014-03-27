package no.kantega.id.dk;

import org.junit.Test;

import java.util.Locale;

import static java.util.Locale.FRANCE;
import static junit.framework.TestCase.assertFalse;
import static no.kantega.id.dk.DanishIdNumber.forId;
import static org.junit.Assert.assertTrue;


public class DanishIdNumberGeneralTest {

    private static final String VALID_ID = "0204850011";

    @Test
    public void nonExistingDate_IsNotValid() {
        assertFalse(forId("3002801234").isValid(DanishIdNumber::valid));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidLocale_WillThrow_IllegalArgumentException() {
        DanishIdNumber.forId(VALID_ID, FRANCE);
    }

    @Test
    public void countryDenmark_IsValidLocale() {
        final Locale localeDK = new Locale("da", "DK");
        assertTrue(DanishIdNumber.forId(VALID_ID, localeDK).supports(localeDK));
    }




}
