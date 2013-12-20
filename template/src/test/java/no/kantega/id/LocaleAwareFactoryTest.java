package no.kantega.id;

import no.kantega.id.finland.FinnishIdFactory;
import no.kantega.id.unknown.UnknownLocaleFactory;
import org.junit.Test;

import java.util.Locale;

import static java.util.Locale.CANADA;
import static no.kantega.id.LocaleAwareFactory.createFactory;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author simom 12/20/13 10:55 AM
 */
public class LocaleAwareFactoryTest {

    private Locale finland = new Locale("fi", "FI");

    @Test
    public void finnishFactory_IsUsed_WhenLocaleCountry_IsFI() {
        assertThat(createFactory(finland), is(instanceOf(FinnishIdFactory.class)));
    }

    @Test
    public void unknownFactory_IsUsed_WhenLocale_NotFound() {
        assertThat(createFactory(CANADA), is(instanceOf(UnknownLocaleFactory.class)));
    }
}
