package no.kantega.id.fin;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocalIdNumber;

import java.util.Locale;
import java.util.function.Function;

import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;
import static org.apache.commons.lang3.StringUtils.containsAny;

public class FinnishIdNumber extends LocalIdNumber {

    private static final int TOKEN_LENTGH = 11;

    private static final String FINLAND = "FI";

    private static final Locale LOCALE_FI = new Locale("fi", FINLAND);

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

    public static Gender gender(IdNumber idNumber) {
        FinnishIdNumber finnishIdNumber = forId(idNumber.getIdToken());
        return finnishIdNumber.genderBitOf(idNumber) % 2 == 0 ? FEMALE : MALE;
    }

    private int genderBitOf(IdNumber idNumber) {
        String token = idNumber.getIdToken();
        return (int) token.charAt(token.length() - 2);
    }

    public static boolean valid(final IdNumber idNumber) {
        final String token = idNumber.getIdToken();
        return token.length() == TOKEN_LENTGH && containsAny(token, '-', '+', 'A');
    }

    public static Function<IdNumber, Gender> genderInFinland() {
        return FinnishIdNumber::gender;
    }

    @Override
    protected boolean supports(Locale locale) {
        return locale != null && FINLAND.equals(locale.getCountry());
    }

}
