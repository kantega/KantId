package no.kantega.id.api;

import org.junit.Test;

import java.util.Optional;
import java.util.function.Function;

import static java.time.LocalDate.now;
import static java.time.Period.ZERO;
import static no.kantega.id.api.IdNumber.forId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

public class IdNumberTest {

    @Test
    public void idToken_IsNotNull() {
        assertExceptionWithInvalidArgument(IdNumber::forId, null);
        assertExceptionWithInvalidArgument(IdNumber::new, null);
    }

    @Test
    public void idToken_IsNotBlank() {
        assertExceptionWithInvalidArgument(IdNumber::forId, "");
        assertExceptionWithInvalidArgument(IdNumber::new, "");
        assertExceptionWithInvalidArgument(IdNumber::forId, " ");
        assertExceptionWithInvalidArgument(IdNumber::new, " ");
    }

    @Test
    public void idToken_IsTrimmedFrom_BothEnds() {
        assertThat(new IdNumber(" 123  ").getIdToken(), is("123"));
    }

    @Test
    public void negativeAge_WillReturn_ZeroAge() {
        IdNumber idNumber = forId("123456-123J");
        assertThat(idNumber.age((b) -> Optional.of(now().plusDays(2))).get(), is(ZERO));
    }

    private void assertExceptionWithInvalidArgument(Function<String, IdNumber> function, String argument) {
        try {
            function.apply(argument);
            fail();
        } catch (IllegalArgumentException e ) {
            assertThat(e.getMessage(), is("Id token was empty or null."));
        }
    }
}
