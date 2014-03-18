package no.kantega.id.se;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Locale;

import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;
import static no.kantega.id.se.SwedishIdNumber.SE_COUNTRY;
import static no.kantega.id.se.SwedishIdNumber.SWEDEN;
import static no.kantega.id.se.SwedishIdNumber.forId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SwedishIdNumberTest {

    public static final String VALID_SWEDISH_ID = "900304-4428";

    public static final String EMPTY = "";

    public static final String SWEDISH_MAN = "081231+6214";

    private static final String SWEDISH_WOMAN = "7812310006";

    public static final LocalDate BIRTHDAY_DATE = LocalDate.of(1908, 12, 31);

    public static final int AGE = 105;

    @Test
    public void testConstructorValid() {
        assertNotNull("Constructor can never return null", forId(VALID_SWEDISH_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullImput() {
        assertNotNull("Constructor can never return null", forId(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyInput() {
        assertNotNull("Constructor can never return null", forId(EMPTY));
    }

    @Test
    public void testConstructorSwedishLocale() {
        assertNotNull("Constructor can never return null", forId(VALID_SWEDISH_ID, SWEDEN));
    }

    @Test
    public void mastAllowSwedishWithOtherLanguage() {
        assertNotNull("Constructor can never return null", forId(VALID_SWEDISH_ID, new Locale("fi", SE_COUNTRY)));
    }

    @Test
    public void canExctractGenderForMen() {
        assertEquals("Given IdNumber identifies a men", MALE, forId(SWEDISH_MAN).gender(SwedishIdNumber::gender).get());
    }

    @Test
    public void canExctractGenderForWomen() {
        assertEquals("Given IdNumber identifies a women", FEMALE,
            forId(SWEDISH_WOMAN).gender(SwedishIdNumber::gender).get());
    }

    @Test
    public void mustBeAbleToExtractTheRighDate() {
        assertEquals("Wrong date was calculated", BIRTHDAY_DATE,
            forId(SWEDISH_MAN).birthday(SwedishIdNumber::birthday).get());
    }

    @Test
    public void mustReturnTheRightAge() {
        assertEquals("Wrong age", AGE, forId(SWEDISH_MAN).age(SwedishIdNumber::birthday).get().getYears());
    }

    @Test
    public void testValid() {
        assertTrue(forId("720620-1381").isValid(SwedishIdNumber::valid));
        assertTrue("050605+0830", forId("050605+0830").isValid(SwedishIdNumber::valid));
        assertTrue("200102203460", forId("200102203460").isValid(SwedishIdNumber::valid));
        assertTrue("090610-0540", forId("090610-0540").isValid(SwedishIdNumber::valid));
    }
}
