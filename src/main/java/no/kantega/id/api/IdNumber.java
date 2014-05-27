package no.kantega.id.api;

import org.apache.commons.lang3.StringUtils;

import java.lang.RuntimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.time.LocalDate.now;
import static java.time.Period.ZERO;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
        this.idToken = cleanup(idToken);
    }

    /**
     * Creates a new ID number for a given token.
     *
     * @param idToken The token to represent.
     * @return The created ID number.
     */
    public static IdNumber forId(final String idToken) {
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
     * gender assertion. Execution of <i>genderFunction</i> will never throw exception
     * or return null.
     *
     * @param genderFunction An implementation of an ID number's gender assertion.
     * @return The gender represented by this ID number as valid for the given assertion logic.
     */
    public Optional<Gender> gender(final Function<IdNumber, Optional<Gender>> genderFunction) {
        return ensureOptionalOf(genderFunction);
    }

    /**
     *  Retrieves the gender of the person holding this ID number as it is defined for a given
     *  gender assertion. Clients implementing <i>genderFunction</i> are encouraged to check
     *  validity of ID number before retrieving gender from ID number, as well as throw
     *  {@link RuntimeException} if ID number is not valid.
     *
     * @param genderFunction An implementation of an ID number's gender assertion.
     * @return The gender represented by this ID number. Pay attention that this method
     * can also return {@code null}.
     */
    public Gender getGender(final Function<IdNumber, Gender> genderFunction) {
        return genderFunction.apply(this);
    }

    /**
     * Retrieves the birth day of the person holding this ID number as it is defined for a given
     * birth day assertion. Execution of <i>birthDayFunction</i> will never throw exception
     * or return null.
     *
     * @param birthDayFunction An implementation of an ID number's birth day assertion.
     * @return The birth day represented by this ID number as valid for the given assertion logic.
     */
    public Optional<LocalDate> birthday(final Function<IdNumber, Optional<LocalDate>> birthDayFunction) {
        return ensureOptionalOf(birthDayFunction);
    }

    /**
     * Retrieves the birth day of the person holding this ID number as it is defined for a given
     * birth day assertion. Clients implementing <i>birthDayFunction</i> are encouraged to check
     * validity of ID number before retrieving birth date from ID number, as well as throw
     * {@link RuntimeException} if ID number is not valid.
     *
     * @param birthDayFunction An implementation of an ID number's birth day assertion.
     * @return The birth day represented by this ID number. Pay attention that this method
     * can also return {@code null}.
     */
    public LocalDate getBirthday(final Function<IdNumber, LocalDate> birthDayFunction) {
        return birthDayFunction.apply(this);
    }

    /**
     * Execute the given function in a context where null return values and exceptions are converted
     * to empty {@link Optional}.
     */
    private <T> Optional<T> ensureOptionalOf(final Function<IdNumber, Optional<T>> ensuredFunction) {
        final Optional<T> optionalValue;
        try {
            optionalValue = ensuredFunction.apply(this);
        } catch (final Exception e) {
            return Optional.<T>empty();
        }
        return optionalValue != null ? optionalValue : Optional.<T>empty();
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
            .map(birthday -> birthday.until(now()))
            .map(age -> age.isNegative() ? ZERO : age);
    }

    public Period getAge(final Function<IdNumber, LocalDate> birthDayFunction) {
        final Period age = birthDayFunction.apply(this).until(now());
        return age.isNegative() ? ZERO : age;
    }

    /**
     * Clean up idToken before setting #idToken field.
     *
     * Implementing classes can override this to provide their own ways to clean an id number.
     *
     * @param idNumber    id numnber string representation to clean.
     * @return A clean (default: trimmed) version of the id number.
     */
    protected String cleanup(String idNumber) {
        return StringUtils.trim(idNumber);
    }
}
