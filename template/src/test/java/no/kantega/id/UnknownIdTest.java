package no.kantega.id;

import no.kantega.id.unknown.UnknownIdToken;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author simom 12/20/13 11:02 AM
 */
public class UnknownIdTest {

    @Test
    public void unknownIdIs_AlwaysValid() {
        assertTrue(new UnknownIdToken("").isValid());
        assertTrue(new UnknownIdToken("   ").isValid());
        assertTrue(new UnknownIdToken("12345-abcde").isValid());
    }
}
