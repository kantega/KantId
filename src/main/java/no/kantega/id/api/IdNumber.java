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
 * Base class for implementations of an ID number. This class is not capable of understanding the meaning of any
 * ID number but serves as a dispatcher for implementations of a country's ID number.
 */
public class IdNumber {

    /**
     * The ID number token for this instance.
     */
    protected final String idToken;

    /**
     * Creates an immutable ID number.
     *
     * @param idToken The ID token to represent.
     */
    public IdNumber(final String idToken) {
        if (isBlank(idToken)) {
            throw new IllegalArgumentException("Id token was empty or null.");
        }
        this.idToken = trim(idToken);
    }

    /**
     * Creates a new ID number for a given token.
     *
     * @param idToken The token to represent.
     * @return The created ID number.
     */
    public static IdNumber forId(String idToken) {
        return new IdNumber(idToken);
    }

    /**
     * Returns this ID number's token.
     *
     * @return The token represented by this ID number.
     */
    public String getIdToken() {
        return idToken;
    }

    /**
     * Checks if this ID number is valid for a given validity assertion.
     *
     * @param validityTest An implementation of an ID number's validity assertion.
     * @return {@code true} if this ID number is valid for the given assertion logic.
     */
    public boolean isValid(final Predicate<IdNumber> validityTest) {
        return validityTest.test(this);
    }

    /**
     * Retrieves the gender of the person holding this ID number as it is defined for a given
     * gender assertion.
     *
     * @param genderFunction An implementation of an ID number's gender assertion.
     * @return The gender represented by this ID number as valid for the given assertion logic.
     */
    public Optional<Gender> gender(final Function<IdNumber, Optional<Gender>> genderFunction) {
        return genderFunction.apply(this);
    }

    /**
     * Retrieves the birth day of the person holding this ID number as it is defined for a given
     * birth day assertion.
     *
     * @param birthDayFunction An implementation of an ID number's birth day assertion.
     * @return The birth day represented by this ID number as valid for the given assertion logic.
     */
    public Optional<LocalDate> birthday(final Function<IdNumber, Optional<LocalDate>> birthDayFunction) {
        return birthDayFunction.apply(this);
    }

    /**
     * Retrieves the age of the person holding this ID number as it is defined for a given
     * birth day assertion.
     *
     * @param birthDayFunction An implementation of an ID number's birth day assertion.
     * @return The age as represented by this ID number as valid for the given assertion logic.
     */
    public Optional<Period> age(final Function<IdNumber, Optional<LocalDate>> birthDayFunction) {
        return birthDayFunction.apply(this)
                .map((birthday) -> birthday.until(now()))
                .map((period) -> period.isNegative() ? ZERO : period);
    }
}
