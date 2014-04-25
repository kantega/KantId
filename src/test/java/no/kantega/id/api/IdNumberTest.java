package no.kantega.id.api;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.function.Function;

import static java.time.LocalDate.now;
import static java.time.Period.ZERO;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;
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
        assertThat(forId("123456-123J").age(id -> of(now().plusDays(2))).get(), is(ZERO));
    }

    @Test
    public void genderFunctionThrowing_ExceptionWillReturn_EmptyOptional() {
        assertThat(forId("123456-124X").gender(IdNumberTest::genderThrowsException), is(empty()));
    }

    @Test
    public void genderFunctionReturning_NullWillReturn_EmptyOptional() {
        assertThat(forId("123456-124X").gender(id -> null), is(empty()));
    }

    @Test
    public void genderFunction_WillReturnGenderOptional() {
        assertThat(forId("123456-124X").gender(id -> of(MALE)).get(), is(MALE));
        assertThat(forId("123456-123Y").gender(id -> of(FEMALE)).get(), is(FEMALE));
    }

    @Test
    public void birthDateFunctionThrowing_ExceptionWillReturn_EmptyOptional() {
        assertThat(forId("123456-124X").birthday(IdNumberTest::birthDateThrowsException), is(empty()));
    }

    @Test
    public void birthDateFunctionReturning_NullWillReturn_EmptyOptional() {
        assertThat(forId("123456-124X").birthday(id -> null), is(empty()));
    }

    @Test
    public void birthDateFunction_WillReturnLocalDateOptional() {
        LocalDate timeNow = now();
        assertThat(forId("123456-124X").birthday(id -> of(timeNow)).get(), is(timeNow));
    }

    @Test
    public void ageIsCalculated_FromBirthday() {
        LocalDate twoYearsAgo = now().minusYears(2);
        LocalDate thirtyFifeYearsOneMonthAgo = now().minusYears(35).minusMonths(1);

        Period age = forId("123456-123K").age(idNumber -> of(twoYearsAgo)).get();
        assertThat(age.getYears(), is(2));
        assertThat(age.getMonths(), is(0));
        assertThat(age.getDays(), is(0));

        age = forId("123456-123K").age(idNumber -> of(thirtyFifeYearsOneMonthAgo)).get();
        assertThat(age.getYears(), is(35));
        assertThat(age.getMonths(), is(1));
    }

    private static Optional<Gender> genderThrowsException(IdNumber idNumber) {
        throw new RuntimeException("");
    }

    private static Optional<LocalDate> birthDateThrowsException(IdNumber idNumber) {
        throw new RuntimeException("");
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
