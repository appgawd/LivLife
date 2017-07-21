package com.bytefruit.patri.carpediem;

/**
 * Created by mrimagine on 3/19/17.
 */

public class UserProfile {
    final UserDate bday;


    final double inches;
    final double age;
    final double pounds;
    final int yearsOfEd;
    final int yearsOfWork;
    final int yearsPlannedToWork;
    final double annualIncome;
    final double lifeExpFromBirth;


    final String exercise;
    final String health;
    final String outlook;
    final String alcohol;
    final String smoke;
    final String country;
    final String marriageStatus;
    final String gender;
    final String race;
    double timeLeft;

    public UserProfile(
            UserDate bday,

            double inches,
            double age,
            double pounds,
            int yearsOfEd,
            int yearsOfWork,
            int yearsPlannedToWork,
            double annualIncome,
            double lifeExpFromBirth,
            String exercise,
            String health,
            String outlook,
            String alcohol,
            String smoke,
            String country,
            String marriageStatus,
            String gender,
            String race) {

        this.bday = bday; //timedate object

        this.inches = inches;
        this.age = age;
        this.pounds = pounds;
        this.yearsOfEd = yearsOfEd;
        this.yearsOfWork = yearsOfWork;
        this.yearsPlannedToWork = yearsPlannedToWork;
        this.annualIncome = annualIncome;
        this.lifeExpFromBirth = lifeExpFromBirth;
        this.exercise = exercise;
        this.health = health;
        this.outlook = outlook;
        this.alcohol = alcohol;
        this.smoke = smoke;
        this.country = country;
        this.marriageStatus = marriageStatus;
        this.gender = gender;
        this.race = race;


    }


    public String toString() {
        String result = "";
        result = "Birth date: " + bday.toString();
        result += "\n" + "Age: " + age;
        result += "\n" + "Race: " + race;
        result += "\n" + "Gender: " + gender;
        result += "\n" + "Pounds: " + pounds;
        result += "\n" + "Inches: " + inches;
        result += "\n" + "Years of education: " + yearsOfEd;
        result += "\n" + "Years of work: " + yearsOfWork;
        result += "\n" + "Years planned to work: " + yearsPlannedToWork;
        result += "\n" + "Annual income: " + annualIncome;
        result += "\n" + "Life expectancu from birth: " + lifeExpFromBirth;
        result += "\n" + "Exercise: " + exercise;
        result += "\n" + "Heatlh: " + health;
        result += "\n" + "Outlook: " + outlook;
        result += "\n" + "Alcohol: " + alcohol;
        result += "\n" + "Smoke: " + smoke;
        result += "\n" + "Country: " + country;
        result += "\n" + "Marriage Status : " + marriageStatus;
        result += "\n" + "Life Left : " +timeLeft ;
        result += "\n" + "Age of death : " +(timeLeft + age) ;
        return result;



    }

}
