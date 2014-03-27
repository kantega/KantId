package no.kantega.id.no;

import no.kantega.id.api.Gender;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public abstract class AbstractNorwegianIdNumberTest {

    private static final Locale NORWAY = new Locale("no", "NO"), SWEDEN = new Locale("se", "SE");

    protected static Collection<Object[]> read(String document) throws Exception {
        return Files.readAllLines(Paths.get(Male2000NorwegianIdNumberTest.class.getResource(document).toURI()))
                .stream().map((String s) -> new Object[]{s}).collect(Collectors.toList());
    }

    private final NorwegianIdNumber norwegianIdNumber;

    public AbstractNorwegianIdNumberTest(String id) {
        this.norwegianIdNumber = new NorwegianIdNumber(id);
    }

    @Test
    public void testGender() throws Exception {
        assertThat(norwegianIdNumber.gender().get(), is(getExpectedGender()));
    }

    protected abstract Gender getExpectedGender();

    @Test
    public void testBirthday() throws Exception {
        LocalDate birthday = norwegianIdNumber.birthday().get();
        assertThat(birthday.getDayOfMonth(), is(Integer.parseInt(norwegianIdNumber.getIdToken().substring(0, 2))));
        assertThat(birthday.getMonthValue(), is(Integer.parseInt(norwegianIdNumber.getIdToken().substring(2, 4))));
        assertThat(birthday.getYear() % 100, is(Integer.parseInt(norwegianIdNumber.getIdToken().substring(4, 6))));
        assertThat(birthday.getYear() >= getExpectedMinimumYear(), is(true));
        assertThat(birthday.getYear() < getExpectedMaximumYear(), is(true));
    }

    protected abstract int getExpectedMinimumYear();

    protected abstract int getExpectedMaximumYear();

    @Test
    public void testValidity() throws Exception {
        assertThat(norwegianIdNumber.isValid(), is(true));
    }

    @Test
    public void testType() throws Exception {
        assertThat(norwegianIdNumber.type().get(), is(NorwegianIdNumber.Type.FNUMBER));
    }

    @Test
    public void testSupport() throws Exception {
        assertThat(norwegianIdNumber.supports(NORWAY), is(true));
        assertThat(norwegianIdNumber.supports(SWEDEN), is(false));
    }
}
