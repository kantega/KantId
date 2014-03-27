package no.kantega.id.dk;

import no.kantega.id.api.Gender;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;


@RunWith(Parameterized.class)
public class Male1800Modulus11DanishIdNumberTest extends AbstractDanishIdNumberTest {


    public Male1800Modulus11DanishIdNumberTest(String ssnInput) {
        super(ssnInput);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws URISyntaxException, IOException {
        return dataFromPath("/dk/male1800modulus11.txt");
    }

    @Override
    protected Gender getTestGender() {
        return Gender.MALE;
    }

    @Override
    protected int getTestCentury() {
        return 1800;
    }

    @Override
    protected boolean shouldValidateModulus11() {
        return true;
    }
}
