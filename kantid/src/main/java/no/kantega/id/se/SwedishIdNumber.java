package no.kantega.id.se;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

import static no.kantega.id.api.Gender.UNKNOWN;

public class SwedishIdNumber extends LocalIdNumber {

    public static final Locale SWEDEN = new Locale("se", "SE");

    public SwedishIdNumber(String idToken, Locale locale) {
        super(idToken, locale);
    }

    public SwedishIdNumber(String idToken) {
        super(idToken, SWEDEN);
    }

    @Override
    public boolean supports(Locale locale) {
        return SWEDEN.equals(locale);
    }

    /**
     * Provide an instance of IdNumber with swedish locale and provides
     * implementation of all the method supported.
     *
     * @param token The idToken
     * @return An instance of SwedishIdNumber with Locale set to "se", "SE"
     */
    public static SwedishIdNumber forId(String token) {
        return new SwedishIdNumber(token);
    }

    /**
     * Provide an instance of IdNumber with swedish locale and provides
     * implementation of all the method supported.
     *
     * @param token the idToken
     * @param locale The Locale for the current instance of SwedishIdNumber
     * @return An instance of SwedishIdNumber for the provided locale
     */
    public static SwedishIdNumber forId(String token, Locale locale) {
        return new SwedishIdNumber(token, locale);
    }

    /**
     * Standard implementation of validity check for Swedish idNumbers.
     * Implementation is based on Wikipedia specification.
     *
     * @param idNumber The IdNumber to validate
     * @return True when the token respect the specification given by wikipedia
     * for the Swedish National Id number
     */
    public static boolean isValid(IdNumber idNumber) {
        return false;
    }


    /**
     * Extracts the gender from the given person number follwing the
     * specification for the swedish person Number
     *
     * @param idNumber The IdNumber to consider
     * @return The gender associated to the given idNumber
     */
    public static Gender gender(IdNumber idNumber) {
        return UNKNOWN;
    }

    /**
     * Calculates the birthday date of the given IdNumber
     *
     * @param idNumber The IdNumber to consider
     * @return The birthday date associated to the given IdNumber
     */
    public static LocalDate birthdate(IdNumber idNumber) {
        return null;
    }

    /**
     * Calculates the age of the entity that has the given IdNumber
     *
     * @param idNumber The IdNumber to consider
     * @return The age associated to the entity that has the given IdNumber
     */
    public static Period age(IdNumber idNumber) {
        return null;
    }

}
