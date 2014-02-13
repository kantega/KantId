package no.kantega.id.fin;

import no.kantega.id.api.Gender;
import no.kantega.id.api.IdNumber;

import static no.kantega.id.api.Gender.FEMALE;
import static no.kantega.id.api.Gender.MALE;

public class FinnishIdNumber extends IdNumber {

    private static final int TOKEN_LENTGH = 11;

    public FinnishIdNumber(final String idToken) {
        super(idToken);
        if (idToken.length() != TOKEN_LENTGH) {
            throw new IllegalArgumentException("Finnish token has 11 characters.");
        }
    }

    public static FinnishIdNumber forId(String idToken) {
        return new FinnishIdNumber(idToken);
    }

    public Gender genderOf(IdNumber idNumber) {
        return genderBitOf(idNumber) % 2 == 0 ? FEMALE : MALE;
    }

    public static Gender gender(IdNumber idNumber) {
        FinnishIdNumber finnishIdNumber = forId(idNumber.getIdToken());
        return finnishIdNumber.genderOf(finnishIdNumber);
    }

    private int genderBitOf(IdNumber idNumber) {
        String token = idNumber.getIdToken();
        return (int) token.charAt(token.length() - 2);
    }

    public boolean validity(final IdNumber idNumber) {
        return idNumber.getIdToken().length() == TOKEN_LENTGH;
    }

    public static boolean valid(final IdNumber idNumber) {
        final String token = idNumber.getIdToken();
        return token.length() == TOKEN_LENTGH && token.contains("-");
    }

}
