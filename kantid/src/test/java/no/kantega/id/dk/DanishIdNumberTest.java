package no.kantega.id.dk;

import no.kantega.id.api.Gender;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static no.kantega.id.dk.DanishIdNumber.forId;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class DanishIdNumberTest {

    private static final String MALE_ID1 = "0204850011";
    private static final String MALE_ID2 = "0504156907";
    private static final String FEMALE_ID1 = "0204850372";
    @Test
    public void testValidity() {
        assertThat(forId(MALE_ID1).isValid(),is(true));
        assertThat(forId(FEMALE_ID1).isValid(),is(true));
        assertThat(forId("1234567890").isValid(),is(false));
        assertThat(forId("020485-0011").isValid(),is(false));
    }

    @Test
    public void testGender() {
        assertThat(forId(MALE_ID1).gender(), is(Gender.MALE));
        assertThat(forId(FEMALE_ID1).gender(), is(Gender.FEMALE));
    }

    @Test
    public void testBirthDate() {
        assertThat(forId(MALE_ID1).birthday(), is(LocalDate.of(1985, Month.APRIL, 2)));
        assertThat(forId(MALE_ID2).birthday(), is(LocalDate.of(2015, Month.APRIL, 5)));
    }

}
