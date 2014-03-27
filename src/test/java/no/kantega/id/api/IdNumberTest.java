package no.kantega.id.api;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.Function;

import static java.time.LocalDate.now;
import static java.time.Period.ZERO;
import static java.util.Optional.of;
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
        assertThat(idNumber.age((id) -> of(now().plusDays(2))).get(), is(ZERO));
    }

    @Test
    public void ageIsCalculated_FromBirthday() {
        LocalDate twoYearsAgo = now().minusYears(2);
        LocalDate thirtyFifeYearsOneMonthSixDaysAgo = now().minusYears(35).minusMonths(1).minusDays(6);

        Period age = forId("123456-123K").age((idNumber) -> of(twoYearsAgo)).get();
        assertThat(age.getYears(), is(2));
        assertThat(age.getMonths(), is(0));
        assertThat(age.getDays(), is(0));

        age = forId("123456-123K").age((idNumber) -> of(thirtyFifeYearsOneMonthSixDaysAgo)).get();
        assertThat(age.getYears(), is(35));
        assertThat(age.getMonths(), is(1));
        assertThat(age.getDays(), is(6));

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
