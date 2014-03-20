package no.kantega.id.no;

import no.kantega.id.api.Gender;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

@RunWith(Parameterized.class)
public class Male1900NorwegianIdNumberTest extends AbstractNorwegianIdNumberTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws Exception {
        return read("/no/male1900.txt");
    }

    public Male1900NorwegianIdNumberTest(String id) {
        super(id);
    }

    @Override
    protected Gender getExpectedGender() {
        return Gender.MALE;
    }

    @Override
    protected int getExpectedMinimumYear() {
        return 1900;
    }

    @Override
    protected int getExpectedMaximumYear() {
        return 2000;
    }
}
