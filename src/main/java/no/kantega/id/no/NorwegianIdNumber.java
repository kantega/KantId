package no.kantega.id.no;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

/**
 * Representation of a <i>f&oslash;delsnummer</i> which is issued by the Norwegian Skatteetaten. A <i>f&oslash;
 * dselsnummer</i> consists of  11 digits where the first six digits mark the birthday in the <i>DDMMYY</i> format containing the last two digits of the
 * birth year. The following five numbers are also known as the <i>personnummer</i> which differentiates between individuals.
 * The last two digits of the <i>personnummer</i> are control numbers, specified by
 * <ol>
 * <li>{@code k1 = 11 - ((3 * d1 + 7 * d2 + 6 * m1 + 1 * m2 + 8 * y1 + 9 * y2 + 4 * p1 + 5 * p2 + 2 * p3) % 11)}</li>
 * <li>{@code k2 = 11 - ((5 * d1 + 4 * d2 + 3 * m1 + 2 * m2 + 7 * y1 + 6 * y2 + 5 * p1 + 4 * p2 + 3 * p3 + 2 * k1) % 11)}</li>
 * </ol>
 * with {@code kn} representing the {@code n}-th control number and {@code d}, {@code m}, {@code y}, {@code p} equally presenting
 * the day, month or year of birth as given by the <i>f&oslash;dselsnummer</i> and {@code p} meaning a digit of the <i>personnummer</i>.
 * <p>
 * The third digit of the <i>personnummer</i> gives information about the gender of a person with even digits for female and
 * odd digits for male persons. The exact birth year can be deducted from the known last two digits together with the <i>personnummer</i>
 * where the first three digits of the <i>personnummer</i> determines:
 * <ol>
 * <li>000 - 499: A person is born in the 1900.</li>
 * <li>500 - 749: A person is born between 1854 - 1899.</li>
 * <li>500 - 999: A person is born between 2000 - 2039.</li>
 * <li>900 - 999: A person is born between 1940 - 1999.</li>
 * </ol>
 * <p>
 * Note that a Norwegian ID number's validity is dependant of the type as defined by {@link no.kantega.id.no.NorwegianIdNumber.Type}.
 *
 * @see no.kantega.id.no.NorwegianIdNumber.Type
 * @see no.kantega.id.api.IdNumber
 */
public class NorwegianIdNumber extends LocalIdNumber {

    /**
     * Describes the type of the given Norwegian ID.
     */
    public static enum Type {
        /**
         * A F-number represents a normal ID as assigned to each Norwegian citizen and to people living in Norway on
         * a long-term basis.
         */
        FNUMBER(true),

        /**
         * A D-number is assigned to people that are living in Norway on a short term basis. Compared to F-numbers,
         * the number 4 is added to the first digit in order to differentiate D-numbers.
         */
        DNUMBER(true),

        /**
         * H-numbers are assigned to persons that are neither assigned a F-number or a D-number. They are marked by
         * adding the number 4 to the third digit in order to differentiate H-numbers.
         */
        HNUMBER(true),

        /**
         * FH-numbers are assigned in medical context where the numbers cannot be interpreted besides their validity.
         * They are marked by an 8 or 9 as their first digit.
         */
        FHNUMBER(false);

        private final boolean verbose;

        private Type(boolean verbose) {
            this.verbose = verbose;
        }

        /**
         * Determines if this type of ID number is verbose, i.e. contains information on birthday and gender.
         *
         * @return {@code true} if this type is verbose.
         */
        public boolean isVerbose() {
            return verbose;
        }

        @Override
        public String toString() {
            return "NorwegianIdNumber.Type." + this.name();
        }
    }

    private interface Interpreted {

        enum Obscure implements Interpreted {
            INSTANCE;

            @Override
            public boolean isValid() {
                return false;
            }

            @Override
            public Optional<Gender> getGender() {
                return Optional.empty();
            }

            @Override
            public Optional<Type> getType() {
                return Optional.empty();
            }


            @Override
            public Optional<LocalDate> getBirthday() {
                return Optional.empty();
            }
        }

        class Anonymous implements Interpreted {

            private final boolean valid;

            public Anonymous(boolean valid) {
                this.valid = valid;
            }

            @Override
            public boolean isValid() {
                return valid;
            }

            @Override
            public Optional<Gender> getGender() {
                return Optional.empty();
            }

            @Override
            public Optional<Type> getType() {
                return Optional.of(Type.FHNUMBER);
            }

            @Override
            public Optional<LocalDate> getBirthday() {
                return Optional.empty();
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
            public Optional<Type> getType() {
                return Optional.of(type);
            }

            @Override
            public Optional<Gender> getGender() {
                return Optional.of(gender);
            }

            @Override
            public Optional<LocalDate> getBirthday() {
                return Optional.of(birthday);
            }
        }

        boolean isValid();

        Optional<Gender> getGender();

        Optional<Type> getType();

        Optional<LocalDate> getBirthday();
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
        if (day > 80) {
            return new Interpreted.Anonymous(validChecksum);
        } else if (day > 40) {
            day -= 40;
            type = Type.DNUMBER;
        } else if (month > 40) {
            month -= 40;
            type = Type.HNUMBER;
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

    private static final String NORWAY = "NO";

    private static final Locale LOCALE_NORWAY = new Locale("no", NORWAY);

    /**
     * Creates a new Norwegian id number representation with Norwegian locale.
     *
     * @param idToken The id token for this id.
     */
    public NorwegianIdNumber(String idToken) {
        super(idToken, LOCALE_NORWAY);
    }

    /**
     * Creates a new Norwegian id number representation with the given locale.
     *
     * @param idToken The id token for this id.
     * @param locale  The locale of the ID.
     */
    public NorwegianIdNumber(String idToken, Locale locale) {
        super(idToken, locale);
    }

    /**
     * Convenience constructor method for creating a Norwegian ID with Norwegian locale.
     *
     * @param idToken The token for this id.
     * @return A new Norwegian ID token.
     */
    public static NorwegianIdNumber forId(String idToken) {
        return new NorwegianIdNumber(idToken);
    }

    /**
     * Convenience constructor method for creating a Norwegian ID with the given locale.
     *
     * @param idToken The token for this id.
     * @param locale  The locale of the ID.
     * @return A new Norwegian ID token.
     */
    public static NorwegianIdNumber forId(String idToken, Locale locale) {
        return new NorwegianIdNumber(idToken, locale);
    }

    /**
     * Extracts the gender of the given ID.
     *
     * @param idNumber The ID to be examined.
     * @return The gender of this ID by the Norwegian ID definition, if retrievable.
     */
    public static Optional<Gender> gender(IdNumber idNumber) {
        return parse(idNumber.getIdToken()).getGender();
    }

    /**
     * Extracts the gender of this ID.
     *
     * @return The gender of this ID, if known.
     */
    public Optional<Gender> gender() {
        return gender(this);
    }

    /**
     * Checks the validity of a given ID.
     *
     * @param idNumber The ID to be examined.
     * @return {@code true} if the given ID is valid by measures of the Norwegian ID definition.
     */
    public static boolean valid(final IdNumber idNumber) {
        return parse(idNumber.getIdToken()).isValid();
    }

    /**
     * Checks the validity of this ID.
     *
     * @return {@code true} if this ID is valid by measures of the Norwegian ID definition.
     */
    public boolean isValid() {
        return valid(this);
    }

    /**
     * Extracts the birthday of the given ID.
     *
     * @param idNumber The ID to be examined.
     * @return The birthday of the person this ID is assigned to by the Norwegian ID definition, if retrievable.
     */
    public static Optional<LocalDate> birthday(final IdNumber idNumber) {
        return parse(idNumber.getIdToken()).getBirthday();
    }

    /**
     * Extracts the birthday of this ID.
     *
     * @return The birthday of the person this ID is assigned to by the Norwegian ID definition, if retrievable.
     */
    public Optional<LocalDate> birthday() {
        return birthday(this);
    }

    /**
     * Extracts the type of the given ID.
     *
     * @param idNumber The ID to be examined.
     * @return The type of the ID by the Norwegian ID definition, if retrievable.
     */
    public static Optional<Type> type(final IdNumber idNumber) {
        return parse(idNumber.getIdToken()).getType();
    }

    /**
     * Extracts the type of this ID.
     *
     * @return The type of this ID by the Norwegian ID definition, if retrievable.
     */
    public Optional<Type> type() {
        return type(this);
    }

    @Override
    protected boolean supports(Locale locale) {
        return locale != null && NORWAY.equals(locale.getCountry());
    }
}
