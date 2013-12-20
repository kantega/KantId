package no.kantega.id.unknown;

import no.kantega.id.api.AbstractIdToken;

/**
 * @author simom 12/19/13 4:11 PM
 */
public class UnknownIdToken extends AbstractIdToken {

    public UnknownIdToken(String idToken) {
        super(idToken);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
