package no.kantega.id.api;

/**
 * @author simom 12/20/13 11:27 AM
 */
public abstract class AbstractIdElement {

    protected final String value;

    protected AbstractIdElement(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
