package no.kantega.id.dk;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static java.lang.Character.getNumericValue;
import static java.lang.Integer.parseInt;
import static java.util.Optional.empty;
import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;

/**
 * Representation of Danish personal identity number (CPR number) based on
 * official CPR <a href="https://cpr.dk/media/167692/personnummeret%20i%20cpr.pdf">documentation</a>
 *
 *
 */
public class DanishIdNumber extends LocalIdNumber {



    private static final Locale LOCALE_DENMARK = new Locale("da", "DK");

    private static final int GENDER_BIT = 9;

    private static final int CONTROL_MODULO = 11;

    private static final String VALID_FORMAT_PATTERN = "\\d{10}";

    public DanishIdNumber(String idToken, Locale locale) {
        super(idToken, locale);
    }


    /**
     * Provide an instance of IdNumber with Danish locale (country Denmark)
     * and implementation for methods of {@link IdNumber}.
     *
     * @param idToken of idNumber.
     * @return instance of DanishIdNumber with locale set to "da_DK".
     */
    public static DanishIdNumber forId(String idToken) {
        return new DanishIdNumber(idToken, LOCALE_DENMARK);
    }

    /**
     * Provide an instance of IdNumber with given locale (country Denmark is only supported)
     * and implementation for methods of {@link IdNumber}.
     *
     * @param idToken of idNumber.
     * @param locale for idNumber, must be supported.
     * @return instance of DanishIdNumber.
     * @throws IllegalArgumentException if locale is not supported. This class supports
     * only locales with country set as Denmark.
     */
    public static DanishIdNumber forId(String idToken, Locale locale) {
        return new DanishIdNumber(idToken, locale);
    }

    /**
     * Standard validity check for Danish idNumbers. This method does not perform
     * modulus validation as Danish CPR numbers issued after year 2007 do
     * not necessarily pass the modulus test.
     *
     * Valid id number is expected in the following format: {@code ddMMyyCxxG} where:
     * <ul>
     * <li>ddMMyy: day of birth</li>
     * <li>C: century part. The combination of yy and C decide the exact year</li>
     * <li>xx: a serial number</li>
     * <li>G: gender part. Odd numbers represent males, while even numbers represent female.</li>
     * </ul>
     *
     * See official documentation or Wikipedia for more information.
     *
     * @param idNumber The idNumber to validate.
     * @return true when valid id number, based on a given specification for Denmark.
     */
    public static boolean valid(final IdNumber idNumber) {
        return birthday(idNumber).isPresent();
    }

    /**
     * Validity check for Danish idNumbers that performs modulus 11 test. Note that
     * Danish CPR numbers issued after year 2007 do not necessarily pass the modulus
     * test, but can still be valid.
     *
     * Valid id number is expected in the following format: {@code ddMMyyCxxG} where:
     * <ul>
     * <li>ddMMyy: day of birth</li>
     * <li>C: century part. The combination of yy and C decide the exact year</li>
     * <li>xx: a serial number</li>
     * <li>G: gender part and modulus 11 check number. Odd numbers represent males,
     * while even numbers represent female.</li>
     * </ul>
     *
     * See official documentation or Wikipedia for more information.
     *
     * @param idNumber The idNumber to validate.
     * @return true when valid id number, based on a given specification for Denmark.
     */
    public static boolean validateModulus11(final IdNumber idNumber) {
        if (!validateFormat(idNumber)) {
            return false;
        }

        int controlSum =
            4 * getNumericValue(idNumber.getIdToken().charAt(0)) +
            3 * getNumericValue(idNumber.getIdToken().charAt(1)) +
            2 * getNumericValue(idNumber.getIdToken().charAt(2)) +
            7 * getNumericValue(idNumber.getIdToken().charAt(3)) +
            6 * getNumericValue(idNumber.getIdToken().charAt(4)) +
            5 * getNumericValue(idNumber.getIdToken().charAt(5)) +
            4 * getNumericValue(idNumber.getIdToken().charAt(6)) +
            3 * getNumericValue(idNumber.getIdToken().charAt(7)) +
            2 * getNumericValue(idNumber.getIdToken().charAt(8)) +
            getNumericValue(idNumber.getIdToken().charAt(9));
        return controlSum % CONTROL_MODULO == 0;
    }

    /**
     * Extracts the optional gender from the given person number following the
     * specification for the danish CPR number.
     * Note that this method doesn't check the validity of the idNumber, it merely
     * extracts gender bit of idNumber and interprets it.
     *
     * @param idNumber where gender is taken from.
     * @return Optional gender (male or female), or empty in case of non-digit gender bit.
     */
    public static Optional<Gender> gender(final IdNumber idNumber) {
        if (!validateFormat(idNumber)) {
            return empty();
        }
        return (getNumericValue(idNumber.getIdToken().charAt(GENDER_BIT)) & 1) == 0
            ? Optional.of(FEMALE)
            : Optional.of(MALE);
    }

    /**
     * Calculates optional birthday for the given id number. Calculation is based on
     * 7 first characters of the idNumber where the 7th character represents century.
     *
     * @param idNumber where birthday is calculated from.
     * @return optional birthday, or empty in case of invalid date or invalid idNumber format.
     */
    public static Optional<LocalDate> birthday(final IdNumber idNumber) {
        if (!validateFormat(idNumber)) {
            return empty();
        }

        int day= parseInt(idNumber.getIdToken().substring(0, 2));
        int month= parseInt(idNumber.getIdToken().substring(2, 4));
        int shortYear= parseInt(idNumber.getIdToken().substring(4, 6));
        int year = calculateYear(shortYear, getNumericValue(idNumber.getIdToken().charAt(6)));

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            return empty();
        }

        return Optional.of(parsedDate);
    }

    private static int calculateYear(int shortYear, int yearCenturyPart) {
        int century;
        if (yearCenturyPart >= 0 && yearCenturyPart <= 3) {
            century = 1900;
        } else if (yearCenturyPart == 4 || yearCenturyPart == 9) {
            if (shortYear >= 0 && shortYear <= 36) {
                century = 2000;
            } else {
                century = 1900;
            }
        } else {
            if (shortYear >= 58 && shortYear <= 99) {
                century = 1800;
            } else {
                century = 2000;
            }
        }
        return century + shortYear;
    }

    private static boolean validateFormat(final IdNumber number) {
        return number.getIdToken().matches(VALID_FORMAT_PATTERN);
    }

    @Override
    protected boolean supports(Locale locale) {
        return locale != null && LOCALE_DENMARK.getCountry().equals(locale.getCountry());
    }

}
