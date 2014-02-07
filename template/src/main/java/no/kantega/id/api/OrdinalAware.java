package no.kantega.id.api;

/**
 * @author simom 12/19/13 3:52 PM
 */
public interface OrdinalAware extends IdNumber {

    boolean isBefore(OrdinalAware idNumber);

    boolean isAfter(OrdinalAware idNumber);
}
