package no.kantega.id.unknown;

import no.kantega.id.api.AbstractIdToken;
import no.kantega.id.api.IdFactory;

/**
 * @author simom 12/19/13 4:11 PM
 */
public class UnknownLocaleFactory implements IdFactory<NoOpIdElement> {

    @Override
    public AbstractIdToken generate(NoOpIdElement... elements) {
        return new UnknownIdToken(elements[0].getValue());
    }
}
