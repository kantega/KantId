package no.kantega.id.is;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.time.LocalDate;

import static java.util.Locale.FRANCE;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by kristofferskaret on 14.04.14.
 */
public class IcelandishIdNumberTest {

    private static final String VALID_ICE_IDNUMBER = "120174-3389";

    @Test
    public void icelandishIdNumberSupportsLocale() throws Exception {
        assertTrue(IcelandishIdNumber.forId(VALID_ICE_IDNUMBER).supports(IcelandishIdNumber.LOCALE_ICELAND));
    }

    @Test
    public void icelandishIdNumberSupportsValidLocale() throws Exception {
        assertTrue(IcelandishIdNumber.forId(VALID_ICE_IDNUMBER, IcelandishIdNumber.LOCALE_ICELAND).supports(IcelandishIdNumber.LOCALE_ICELAND));
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyValueIsNotSupported() {
        IcelandishIdNumber.forId(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void invalidLocaleIsIllegal() {
        IcelandishIdNumber.forId(VALID_ICE_IDNUMBER, FRANCE);
    }

    @Test
    public void validIdNumberValidates() {
        assertTrue(IcelandishIdNumber.forId(VALID_ICE_IDNUMBER).isValid(IcelandishIdNumber::valid));
    }

    @Test
    public void mustReturnCorrectBirtday() {
        assertThat(IcelandishIdNumber.forId(VALID_ICE_IDNUMBER).birthday(IcelandishIdNumber::birthday).get(), CoreMatchers.is(LocalDate.of(1974, 01, 12)));
    }

    @Test
    public void mustReturnCorrectAge() {

    }


}
    