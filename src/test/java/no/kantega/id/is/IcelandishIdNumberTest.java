package no.kantega.id.is;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.time.LocalDate;

import static java.util.Locale.FRANCE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by kristofferskaret on 14.04.14.
 */
public class IcelandishIdNumberTest {

    private static final String VALID_ICE_IDNUMBER = "120174-3389";
    private static final String INVALID_BIRTHDAY = "156774-3389";
    private static final String INVALID_CHECK_DIGIT = "120174-3379";
    public static final String WRONG_NUMBER_OF_DIGITS = "120174-338";
    public static final String NO_HYPHEN = "1201743389";

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
    public void invalidBirthdayInvalidates() {
        assertFalse(IcelandishIdNumber.forId(INVALID_BIRTHDAY).isValid(IcelandishIdNumber::valid));
    }

    @Test
    public void invalidCheckDigitInvalidates() {
        assertFalse(IcelandishIdNumber.forId(INVALID_CHECK_DIGIT).isValid(IcelandishIdNumber::valid));
    }

    @Test
    public void wrongNumberOfDigitsInvalidates() {
        assertFalse(IcelandishIdNumber.forId(WRONG_NUMBER_OF_DIGITS).isValid(IcelandishIdNumber::valid));
    }

    @Test
    public void hyphenIsOptional() {
        assertTrue(IcelandishIdNumber.forId(NO_HYPHEN).isValid(IcelandishIdNumber::valid));
    }


    @Test
    public void mustReturnCorrectBirtday() {
        assertThat(IcelandishIdNumber.forId(VALID_ICE_IDNUMBER).birthday(IcelandishIdNumber::birthday).get(), CoreMatchers.is(LocalDate.of(1974, 01, 12)));
    }

}
    