package no.kantega.id.api.factory;

/**
 * @author simom 12/19/13 4:03 PM
 */
public interface IdFactory <T> {

    T parse(String idNumber);

    boolean compare(String idNumber);

}
