package no.kantega.id.dk;

import no.kantega.id.api.Gender;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;


@RunWith(Parameterized.class)
public class Female2000DanishIdNumberTest extends AbstractDanishIdNumberTest {


    public Female2000DanishIdNumberTest(String ssnInput) {
        super(ssnInput);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws URISyntaxException, IOException {
        return dataFromPath("/dk/female2000.txt");
    }

    @Override
    protected Gender getTestGender() {
        return Gender.FEMALE;
    }

    @Override
    protected int getTestCentury() {
        return 2000;
    }

    @Override
    protected boolean shouldValidateModulus11() {
        return false;
    }}
