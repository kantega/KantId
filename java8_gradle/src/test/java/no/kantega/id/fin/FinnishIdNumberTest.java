package no.kantega.id.fin;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static no.kantega.id.api.Gender.MALE;
import static no.kantega.id.fin.FinnishIdNumber.forId;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FinnishIdNumberTest {

    private FinnishIdNumber finnishId;

    @Before
    public void setUp() throws Exception {
        finnishId = forId("150674-153J");
    }

    @Test(expected = NullPointerException.class)
    public void idTokenIs_NotNull() {
        assertFalse(forId(null).isValid(FinnishIdNumber::valid));
    }

    @Test
    public void testValidity_WithInstanceMethod() {
        assertTrue(finnishId.isValid(finnishId::validity));
    }

    @Test
    public void testValidity_WithStaticMethod() {
        assertTrue(forId("150674-153J").isValid(FinnishIdNumber::valid));
    }


    @Test
    public void testGender_WithInstancMethod() {
        assertThat(finnishId.gender(finnishId::genderOf), is(MALE));
    }

    @Test
    public void testGender_WithStaticMethod() {
        assertThat(forId("150674-153J").gender(FinnishIdNumber::gender), is(MALE));
    }

    @Test
    public void testGender_WithWrappedMethod_ForBetterDSL() {
        assertThat(forId("150674-153J").gender(inFinland()), is(MALE));
    }


    private static Function<IdNumber, Gender> inFinland() {
        return FinnishIdNumber::gender;
    }
}
