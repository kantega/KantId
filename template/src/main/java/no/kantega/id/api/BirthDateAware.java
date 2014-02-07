package no.kantega.id.api;

import org.joda.time.LocalDate;
import org.joda.time.Period;

/**
 * @author simom 12/19/13 3:52 PM
 */
public interface BirthDateAware extends IdNumber {

    LocalDate getBirthDate();

    String readableFullAge();

    String readbleFormattedAge(String format);

    Period age();
}
