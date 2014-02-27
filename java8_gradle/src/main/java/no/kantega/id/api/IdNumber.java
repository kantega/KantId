package no.kantega.id.api;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.Function;
import java.util.function.Predicate;

import static no.kantega.id.api.Gender.UNKNOWN;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class IdNumber {

    protected final String idToken;

    public IdNumber(final String idToken) {
        if (isBlank(idToken)) {
            throw new IllegalArgumentException("Id token was empty or null.");
        }
        this.idToken = trim(idToken);
    }

    public static IdNumber forId(String idNumber) {
        return new IdNumber(idNumber);
    }

    public String getIdToken() {
        return idToken;
    }

    public boolean isValid(final Predicate<IdNumber> validityTest) {
        return this.getIdToken() != null && validityTest.test(this);
    }

    public Gender gender(final Function<IdNumber, Gender> genderFunction) {
        Gender gender = genderFunction.apply(this);
        return gender != null ? gender : UNKNOWN;
    }

    public LocalDate birthday(final Function<IdNumber, LocalDate> birthDayFunction) {
        return birthDayFunction.apply(this);
    }

    public Period age(final Function<IdNumber, Period> ageFunction) {
        Period age = ageFunction.apply(this).normalized();
        if (age.isNegative()) {
            return Period.ZERO;
        }
        return age;

    }

}
