package no.kantega.id.fin;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static java.time.Month.FEBRUARY;
import static java.util.Locale.FRANCE;
import static java.util.Optional.empty;
import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;
import static no.kantega.id.fin.FinnishIdNumber.forId;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class FinnishIdNumberTest {

    private static final String VALID_FEMALE_ID = "270274-885N";

    private static final String VALID_MALE_ID = "010290+3581";

    @Test(expected = IllegalArgumentException.class)
    public void idTokenIs_NotNull() {
        assertFalse(FinnishIdNumber.forId(null).isValid(FinnishIdNumber::valid));
    }

    @Test
    public void countryFinland_IsValidLocale() {
        final Locale fi_FI = new Locale("fi", "FI");
        final Locale se_FI = new Locale("se", "FI");

        assertThat(forId(VALID_FEMALE_ID, fi_FI).supports(fi_FI), is(true));
        assertThat(forId(VALID_FEMALE_ID, se_FI).supports(se_FI), is(true));
        assertThat(forId(VALID_FEMALE_ID, fi_FI).supports(null), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidLocale_WillThrow_IllegalArgumentException() {
        forId(VALID_FEMALE_ID, FRANCE);
    }

    @Test
    public void wronglyFormattedIdNumber_HasEmptyBirthday() {
        assertThat(forId("X70274-885N").birthday(FinnishIdNumber::birthday), is(empty()));
    }

    @Test
    public void nonExistingDate_HasEmptyBirthday() {
        assertThat(forId("300274-885N").birthday(FinnishIdNumber::birthday), is(empty()));
    }

    @Test
    public void genderIsTaken_FromGenderBit() {
        assertThat(forId(VALID_FEMALE_ID).gender(FinnishIdNumber::gender), is(FEMALE));
        assertThat(forId(VALID_MALE_ID).gender(FinnishIdNumber::gender), is(MALE));
    }

    @Test
    public void birthDayIs_TakenFromSixFirstChars() {
        Optional<LocalDate> birthday = forId(VALID_FEMALE_ID).birthday(FinnishIdNumber::birthday);
        assertThat(birthday.get().getDayOfMonth(), is(27));
        assertThat(birthday.get().getMonth(), is(FEBRUARY));
        assertThat(birthday.get().getYear(), is(1974));
    }

    @Test
    public void centuryIs_TakenFromSeparator() {
        assertThat(forId("270274-885N").birthday(FinnishIdNumber::birthday).get().getYear(), is(1974));
        assertThat(forId("010290+3581").birthday(FinnishIdNumber::birthday).get().getYear(), is(1890));
        assertThat(forId("231211A5182").birthday(FinnishIdNumber::birthday).get().getYear(), is(2011));
    }

}
