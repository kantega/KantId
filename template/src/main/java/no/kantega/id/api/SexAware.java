package no.kantega.id.api;

/**
 * @author simom 12/19/13 3:52 PM
 */
public interface SexAware extends IdNumber {

    boolean isMan();

    boolean isWoman();
}
