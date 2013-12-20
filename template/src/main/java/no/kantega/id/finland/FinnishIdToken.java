package no.kantega.id.finland;

import no.kantega.id.api.AbstractIdToken;

/**
 * @author simom 12/19/13 4:11 PM
 */
public class FinnishIdToken extends AbstractIdToken {

    public FinnishIdToken(String idToken) {
        super(idToken);
    }

    @Override
    public boolean isValid() {
        return idToken.equals("150674");
    }
}
