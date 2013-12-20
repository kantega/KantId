package no.kantega.id.api;

/**
 * @author simom 12/19/13 3:53 PM
 */
public abstract class AbstractIdToken implements Identified {

    protected final String idToken;

    protected AbstractIdToken(String idToken) {
        if (idToken == null) {
            throw new IllegalArgumentException("IdToken cannot be null");
        }
        this.idToken = idToken;
    }
}
