package no.kantega.id.fin;

import org.junit.Test;

import java.util.Locale;

import static java.util.Locale.FRANCE;
import static no.kantega.id.api.Gender.MALE;
import static no.kantega.id.fin.FinnishIdNumber.forId;
import static no.kantega.id.fin.FinnishIdNumber.genderInFinland;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FinnishIdNumberTest {

    private static final String VALID_ID = "150674-153J";

    @Test(expected = IllegalArgumentException.class)
    public void idTokenIs_NotNull() {
        assertFalse(forId(null).isValid(FinnishIdNumber::valid));
    }

    @Test
    public void countryFinland_IsValidLocale() {
        final Locale fi_FI = new Locale("fi", "FI");
        final Locale se_FI = new Locale("se", "FI");

        assertThat(forId(VALID_ID, fi_FI).supports(fi_FI), is(true));
        assertThat(forId(VALID_ID, se_FI).supports(se_FI), is(true));
        assertThat(forId(VALID_ID, fi_FI).supports(null), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidLocale_WillThrow_IllegalArgumentException() {
        forId(VALID_ID, FRANCE);
    }

    @Test
    public void testValidity_WithStaticMethod() {
        assertTrue(forId(VALID_ID).isValid(FinnishIdNumber::valid));
    }

    @Test
    public void testGender_WithStaticMethod() {
        assertThat(forId(VALID_ID).gender(FinnishIdNumber::gender), is(MALE));
    }

    @Test
    public void testGender_WithWrappedMethod_ForBetterDSL() {
        assertThat(forId(VALID_ID).gender(genderInFinland()), is(MALE));
    }

}
