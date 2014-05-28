package no.kantega.id.ie;

import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Representation of a <i>Personal Public Service Number (PPS No)</i> which is issued by a number of public services,
 * including education, health, housing, social welfare, and tax. A5. A PPS No. is always 7 digits,
 * and followed by either one or two letters. If there are two letters, the second letter is always a letter 'W'.
 * <p>
 * The check character is calculated using a weighted addition of all the numbers and modulus calculation. It
 * therefore checks for incorrectly entered digits and for digit transposition (digits in the wrong order will alter
 * the sum due to weightings).
 * </p>
 *
 * @see no.kantega.id.api.IdNumber
 */
public class PersonalPublicServiceNumber extends LocalIdNumber {

    private static final String IRELAND = "IE";

    private static final Locale LOCALE_IE = new Locale("ga", IRELAND);

    private static final char[] CONTROL_CHARS = {
        'W',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
        'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V'
    };

    private static final Pattern IDNUMBER_PATTERN =
        compile("([0-9]{7})([A-Z])([A-Z]?)");

    private static final int NUMBER = 1;

    private static final int CONTROL_CHAR = 2;

    private static final int SECOND_CHAR_OPT = 3;

    public PersonalPublicServiceNumber(final String idToken) {
        super(idToken, LOCALE_IE);
    }

    public PersonalPublicServiceNumber(final String idToken, Locale locale) {
        super(idToken, locale);
    }

    /**
     * Provide an instance of IdNumber with Irish locale (country Ireland)
     * and implementation for methods of {@link no.kantega.id.api.IdNumber}.
     *
     * @param idToken of idNumber.
     * @return instance of PersonalPublicServiceNumber with locale set to "ga_IE".
     */
    public static PersonalPublicServiceNumber forId(String idToken) {
        return new PersonalPublicServiceNumber(idToken);
    }

    /**
     * Provide an instance of IdNumber with given locale (country Ireland is only supported)
     * and implementation for methods of {@link no.kantega.id.api.IdNumber}.
     *
     * @param idToken of idNumber.
     * @param locale for idNumber, must be supported.
     * @return instance of PersonalPublicServiceNumber.
     * @throws IllegalArgumentException if locale is unsupported. Only Irish locale is supported.
     */
    public static PersonalPublicServiceNumber forId(String idToken, Locale locale) {
        return new PersonalPublicServiceNumber(idToken, locale);
    }

    @Override
    protected boolean supports(Locale locale) {
        return locale != null && IRELAND.equals(locale.getCountry());
    }

    /**
     * Provide our own #cleanup-method to upper-case any characters in the ppsn.
     *
     * @param idNumber    id numnber string representation to clean.
     * @return trimmed and uppercased string representation of ppsn.
     */
    @Override
    protected String cleanup(String idNumber) {
        String in = StringUtils.trim(idNumber);
        return in.toUpperCase(LOCALE_IE);
    }

    private boolean hasValidControl(Matcher idFormat) {
        String number = idFormat.group(NUMBER);
        char c1 = idFormat.group(CONTROL_CHAR).charAt(0);

        int[] weights = {8,7,6,5,4,3,2};
        int sum = 0;

        for (int i = 0; i < number.length(); i++) {
            sum = sum + weights[i] * digitAt(number, i);
        }

        int ctrl = (sum % 23);
        char controlChar = CONTROL_CHARS[ctrl];

        return c1 == controlChar;
    }

    /**
     * A5. A PPS No. is always 7 numbers, and followed by either one or two letters. If there are two letters,
     * the second letter is always a letter 'W'.
     */
    private boolean secondCharMissingOrValid(Matcher idFormat) {
        String group = idFormat.group(SECOND_CHAR_OPT);
        boolean hasOptionalChar = group != null && !group.isEmpty();
        if (hasOptionalChar) {
            char optionalChar = group.charAt(0);
            return 'W' == optionalChar;
        } else {
            return true;
        }
    }

    private int digitAt(String number, int i) {
        return Character.getNumericValue(number.charAt(i));
    }

    /**
     * Standard implementation of validity check for Irish instances of IdNumber.
     *
     * Valid idnumber is expected to be formatted as seven digits, followed by one control
     * character, and one optional character. If the second optional character is present, it must be 'W'.
     *
     * @param idNumber The idNumber to validate.
     * @return true if idNumber is evaluated to be a valid Irish Personal Public Service Number.
     */
    public static boolean valid(final IdNumber idNumber) {
        PersonalPublicServiceNumber ppsn = forId(idNumber.getIdToken());
        Matcher format = IDNUMBER_PATTERN.matcher(ppsn.getIdToken());
        return format.matches()
               && ppsn.hasValidControl(format)
               && ppsn.secondCharMissingOrValid(format);
    }

}
