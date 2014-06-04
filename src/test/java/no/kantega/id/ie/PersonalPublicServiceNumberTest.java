package no.kantega.id.ie;

import org.junit.Test;

import java.util.Locale;

import static java.util.Locale.FRANCE;
import static no.kantega.id.ie.PersonalPublicServiceNumber.forId;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PersonalPublicServiceNumberTest {

    private static final String VALID_PPSN = "1234567TW";
    private static final String INVALID_PPSN = "7654321TW";

    @Test(expected = IllegalArgumentException.class)
    public void idTokenIs_NotNull() {
        PersonalPublicServiceNumber.forId(null);
    }

    @Test
    public void countryIreland_IsValidLocale() {
        final Locale ga_IE = new Locale("ga", "IE");

        assertThat(forId(VALID_PPSN, ga_IE).supports(ga_IE), is(true));
        assertThat(forId(VALID_PPSN, ga_IE).supports(null), is(false));
    }

    @Test
    public void testValidCase() throws Exception {
        PersonalPublicServiceNumber validId = PersonalPublicServiceNumber.forId(VALID_PPSN);
        assertTrue("ID should be valid", validId.isValid());
    }

    @Test
    public void testInvalidCase() throws Exception {
        PersonalPublicServiceNumber validId = PersonalPublicServiceNumber.forId(INVALID_PPSN);
        assertFalse("ID should NOT be valid", validId.isValid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidLocale_WillThrow_IllegalArgumentException() {
        forId(VALID_PPSN, FRANCE);
    }

}
