package no.kantega.id.dk;

import no.kantega.id.api.Gender;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static no.kantega.id.dk.DanishIdNumber.forId;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public abstract class AbstractDanishIdNumberTest {

    private String ssnInput;

    public AbstractDanishIdNumberTest(String ssnInput) {
        this.ssnInput = ssnInput;
    }

    public static Collection<Object[]> dataFromPath(String path) throws URISyntaxException, IOException {
        Path filePath = Paths.get(ClassLoader.class.getResource(path).toURI());
        return Files.lines(filePath).map(ssn -> new Object[]{ssn}).collect(toList());
    }

    @Test
    public void testValidity() {
        DanishIdNumber idNumber = forId(ssnInput);
        assertTrue(idNumber.isValid(DanishIdNumber::valid));
        assertEquals(shouldValidateModulus11(), idNumber.isValid(DanishIdNumber::validateModulus11));

    }

    @Test
    public void testGender() {
        assertTrue(forId(ssnInput).gender(DanishIdNumber::gender).get() == getTestGender());
    }

    @Test
    public void testBirthday() {
        int year = getTestCentury() + parseInt(ssnInput.substring(4, 6));
        int month = parseInt(ssnInput.substring(2, 4));
        int day = parseInt(ssnInput.substring(0, 2));
        LocalDate inputBirthday = LocalDate.of(year, month, day);
        Optional calculatedDate = forId(ssnInput).birthday(DanishIdNumber::birthday);

        assertTrue(calculatedDate.isPresent());
        assertThat(calculatedDate.get(), is(inputBirthday));
    }

    protected abstract Gender getTestGender();
    protected abstract int getTestCentury();
    protected abstract boolean shouldValidateModulus11();

}
