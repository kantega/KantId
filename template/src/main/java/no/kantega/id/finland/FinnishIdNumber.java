package no.kantega.id.finland;

import no.kantega.id.api.BirthDateAware;
import no.kantega.id.api.IdNumber;
import no.kantega.id.api.LocaleAware;
import no.kantega.id.api.OrdinalAware;
import no.kantega.id.api.SexAware;
import org.joda.time.LocalDate;
import org.joda.time.Period;

public final class FinnishIdNumber implements LocaleAware, SexAware, BirthDateAware, OrdinalAware, IdNumber {

    private static final String FINLAND = "FI";

    FinnishIdNumber(String idString) {
    }


    @Override
    public LocalDate getBirthDate() {
        return null;
    }

    @Override
    public String readableFullAge() {
        return null;
    }

    @Override
    public String readbleFormattedAge(String format) {
        return null;
    }

    @Override
    public Period age() {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public IdNumber copy() {
        return null;
    }

    @Override
    public String getCountry() {
        return FINLAND;
    }

    @Override
    public boolean isBefore(OrdinalAware idNumber) {
        return false;
    }

    @Override
    public boolean isAfter(OrdinalAware idNumber) {
        return false;
    }

    @Override
    public boolean isMan() {
        return false;
    }

    @Override
    public boolean isWoman() {
        return false;
    }
}
