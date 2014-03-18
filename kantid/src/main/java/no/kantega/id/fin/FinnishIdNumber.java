package no.kantega.id.fin;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.valueOf;
import static java.util.Optional.empty;
import static java.util.regex.Pattern.compile;
import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;

public class FinnishIdNumber extends LocalIdNumber {

    private static final int GENDER_BIT = 7;

    private static final String FINLAND = "FI";

    private static final Locale LOCALE_FI = new Locale("fi", FINLAND);

    private static final char[] CONTROL_CHARS = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'H', 'J', 'K', 'L',
        'M', 'N', 'P', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Z'
    };

    private static final Pattern IDNUMBER_PATTERN =
        compile("([0-3][0-9])([0-1][0-9])([0-9]{2})(\\-|[A]|\\+)([0-9]{3})([0-9]|[A-Z])");

    private static final int DAY = 1;

    private static final int MONTH = 2;

    private static final int YEAR = 3;

    private static final int SEPARATOR = 4;

    private static final int CONTROL_NUMBER = 5;

    private static final int CONTROL_CHAR = 6;

    private static final int DIVIDER = 31;

    public FinnishIdNumber(final String idToken) {
        super(idToken, LOCALE_FI);
    }

    public FinnishIdNumber(final String idToken, Locale locale) {
        super(idToken, locale);
    }

    public static FinnishIdNumber forId(String idToken) {
        return new FinnishIdNumber(idToken);
    }


    public static FinnishIdNumber forId(String idToken, Locale locale) {
        return new FinnishIdNumber(idToken, locale);
    }

    public static Optional<Gender> gender(IdNumber idNumber) {
        return forId(idNumber.getIdToken()).genderBitOf(idNumber) % 2 == 0
            ? Optional.of(FEMALE)
            : Optional.of(MALE);
    }

    public static boolean valid(final IdNumber idNumber) {
        FinnishIdNumber finnishIdNumber = forId(idNumber.getIdToken());
        Matcher format = IDNUMBER_PATTERN.matcher(finnishIdNumber.getIdToken());
        return format.matches()
               && finnishIdNumber.birthdayFor(format).isPresent()
               && finnishIdNumber.hasValidControl(format);
    }

    public static Optional<LocalDate> birthday(final IdNumber idNumber) {
        final Matcher format = IDNUMBER_PATTERN.matcher(idNumber.getIdToken());
        if (format.matches()) {
            return forId(idNumber.getIdToken()).birthdayFor(format);
        }
        return empty();

    }

    private int genderBitOf(IdNumber idNumber) {
        return (int) idNumber.getIdToken().charAt(GENDER_BIT);
    }

    private boolean hasValidControl(Matcher idFormat) {
        int controlNumber = valueOf(idFormat.group(DAY) + idFormat.group(MONTH) +
                                    idFormat.group(YEAR) + idFormat.group(CONTROL_NUMBER));
        return idFormat.group(CONTROL_CHAR).charAt(0) == CONTROL_CHARS[controlNumber % DIVIDER];
    }

    private Optional<LocalDate> birthdayFor(Matcher format) {
        try {
            return Optional.of(
                LocalDate.of(centuryFrom(format.group(SEPARATOR).charAt(0)) + valueOf(format.group(YEAR)),
                    valueOf(format.group(MONTH)),
                    valueOf(format.group(DAY))));
        } catch (DateTimeException e) {
            return Optional.empty();
        }
    }

    private int centuryFrom(char separator) {
        switch (separator) {
            case '+':
                return 1800;
            case '-':
                return 1900;
        }
        return 2000;
    }

    @Override
    protected boolean supports(Locale locale) {
        return locale != null && FINLAND.equals(locale.getCountry());
    }

}
