package no.kantega.id.ie;

import org.junit.Test;

import java.util.Locale;

import static java.util.Locale.FRANCE;
import static no.kantega.id.ie.PersonalPublicServiceNumber.forId;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PersonalPublicServiceNumberTest {

    private static final String VALID_PPSN = "1234567TW";

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

    @Test(expected = IllegalArgumentException.class)
    public void invalidLocale_WillThrow_IllegalArgumentException() {
        forId(VALID_PPSN, FRANCE);
    }

}
