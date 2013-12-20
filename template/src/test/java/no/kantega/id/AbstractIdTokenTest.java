package no.kantega.id;

import no.kantega.id.unknown.UnknownIdToken;
import org.junit.Test;

/**
 * @author simom 12/20/13 11:10 AM
 */
public class AbstractIdTokenTest {

    @Test(expected = IllegalArgumentException.class)
    public void idToken_CannotBe_Null() {
        new UnknownIdToken(null);
    }

}
