package no.kantega.id.api;

/**
 * @author simom 12/19/13 4:03 PM
 */
public interface IdFactory <T extends AbstractIdElement> {

    AbstractIdToken generate(T... elements);
}
