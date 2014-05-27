package no.kantega.id.ie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static no.kantega.id.ie.PersonalPublicServiceNumber.forId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test examples of invalid <emp>Personal Public Service Numbers</emp>.
 */
@RunWith(Parameterized.class)
public class PersonalPublicServiceNumberInvalidityTest {

    @Parameterized.Parameter
    public String inputNumber;

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws URISyntaxException, IOException {
        Path filePath = Paths.get(ClassLoader.class.getResource("/ie/invalid_numbers.txt").toURI());
        return Files.lines(filePath).map(ssn -> new Object[]{ssn}).collect(toList());
    }

    @Test
    public void testInvalidity() {

        String message = format("Expected %s to be not valid ppsn.", inputNumber);
        PersonalPublicServiceNumber ppsn = forId(inputNumber);

        assertFalse(message, ppsn.isValid(PersonalPublicServiceNumber::valid));
    }

}
