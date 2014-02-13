package no.kantega.id.api;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static no.kantega.id.api.Gender.UNKNOWN;

public class IdNumber {

    private final String idToken;

    public IdNumber(final String idToken) {
        requireNonNull(idToken, "IdToken must not be null.");
        this.idToken = idToken;
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
}
