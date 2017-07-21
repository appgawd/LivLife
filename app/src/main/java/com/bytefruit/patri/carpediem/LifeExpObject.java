package com.bytefruit.patri.carpediem;

/**
 * Created by mrimagine on 3/19/17.
 */

public class LifeExpObject {
    double yearsMale;
    double yearsFemale;
    String country;

    public LifeExpObject(String country, double yearMale, double yearFemale) {
        this.yearsMale = yearMale;
        this.yearsFemale = yearFemale;
        this.country = country;
    }

    public String toString() {
        String s = "Country: "  + country + " | Male Life Exp: " + yearsMale + " | Female Life Exp: " + yearsFemale;
        return s;
    }
}

