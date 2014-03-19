package no.kantega.id.se;

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
import static no.kantega.id.se.SwedishIdNumber.forId;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class ParametrizedSeInvalidityTest {


    @Parameterized.Parameter
    public String invalidSsn;

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws URISyntaxException, IOException {
        Path filePath = Paths.get(ClassLoader.class.getResource("/se/invalid_numbers.txt").toURI());
        return Files.lines(filePath).map(ssn -> new Object[]{ssn}).collect(toList());
    }

    @Test
    public void testInvalid() {
        assertFalse("The ssn " + invalidSsn + " is an invalid Swedish personal number",
            forId(invalidSsn).isValid(SwedishIdNumber::valid));
    }

}
