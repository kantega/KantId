package no.kantega.id.se;

import org.junit.Test;

import static no.kantega.id.se.SwedishIdNumber.forId;
import static org.junit.Assert.assertNotNull;

public class SwedishIdNumberTest {

    @Test
    public void testConstructorValid() {

        assertNotNull("Constructor can never return null", forId("12342144123"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyImput() {

        assertNotNull("Constructor can never return null", forId(null));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorBlankInput() {

        assertNotNull("Constructor can never return null", forId(""));

    }

}
