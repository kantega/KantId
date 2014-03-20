package no.kantega.id.no;

import org.junit.Test;

import static no.kantega.id.no.NorwegianIdNumber.forId;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SpecialNumberTest {

    @Test
    public void testInvalidNumber() throws Exception {
        assertThat(forId("00000000000").isValid(), is(false));
    }

    @Test
    public void testDNumber() throws Exception {
        NorwegianIdNumber dNumber = forId("49068244349");
        assertThat(dNumber.isValid(), is(true));
        assertThat(dNumber.type().get(), is(NorwegianIdNumber.Type.DNUMBER));
        assertEqualInformation(dNumber, forId("09068244355"));
    }

    @Test
    public void testHNumber() throws Exception {
        NorwegianIdNumber hNumber = forId("11515505468");
        assertThat(hNumber.isValid(), is(true));
        assertThat(hNumber.type().get(), is(NorwegianIdNumber.Type.HNUMBER));
        assertEqualInformation(hNumber, forId("11115505485"));
    }

    private static void assertEqualInformation(NorwegianIdNumber original, NorwegianIdNumber other) {
        assertThat(original.birthday(), is(other.birthday()));
        assertThat(original.gender(), is(other.gender()));
    }

    @Test
    public void testFHNumber() throws Exception {
        NorwegianIdNumber hNumber = forId("84075010582");
        assertThat(hNumber.isValid(), is(true));
        assertThat(hNumber.type().get(), is(NorwegianIdNumber.Type.FHNUMBER));
        assertThat(hNumber.gender().isPresent(), is(false));
        assertThat(hNumber.birthday().isPresent(), is(false));
    }
}
