package no.kantega.id.finland;

import no.kantega.id.IdFactoryBuilder;
import no.kantega.id.api.IdNumber;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;

public class FinnishIdFactoryTest {

    private Locale finland;

    @Before
    public void setup() {
        finland = new Locale("fi", "FI");
    }

    @Test
    public void parsedIdNumberHas_FIAsCountry() {
        FinnishIdNumber idNumber =  new IdFactoryBuilder().forCountry(finland).parse("123457-123J");

        assertThat(idNumber.getCountry(), Is.is("FI"));
    }

}
