package no.kantega.id.no;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

public class NorwegianIdNumber extends LocalIdNumber {

    public static enum Type {
        FNUMBER,
        DNUMBER,
        FHNUMBER,
        HNUMBER
    }

    private interface Interpreted {

        enum Obscure implements Interpreted {
            INSTANCE;

            @Override
            public boolean isValid() {
                return false;
            }

            @Override
            public Gender getGender() {
                return Gender.UNKNOWN;
            }

            @Override
            public Type getType() {
                throw new IllegalStateException("Cannot determine type");
            }


            @Override
            public LocalDate getBirthday() {
                throw new IllegalStateException("Birthday is not known");
            }
        }

        class Readable implements Interpreted {

            private final Gender gender;
            private final LocalDate birthday;
            private final Type type;
            private final boolean valid;

            public Readable(Gender gender, LocalDate birthday, Type type, boolean valid) {
                this.gender = gender;
                this.birthday = birthday;
                this.type = type;
                this.valid = valid;
            }

            @Override
            public boolean isValid() {
                return valid;
            }

            @Override
            public Type getType() {
                return type;
            }

            @Override
            public Gender getGender() {
                return gender;
            }

            @Override
            public LocalDate getBirthday() {
                return birthday;
            }
        }

        boolean isValid();

        Gender getGender();

        Type getType();

        LocalDate getBirthday();
    }

    private static Interpreted parse(String id) {

        if (!id.matches("[0-9]{11}")) {
            return Interpreted.Obscure.INSTANCE;
        }

        int[] digit = new int[11];
        for (int i = 0; i < digit.length; i++) {
            digit[i] = Character.getNumericValue(id.charAt(i));
        }

        boolean validChecksum = true;

        int control1 = 11 - ((3 * digit[0] + 7 * digit[1] + 6 * digit[2] + digit[3]
                + 8 * digit[4] + 9 * digit[5] + 4 * digit[6] + 5 * digit[7] + 2 * digit[8]) % 11);
        if (control1 == 11) control1 = 0;
        if (control1 != digit[9]) {
            validChecksum = false;
        }

        int control2 = 11 - ((5 * digit[0] + 4 * digit[1] + 3 * digit[2] + 2 * digit[3]
                + 7 * digit[4] + 6 * digit[5] + 5 * digit[6] + 4 * digit[7] +
                3 * digit[8] + 2 * digit[9]) % 11);
        if (control2 == 11) control2 = 0;
        if (control2 != digit[10]) {
            validChecksum = false;
        }

        int ageRegion = Integer.valueOf(id.substring(6, 9));
        int day = Integer.valueOf(id.substring(0, 2));
        int month = Integer.valueOf(id.substring(2, 4));
        int year = Integer.valueOf(id.substring(4, 6));

        Type type;
        if (day > 40) {
            day -= 40;
            type = Type.DNUMBER;
        } else if (month > 30) {
            month -= 30;
            type = Type.HNUMBER;
        } else if (day > 80) {
            day -= 80;
            type = Type.FHNUMBER;
        } else {
            type = Type.FNUMBER;
        }

        if (ageRegion < 500) {
            year += 1900;
        } else if (ageRegion < 750 && year >= 54) {
            year += 1800;
        } else if (year < 40) {
            year += 2000;
        } else {
            year += 1900;
        }

        Gender gender = digit[8] % 2 == 0 ? Gender.FEMALE : Gender.MALE;

        try {
            return new Interpreted.Readable(gender, LocalDate.of(year, month, day), type, validChecksum);
        } catch (DateTimeException e) {
            return Interpreted.Obscure.INSTANCE;
        }
    }

    public static final String NORWAY = "NO";

    private static final Locale LOCALE_NORWAY = new Locale("no", NORWAY);

    public NorwegianIdNumber(String idToken) {
        super(idToken, LOCALE_NORWAY);
    }

    public NorwegianIdNumber(String idToken, Locale locale) {
        super(idToken, locale);
    }

    public static NorwegianIdNumber forId(String idToken) {
        return new NorwegianIdNumber(idToken);
    }

    public static NorwegianIdNumber forId(String idToken, Locale locale) {
        return new NorwegianIdNumber(idToken, locale);
    }

    public static Gender gender(IdNumber idNumber) {
        return parse(idNumber.getIdToken()).getGender();
    }

    public static boolean valid(final IdNumber idNumber) {
        return parse(idNumber.getIdToken()).isValid();
    }

    public static LocalDate birthday(final IdNumber idNumber) {
        return parse(idNumber.getIdToken()).getBirthday();
    }

    public static Period age(final IdNumber idNumber) {
        return birthday(idNumber).periodUntil(LocalDate.now());
    }

    public static Type type(final IdNumber idNumber) {
        return parse(idNumber.getIdToken()).getType();
    }

    @Override
    protected boolean supports(Locale locale) {
        return locale != null && NORWAY.equals(locale.getCountry());
    }
}
