package no.kantega.id;

import no.kantega.id.finland.FinnishIdFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

public class IdFactoryBuilderTest {

    private IdFactoryBuilder factoryBuilder;

    private Locale findland;

    @Before
    public void setup() {
        factoryBuilder = new IdFactoryBuilder();
        findland = new Locale("fi", "FI");
    }

    @Test
    public void builderAccordingCountry_IsReturned() {
        assertThat(factoryBuilder.forCountry(findland), instanceOf(FinnishIdFactory.class));
    }

}
