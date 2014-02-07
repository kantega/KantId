package no.kantega.id.finland;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FinnishIdFactoryTest {

    private Locale finland;

    @Before
    public void setup() {
        finland = new Locale("fi", "FI");
    }

    @Test
    public void parsedIdNumberHas_FIAsCountry() {
        FinnishIdNumber idNumber =  new FinnishIdFactory().parse("123457-123J");
        assertThat(idNumber.getCountry(), is("FI"));
    }

}
