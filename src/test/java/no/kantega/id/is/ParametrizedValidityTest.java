package no.kantega.id.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static java.util.stream.Collectors.toList;
import static no.kantega.id.fin.FinnishIdNumber.forId;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParametrizedValidityTest {

    @Parameter
    public String ssnInput;

    @Parameters
    public static Collection<Object[]> data() throws URISyntaxException, IOException {
        Path filePath = Paths.get(ClassLoader.class.getResource("/is/valid_is_numbers.txt").toURI());
        return Files.lines(filePath).map(ssn -> new Object[]{ssn}).collect(toList());
    }

    @Test
    public void testValidity() {
        assertTrue("Expected " + ssnInput + " to be valid ssn.", forId(ssnInput).isValid(IcelandishIdNumber::valid));
    }
}
