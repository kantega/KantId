package no.kantega.id.finland;

import no.kantega.id.api.AbstractIdToken;
import no.kantega.id.api.IdFactory;

/**
 * @author simom 12/19/13 4:07 PM
 */
public class FinnishIdFactory implements IdFactory<FinnishDateElement> {


    /**
     * See rules for generating
     * <a href="http://en.wikipedia.org/wiki/National_identification_number#Finland">Finnish Id Number</a>
     * @return valid id token in Finland.
     */
    @Override
    public AbstractIdToken generate(FinnishDateElement... elements) {
        return new FinnishIdToken(elements[0].getValidDatePart() + "ABC");
    }
}
