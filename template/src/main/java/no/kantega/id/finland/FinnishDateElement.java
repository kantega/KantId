package no.kantega.id.finland;

import no.kantega.id.api.AbstractDateElement;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author simom 12/20/13 11:36 AM
 */
public class FinnishDateElement extends AbstractDateElement {

    private final static DateTimeFormatter FINNISH_IDNUMBER_DATE_FORMAT = DateTimeFormat.forPattern("ddMMyy");

    protected FinnishDateElement(String value) {
        super(value);
    }

    @Override
    protected String getValidDatePart() {
        return FINNISH_IDNUMBER_DATE_FORMAT.parseDateTime(value).toString();
    }
}
