package no.kantega.id.api;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.time.LocalDate.now;
import static java.time.Period.ZERO;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Base class for implementations of id number. This class acts merely as
 * a common type for id numbers and dispatcher for implementations of
 * different id numbers.
 */
public class IdNumber {

    protected final String idToken;

    public IdNumber(final String idToken) {
        if (isBlank(idToken)) {
            throw new IllegalArgumentException("Id token was empty or null.");
        }
        this.idToken = trim(idToken);
    }

    public static IdNumber forId(String idToken) {
        return new IdNumber(idToken);
    }

    public String getIdToken() {
        return idToken;
    }

    public boolean isValid(final Predicate<IdNumber> validityTest) {
        return validityTest.test(this);
    }

    public Optional<Gender> gender(final Function<IdNumber, Optional<Gender>> genderFunction) {
        return genderFunction.apply(this);
    }

    public Optional<LocalDate> birthday(final Function<IdNumber, Optional<LocalDate>> birthDayFunction) {
        return birthDayFunction.apply(this);
    }

    public Optional<Period> age(final Function<IdNumber, Optional<LocalDate>> birthDayFunction) {
        return birthDayFunction.apply(this)
            .map((birthday) -> birthday.until(now()))
            .map((period) -> period.isNegative() ? ZERO : period);
    }
}
