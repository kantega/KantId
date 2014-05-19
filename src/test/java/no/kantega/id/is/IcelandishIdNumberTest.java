package no.kantega.id.is;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.time.LocalDate;

import static java.util.Locale.FRANCE;
import static no.kantega.id.is.IcelandishIdNumber.LOCALE_ICELAND;
import static no.kantega.id.is.IcelandishIdNumber.forId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class IcelandishIdNumberTest {

    private static final String VALID_ICE_IDNUMBER = "120174-3399";

    private static final String INVALID_BIRTHDAY = "156774-3399";

    private static final String INVALID_CHECK_DIGIT = "120174-3379";

    public static final String WRONG_NUMBER_OF_DIGITS = "120174-338";

    public static final String NO_HYPHEN = "1201743399";

    @Test
    public void icelandishIdNumberSupportsLocale() throws Exception {
        assertTrue(forId(VALID_ICE_IDNUMBER).supports(LOCALE_ICELAND));
    }

    @Test
    public void icelandishIdNumberSupportsValidLocale() throws Exception {
        assertTrue(forId(VALID_ICE_IDNUMBER, LOCALE_ICELAND).supports(LOCALE_ICELAND));
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyValueIsNotSupported() {
        forId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidLocaleIsIllegal() {
        forId(VALID_ICE_IDNUMBER, FRANCE);
    }

    @Test
    public void validIdNumberValidates() {
        assertTrue(forId(VALID_ICE_IDNUMBER).isValid(IcelandishIdNumber::valid));
    }

    @Test
    public void invalidBirthdayInvalidates() {
        assertFalse(forId(INVALID_BIRTHDAY).isValid(IcelandishIdNumber::valid));
    }

    @Test
    public void invalidCheckDigitInvalidates() {
        assertFalse(forId(INVALID_CHECK_DIGIT).isValid(IcelandishIdNumber::valid));
    }

    @Test
    public void wrongNumberOfDigitsInvalidates() {
        assertFalse(forId(WRONG_NUMBER_OF_DIGITS).isValid(IcelandishIdNumber::valid));
    }

    @Test
    public void hyphenIsOptional() {
        assertTrue(forId(NO_HYPHEN).isValid(IcelandishIdNumber::valid));
    }

    @Test
    public void mustReturnCorrectBirtday() {
        assertThat(forId(VALID_ICE_IDNUMBER).birthday(IcelandishIdNumber::birthday).get(),
            CoreMatchers.is(LocalDate.of(1974, 01, 12)));
    }

}
    