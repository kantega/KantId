package no.kantega.id.finland;

import no.kantega.id.api.factory.IdFactory;

/**
 * @author simom 12/19/13 4:07 PM
 */
public class FinnishIdFactory implements IdFactory<FinnishIdNumber> {


    /**
     * See rules for generating
     * <a href="http://en.wikipedia.org/wiki/National_identification_number#Finland">Finnish Id Number</a>
     * @return valid id token in Finland.
     */
    @Override
    public FinnishIdNumber parse(String idNumber) {
        return new FinnishIdNumber(idNumber);
    }
}
