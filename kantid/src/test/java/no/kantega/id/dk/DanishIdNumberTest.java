package no.kantega.id.dk;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Locale;

import static java.time.Month.APRIL;
import static java.util.Locale.FRANCE;
import static java.util.Optional.empty;
import static junit.framework.TestCase.assertFalse;
import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;
import static no.kantega.id.dk.DanishIdNumber.forId;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;


public class DanishIdNumberTest {

    private static final String MALE_ID1 = "0204850011";

    private static final String MALE_ID2 = "0504156907";

    private static final String FEMALE_ID1 = "0204850372";

    @Test
    public void testValidity() {
        assertThat(forId(MALE_ID1).isValid(DanishIdNumber::validate1968), is(true));
        assertThat(forId(MALE_ID2).isValid(DanishIdNumber::validate), is(true));
        assertThat(forId(FEMALE_ID1).isValid(DanishIdNumber::validate1968), is(true));

        assertThat(forId("1234567890").isValid(DanishIdNumber::validate1968), is(false));
        assertThat(forId("020485-0011").isValid(DanishIdNumber::validate1968), is(false));
    }

    @Test
    public void nonExistingDate_IsNotValid() {
        assertFalse(forId("3002801234").isValid(DanishIdNumber::validate));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidLocale_WillThrow_IllegalArgumentException() {
        DanishIdNumber.forId(MALE_ID1, FRANCE);
    }

    @Test
    public void countryDenmark_IsValidLocale() {
        final Locale localeDK = new Locale("da", "DK");
        assertTrue(DanishIdNumber.forId(MALE_ID1, localeDK).supports(localeDK));
    }


    @Test
    public void testGender() {
        assertThat(forId(MALE_ID1).gender(DanishIdNumber::gender).get(), is(MALE));
        assertThat(forId(FEMALE_ID1).gender(DanishIdNumber::gender).get(), is(FEMALE));
        assertThat(forId("1234").gender(DanishIdNumber::gender), is(empty()));
    }

    @Test
    public void testBirthDay() {
        assertThat(forId(MALE_ID1).birthday(DanishIdNumber::birthday).get(), is(LocalDate.of(1985, APRIL, 2)));
        assertThat(forId(MALE_ID2).birthday(DanishIdNumber::birthday).get(), is(LocalDate.of(2015, APRIL, 5)));
        assertThat(forId("1234").birthday(DanishIdNumber::birthday), is(empty()));
    }

}
