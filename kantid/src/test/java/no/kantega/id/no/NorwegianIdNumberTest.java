package no.kantega.id.no;

import no.kantega.id.api.Gender;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static no.kantega.id.no.NorwegianIdNumber.forId;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NorwegianIdNumberTest {

    private static final String MALE_1800 = "13107362317";
    private static final String MALE_1900 = "13055226746";
    private static final String MALE_2000 = "20090587995";
    private static final String FEMALE_1800 = "04077362296";
    private static final String FEMALE_1900 = "24046541886";
    private static final String FEMALE_2000 = "11091370656";

    @Test
    public void testValidity() {
        assertThat(forId(MALE_1800).isValid(NorwegianIdNumber::valid), is(true));
        assertThat(forId(FEMALE_1800).isValid(NorwegianIdNumber::valid), is(true));
        assertThat(forId(MALE_1900).isValid(NorwegianIdNumber::valid), is(true));
        assertThat(forId(FEMALE_1900).isValid(NorwegianIdNumber::valid), is(true));
        assertThat(forId(MALE_2000).isValid(NorwegianIdNumber::valid), is(true));
        assertThat(forId(FEMALE_2000).isValid(NorwegianIdNumber::valid), is(true));
    }

    @Test
    public void testBirthday() {
        assertThat(forId(MALE_1800).birthday(NorwegianIdNumber::birthday), is(LocalDate.of(1873, Month.OCTOBER, 13)));
        assertThat(forId(FEMALE_1800).birthday(NorwegianIdNumber::birthday), is(LocalDate.of(1873, Month.JULY, 4)));
        assertThat(forId(MALE_1900).birthday(NorwegianIdNumber::birthday), is(LocalDate.of(1952, Month.MAY, 13)));
        assertThat(forId(FEMALE_1900).birthday(NorwegianIdNumber::birthday), is(LocalDate.of(1965, Month.APRIL, 24)));
        assertThat(forId(MALE_2000).birthday(NorwegianIdNumber::birthday), is(LocalDate.of(2005, Month.SEPTEMBER, 20)));
        assertThat(forId(FEMALE_2000).birthday(NorwegianIdNumber::birthday), is(LocalDate.of(2013, Month.SEPTEMBER, 11)));
    }

    @Test
    public void testGender() {
        assertThat(forId(MALE_1800).gender(NorwegianIdNumber::gender), is(Gender.MALE));
        assertThat(forId(MALE_1900).gender(NorwegianIdNumber::gender), is(Gender.MALE));
        assertThat(forId(MALE_2000).gender(NorwegianIdNumber::gender), is(Gender.MALE));
        assertThat(forId(FEMALE_1800).gender(NorwegianIdNumber::gender), is(Gender.FEMALE));
        assertThat(forId(FEMALE_1900).gender(NorwegianIdNumber::gender), is(Gender.FEMALE));
        assertThat(forId(FEMALE_2000).gender(NorwegianIdNumber::gender), is(Gender.FEMALE));
    }

    @Test
    public void testType() {
        assertThat(NorwegianIdNumber.type(forId(MALE_1800)), is(NorwegianIdNumber.Type.FNUMBER));
        assertThat(NorwegianIdNumber.type(forId(MALE_1900)), is(NorwegianIdNumber.Type.FNUMBER));
        assertThat(NorwegianIdNumber.type(forId(MALE_2000)), is(NorwegianIdNumber.Type.FNUMBER));
        assertThat(NorwegianIdNumber.type(forId(FEMALE_1800)), is(NorwegianIdNumber.Type.FNUMBER));
        assertThat(NorwegianIdNumber.type(forId(FEMALE_1900)), is(NorwegianIdNumber.Type.FNUMBER));
        assertThat(NorwegianIdNumber.type(forId(FEMALE_2000)), is(NorwegianIdNumber.Type.FNUMBER));
    }
}
