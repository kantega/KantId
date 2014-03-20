package no.kantega.id.no;

import no.kantega.id.api.Gender;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

@RunWith(Parameterized.class)
public class Male2000NorwegianIdNumberTest extends AbstractNorwegianIdNumberTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws Exception {
        return read("/no/male2000.txt");
    }

    public Male2000NorwegianIdNumberTest(String id) {
        super(id);
    }

    @Override
    protected Gender getExpectedGender() {
        return Gender.MALE;
    }

    @Override
    protected int getExpectedMinimumYear() {
        return 2000;
    }

    @Override
    protected int getExpectedMaximumYear() {
        return Integer.MAX_VALUE;
    }
}
