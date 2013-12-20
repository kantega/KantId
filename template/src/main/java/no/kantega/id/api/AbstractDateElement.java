package no.kantega.id.api;

/**
 * @author simom 12/20/13 11:29 AM
 */
public abstract class AbstractDateElement extends AbstractIdElement {

    protected AbstractDateElement(String value) {
        super(value);
    }

    protected abstract String getValidDatePart();
}
