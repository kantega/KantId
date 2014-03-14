package no.kantega.id.se;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Locale;

import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;
import static no.kantega.id.se.SwedishIdNumber.SWEDEN;
import static no.kantega.id.se.SwedishIdNumber.forId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SwedishIdNumberTest {

    public static final String VALID_SWEDISH_ID = "12342144123";

    public static final String EMPTY = "";

    public static final String SWEDISH_MAN = "toDoManSwed";

    private static final String SWEDISH_WOMAN = "toDoWomenSwed";

    public static final LocalDate BIRTHDAY_DATE = LocalDate.of(1, 1, 1980);

    public static final int AGE = 34;

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
        assertNotNull("Constructor can never return null", forId(VALID_SWEDISH_ID, new Locale("ss", "SE")));
    }

    @Test
    public void canExctractGenderForMen() {
        assertEquals("Given IdNumber identifies a men", MALE, forId(SWEDISH_MAN).gender(SwedishIdNumber::gender));
    }

    @Test
    public void canExctractGenderForWomen() {
        assertEquals("Given IdNumber identifies a women", FEMALE, forId(SWEDISH_WOMAN).gender(SwedishIdNumber::gender));
    }

    @Test
    public void mustBeAbleToExtractTheRighDate() {
        assertEquals("Wrong date was calculated", BIRTHDAY_DATE, forId(SWEDISH_MAN).birthday(SwedishIdNumber::birthdate));
    }

    @Test
    public void mustReturnTheRightAge() {
        assertEquals("Wrong age", AGE, forId(SWEDISH_MAN).age(SwedishIdNumber::age));
    }
}
