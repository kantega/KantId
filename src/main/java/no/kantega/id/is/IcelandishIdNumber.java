package no.kantega.id.is;

import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static java.lang.Character.getNumericValue;
import static java.lang.Integer.parseInt;
import static java.util.Optional.empty;

/**
 * Created by kristofferskaret on 14.04.14.
 */
public class IcelandishIdNumber extends LocalIdNumber {

    public static final Locale LOCALE_ICELAND = new Locale("is", "IS");

    protected IcelandishIdNumber(String idToken, Locale locale) {
        super(idToken, locale);
    }

    public static IcelandishIdNumber forId(String idToken) {
        return new IcelandishIdNumber(idToken, LOCALE_ICELAND);
    }

    public static IcelandishIdNumber forId(String idToken, Locale locale) {
        return new IcelandishIdNumber(idToken, locale);
    }

    @Override
    protected boolean supports(Locale locale) {
        return LOCALE_ICELAND.getCountry().equals(locale.getCountry());
    }

    public static boolean valid(final IdNumber idNumber) {
        return   idNumber.getIdToken().matches("\\d{6}-\\d{4}") &&
                        birthday(idNumber).isPresent();
    }

    /**
     * Calculates optional birthday for the given id number. Calculation is based on
     * 7 first characters of the idNumber where the 7th character represents century.
     *
     * @param idNumber where birthday is calculated from.
     * @return optional birthday, or empty in case of invalid date or invalid idNumber format.
     */
    public static Optional<LocalDate> birthday(final IdNumber idNumber) {

        int day = parseInt(idNumber.getIdToken().substring(0, 2));
        int month = parseInt(idNumber.getIdToken().substring(2, 4));
        int shortYear = parseInt(idNumber.getIdToken().substring(4, 6));

        int centuryDigit = getNumericValue(idNumber.getIdToken().charAt(10));
        int year = calculateYear(shortYear, centuryDigit);

        try {
            return Optional.of(LocalDate.of(year, month, day));
        } catch (DateTimeException e) {
            return empty();
        }
    }

    private static int calculateYear(int shortYear, int centuryDigit) {

        int century;
        if (centuryDigit == 0) {
            century = 2000;
        } else {
            century = 1000 + 100 * centuryDigit;
        }
        return century + shortYear;
    }

}
