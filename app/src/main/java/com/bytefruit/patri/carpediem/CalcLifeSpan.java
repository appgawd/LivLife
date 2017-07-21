package com.bytefruit.patri.carpediem;

import java.util.ArrayList;

/**
 * Created by mrimagine on 3/19/17.
 */

public class CalcLifeSpan {


    private static ArrayList<LifeExpObject> CountriesAndLifeSpanArray = new  ArrayList<LifeExpObject>();
    private static UserProfile user;
    private static double timeLeft = 0.0;






    public static UserProfile calculate(UserProfile userInput, ArrayList<LifeExpObject> LifeSpanFromBirth) {


        setArray();
        user = userInput;

        setTimeLeftAccoringToAge();
        calcLifeFromRace();

        calcLifeFromExercise();
        calcLifeFromHealth();
        calcLifeFromAlcohol();
        calcLifeFromSmoke();
        calcLifeFromOutlook();
        calcLifeFromIncome();
        calcLifeFromMarriage();
        calcLifeFromEducation();
        calcLifeFromYearsOfWork();
        calcLifeFromYearsPlannedToWork();
        calcLifeFromBMI();
        user.timeLeft = timeLeft;
        return user;
    }

    public static void setArray() { //sets local array
        for(int i = 0;i<LifeExpectFromBirthAndCountries.countries_lifeExp.size();i++) { //sets arraylist equal arraylist
            CountriesAndLifeSpanArray.add(i, LifeExpectFromBirthAndCountries.countries_lifeExp.get(i));
        }
    }



    public static void setTimeLeftAccoringToAge() {
        double age = user.age;


        if (age >= user.lifeExpFromBirth ) {

            double distance = age - user.lifeExpFromBirth ;

            if (distance <= 10) {
                timeLeft = 10;

            }

            else if (distance > 10 && distance <= 20) {

                timeLeft = 8;
            }

            else {
                timeLeft = 4;
            }
        }

        else if (age < user.lifeExpFromBirth) {

            timeLeft = user.lifeExpFromBirth - age;
        }

        //Jakes Edit
        double ageFactor = age / 10.0;
        timeLeft = Utilities.calcCorrectTimeLeft((int) -ageFactor, user.lifeExpFromBirth, timeLeft, user.age);
    }

    public static void calcLifeFromRace() { //fix

        if(user.race.equals("Black")) {
            timeLeft = Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);
        }
        if(user.race.equals("White")) {
            timeLeft = Utilities.calcCorrectTimeLeft(+2, user.lifeExpFromBirth, timeLeft, user.age);

        }
        if(user.race.equals("Hispanic")) {
            timeLeft = Utilities.calcCorrectTimeLeft(-1, user.lifeExpFromBirth, timeLeft, user.age);

        }
        if(user.race.equals("Asian")) {
            timeLeft = Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);

        }
        if(user.race.equals("Other")) {
            timeLeft = Utilities.calcCorrectTimeLeft(1, user.lifeExpFromBirth, timeLeft, user.age);

        }

    }




    public static void calcLifeFromBMI() {
        if(user.inches == 0 || user.pounds == 0) {
            return;
        }

        String gender = user.gender;
        double inches = user.inches;
        double pounds = user.pounds;
        double kilograms = (pounds * 0.45);
        double BMI = (inches * .025);
        BMI = BMI * BMI;
        BMI = kilograms / BMI;

        System.out.println("BMI: " + BMI);

        //int meanLifeDiffFromMean, double lifeExpectancyAtBirth, double lifeLeft, double age) {

        if(BMI <= 15 ) { //starvation
            timeLeft = Utilities.calcCorrectTimeLeft(-20, user.lifeExpFromBirth, timeLeft, user.age);
            return;
        }
       else if (gender.equals("Male")) {

            if (BMI >= 18.5 && BMI <= 24.9) { //normal
                timeLeft = Utilities.calcCorrectTimeLeft(5, user.lifeExpFromBirth, timeLeft, user.age);
            }

            else if (BMI >= 25 && BMI <= 29.9) { //overweight
                timeLeft = Utilities.calcCorrectTimeLeft(-2, user.lifeExpFromBirth, timeLeft, user.age);
            }

            else if (BMI >= 30 && BMI <= 39.9) { //obese
                timeLeft = Utilities.calcCorrectTimeLeft(-6, user.lifeExpFromBirth, timeLeft, user.age);
            }

            else if (BMI < 18.5) { //underweight
                timeLeft = Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);
            }

            else if (BMI > 40) { //severe obese
                timeLeft = Utilities.calcCorrectTimeLeft(-10, user.lifeExpFromBirth, timeLeft, user.age);

            }


        }

        else if (gender.equals("Female")) {
            if (BMI >= 18.5 && BMI <= 24.9) { //normal
                timeLeft = Utilities.calcCorrectTimeLeft(6, user.lifeExpFromBirth, timeLeft, user.age);
            }

            else if (BMI >= 25 && BMI <= 29.9) { //overweight
                timeLeft = Utilities.calcCorrectTimeLeft(-1, user.lifeExpFromBirth, timeLeft, user.age);
            }

            else if (BMI >= 30 && BMI <= 39.9) { //obese
                timeLeft = Utilities.calcCorrectTimeLeft(-5, user.lifeExpFromBirth, timeLeft, user.age);
            }

            else if (BMI < 18.5) { //underweight
                timeLeft = Utilities.calcCorrectTimeLeft(-2, user.lifeExpFromBirth, timeLeft, user.age);
            }

            else if (BMI > 40) { //severe obese
                timeLeft = Utilities.calcCorrectTimeLeft(-8, user.lifeExpFromBirth, timeLeft, user.age);

            }

        }

        else if (gender.equals("Other")) {
            if (BMI >= 18.5 && BMI <= 24.9) { //normal
                timeLeft = Utilities.calcCorrectTimeLeft(5, user.lifeExpFromBirth, timeLeft, user.age);
            }

            else if (BMI >= 25 && BMI <= 29.9) { //overweight
                timeLeft = Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);
            }

            else if (BMI >= 30 && BMI <= 39.9) { //obese
                timeLeft = Utilities.calcCorrectTimeLeft(-7, user.lifeExpFromBirth, timeLeft, user.age);
            }

            else if (BMI < 18.5) { //underweight
                timeLeft = Utilities.calcCorrectTimeLeft(-4, user.lifeExpFromBirth, timeLeft, user.age);
            }

            else if (BMI > 40) { //severe obese
                timeLeft = Utilities.calcCorrectTimeLeft(-11, user.lifeExpFromBirth, timeLeft, user.age);

            }

        }

    }





    public static void calcLifeFromMarriage() { //has to account for age
        String status = user.marriageStatus;
        String gender = user.gender;


        String[] marryStatus = {"Married", "Widowed", "Divorced", "Never Married", "Seperated"};


        if (gender.equals("Male")) { //stronger for men
            switch (status) {
                case "Married" : //all time best
                    //System.out.println("Married"); // + 5 years
                    timeLeft =  Utilities.calcCorrectTimeLeft(5, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                case "Widowed" : //worse than divorce but better than seperate
                    //System.out.println("Widowed"); //- 2 years
                    timeLeft =  Utilities.calcCorrectTimeLeft(-2, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                case "Divorced" ://not that bad
                    //System.out.println("Divorced"); //- 4 years
                    timeLeft =  Utilities.calcCorrectTimeLeft(-4, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                case "Never Married" : //worst for life
                    if(user.age <= 25) {

                        return;
                    }
                    //System.out.println("Never Married"); //- 6 years
                    timeLeft =  Utilities.calcCorrectTimeLeft(-6, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                case "Seperated" : //pretty bad
                    //System.out.println("Seperated"); //- 3
                    timeLeft =  Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                default:
                    break;
            }
        }

        else if (gender.equals("Female")) {
            switch (status) {
                case "Married" : //all time best
                    //System.out.println("Married"); // + 3 years
                    timeLeft =  Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                case "Widowed" : //worse than divorce but better than seperate
                    //System.out.println("Widowed"); //- 1 years
                    timeLeft =  Utilities.calcCorrectTimeLeft(-1, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                case "Divorced" ://not that bad
                    //System.out.println("Divorced"); //- 2 years
                    timeLeft =  Utilities.calcCorrectTimeLeft(-2, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                case "Never Married" : //worst for life
                    //System.out.println("Never Married"); //- 4 years
                    if(user.age <= 25) {

                        return;
                    }
                    timeLeft =  Utilities.calcCorrectTimeLeft(-4, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                case "Seperated" : //pretty bad
                    //System.out.println("Seperated"); //- 2
                    timeLeft =  Utilities.calcCorrectTimeLeft(-2, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                default:
                    break;
            }
        }
        else if (gender.equals("Other")) {
            switch (status) {
                case "Married" : //all time best
                    //System.out.println("Married"); // + 3 years
                    timeLeft =  Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                case "Widowed" : //worse than divorce but better than seperate
                    //System.out.println("Widowed"); //- 1 years
                    timeLeft =  Utilities.calcCorrectTimeLeft(-1,user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                case "Divorced" ://not that bad
                    //System.out.println("Divorced"); //- 2 years
                    timeLeft =  Utilities.calcCorrectTimeLeft(-1, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                case "Never Married" : //worst for life
                    if(user.age <= 25) {

                        return;
                    }
                    //System.out.println("Never Married"); //- 4 years
                    timeLeft =  Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                case "Seperated" : //pretty bad
                    //System.out.println("Seperated"); //- 2
                    timeLeft =  Utilities.calcCorrectTimeLeft(-1, user.lifeExpFromBirth, timeLeft, user.age);
                    break;
                default:
                    break;
            }
        }

    }

    public static void calcLifeFromEducation() {
        int education = user.yearsOfEd;
        String gender = user.gender;

        if (gender.equals("Female")) {

            if (education < 10){
                if(user.age <= 18) {

                    return;
                }
                timeLeft =  Utilities.calcCorrectTimeLeft(-8, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (education ==  12) {
                if(user.age <= 18) {

                    return;
                }
                timeLeft =  Utilities.calcCorrectTimeLeft(-6, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (education > 12 && education < 16){
                timeLeft =  Utilities.calcCorrectTimeLeft(-4, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (education == 16) {
                timeLeft =  Utilities.calcCorrectTimeLeft(5, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (education > 16){
                timeLeft =  Utilities.calcCorrectTimeLeft(7, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }


        else if (gender.equals("Male")) {
            if (education < 10){
                if(user.age <= 18) {

                    return;
                }
                timeLeft =  Utilities.calcCorrectTimeLeft(-10, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (education ==  12) {
                if(user.age <= 18) {

                    return;
                }
                timeLeft =  Utilities.calcCorrectTimeLeft(-8,user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (education > 12 && education < 16){
                timeLeft =  Utilities.calcCorrectTimeLeft(-5, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (education == 16) {
                timeLeft =  Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (education > 16){
                timeLeft =  Utilities.calcCorrectTimeLeft(5, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }

        else if (gender.equals("Other")) {
            if (education < 10){
                if(user.age <= 18) {

                    return;
                }
                timeLeft =  Utilities.calcCorrectTimeLeft(-11, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (education ==  12) {
                if(user.age <= 18) {

                    return;
                }
                timeLeft =  Utilities.calcCorrectTimeLeft(-9, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (education > 12 && education < 16){
                timeLeft =  Utilities.calcCorrectTimeLeft(-6, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (education == 16) {
                timeLeft =  Utilities.calcCorrectTimeLeft(2,user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (education > 16){
                timeLeft =  Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }

    }



    public static void calcLifeFromYearsOfWork() { //TODO: what do i do if age is zero here
        int yearsOfWork = user.yearsOfWork;
        String gender = user.gender;



        if (gender.equals("Male")) {
            if(yearsOfWork < 30) {
                timeLeft =  Utilities.calcCorrectTimeLeft(5, user.lifeExpFromBirth, timeLeft, user.age);
            }
            else if(yearsOfWork >30 && yearsOfWork <60) {
                timeLeft =  Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);
            }
            if(yearsOfWork > 60) {
                timeLeft =  Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }

        if(gender.equals("Female")) {
            if(yearsOfWork < 30) {
                timeLeft =  Utilities.calcCorrectTimeLeft(6, user.lifeExpFromBirth, timeLeft, user.age);
            }
            else if(yearsOfWork >30 && yearsOfWork <60) {
                timeLeft =  Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);
            }
            if(yearsOfWork > 50) {
                timeLeft =  Utilities.calcCorrectTimeLeft(-2, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }

        if(gender.equals("Other")) {
            if(yearsOfWork < 30) {
                timeLeft =  Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);
            }
            else if(yearsOfWork >30 && yearsOfWork <60) {
                timeLeft =  Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
            }
            if(yearsOfWork > 60) {
                timeLeft =  Utilities.calcCorrectTimeLeft(-4, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }

    }



    public static void calcLifeFromYearsPlannedToWork() {
        int yearsPlannedToWork = user.yearsPlannedToWork;
        int yearsWorked = user.yearsOfWork;
        String gender = user.gender;


        if (gender.equals("Male")) {
            if (yearsPlannedToWork < 30) {

                if (yearsWorked < 30) {
                    timeLeft = Utilities.calcCorrectTimeLeft(6, user.lifeExpFromBirth, timeLeft, user.age);
                } else if (yearsWorked > 30 && yearsWorked < 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);
                }
                if (yearsWorked > 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-1, user.lifeExpFromBirth, timeLeft, user.age);
                }
            } else if (yearsPlannedToWork > 30 && yearsPlannedToWork < 60) {

                if (yearsWorked < 30) {
                    timeLeft = Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);
                } else if (yearsWorked > 30 && yearsWorked < 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
                }
                if (yearsWorked > 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-2, user.lifeExpFromBirth, timeLeft, user.age);
                }
            }
            if (yearsPlannedToWork > 60) {

                if (yearsWorked < 30) {
                    timeLeft = Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);
                } else if (yearsWorked > 30 && yearsWorked < 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(0, user.lifeExpFromBirth, timeLeft, user.age);
                }
                if (yearsWorked > 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-5, user.lifeExpFromBirth, timeLeft, user.age);
                }
            }
        }

        if (gender.equals("Female")) {
            if (yearsPlannedToWork < 30) {

                if (yearsWorked < 30) {
                    timeLeft = Utilities.calcCorrectTimeLeft(7, user.lifeExpFromBirth, timeLeft, user.age);
                } else if (yearsWorked > 30 && yearsWorked < 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(5, user.lifeExpFromBirth, timeLeft, user.age);
                }
                if (yearsWorked > 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(0, user.lifeExpFromBirth, timeLeft, user.age);
                }
            } else if (yearsPlannedToWork > 30 && yearsPlannedToWork < 60) {

                if (yearsWorked < 30) {
                    timeLeft = Utilities.calcCorrectTimeLeft(5, user.lifeExpFromBirth, timeLeft, user.age);
                } else if (yearsWorked > 30 && yearsWorked < 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);
                }
                if (yearsWorked > 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);
                }
            }
            if (yearsPlannedToWork > 60) {

                if (yearsWorked < 30) {
                    timeLeft = Utilities.calcCorrectTimeLeft(5, user.lifeExpFromBirth, timeLeft, user.age);
                } else if (yearsWorked > 30 && yearsWorked < 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(1, user.lifeExpFromBirth, timeLeft, user.age);
                }
                if (yearsWorked > 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-4, user.lifeExpFromBirth, timeLeft, user.age);
                }
            }
        }

        if (gender.equals("Other")) {
            if (yearsPlannedToWork < 30) {

                if (yearsWorked < 30) {
                    timeLeft = Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);
                } else if (yearsWorked > 30 && yearsWorked < 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
                }
                if (yearsWorked > 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);
                }
            } else if (yearsPlannedToWork > 30 && yearsPlannedToWork < 60) {

                if (yearsWorked < 30) {
                    timeLeft = Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
                } else if (yearsWorked > 30 && yearsWorked < 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(0, user.lifeExpFromBirth, timeLeft, user.age);
                }
                if (yearsWorked > 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-4, user.lifeExpFromBirth, timeLeft, user.age);
                }
            }
            if (yearsPlannedToWork > 60) {

                if (yearsWorked < 30) {
                    timeLeft = Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);
                } else if (yearsWorked > 30 && yearsWorked < 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-1, user.lifeExpFromBirth, timeLeft, user.age);
                }
                if (yearsWorked > 60) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-6, user.lifeExpFromBirth, timeLeft, user.age);
                }
            }

        }



    }


    public static void calcLifeFromIncome() { //income has to be in thousands
        double income = user.annualIncome;
        String gender = user.gender;



        if (gender.equals("Male")) {

            if (income < 20){
                if(user.age <= 20) {
                    if(user.annualIncome >= 1) {
                        timeLeft =  Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);
                        return;
                    }
                    else {
                        return;
                    }
                }
                timeLeft =  Utilities.calcCorrectTimeLeft(-8, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (income > 20 && income < 50) {
                timeLeft =  Utilities.calcCorrectTimeLeft(-5, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (income >50 && income < 70) {
                timeLeft =  Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (income > 70) {
                timeLeft =  Utilities.calcCorrectTimeLeft(6, user.lifeExpFromBirth, timeLeft, user.age);
            }

        }

        if(gender.equals("Female")) {
            if(income < 20) {

                if(user.age <= 20) {
                    if(user.annualIncome >= 1) {
                        timeLeft =  Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);
                        return;
                    }
                    else {
                        return;
                    }
                }
                timeLeft =  Utilities.calcCorrectTimeLeft(-6, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (income > 20 && income < 50) {
                timeLeft =  Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (income >50 && income < 70) {
                timeLeft =  Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if(income > 70) {
                timeLeft =  Utilities.calcCorrectTimeLeft(8, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }



        if(gender.equals("Other")) {
            if(income < 20) {
                if(user.age <= 20) {
                    if(user.annualIncome >= 1) {
                        timeLeft =  Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
                        return;
                    }
                    else {
                        return;
                    }
                }

                timeLeft =  Utilities.calcCorrectTimeLeft(-7, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (income > 20 && income < 50) {
                timeLeft =  Utilities.calcCorrectTimeLeft(-4, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (income >50 && income < 70) {
                timeLeft =  Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if(income > 70) {
                timeLeft =  Utilities.calcCorrectTimeLeft(5, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }
    }

    public static void calcLifeFromExercise() {
        String excercise = user.exercise;
        String gender = user.gender;

        if (gender.equals("Male")) {
            if(excercise.equals("Little to none")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(-1, user.lifeExpFromBirth, timeLeft, user.age);

            }
            if (excercise.equals("Moderate amounts")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if( excercise.equals("Alot")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(+4, user.lifeExpFromBirth, timeLeft, user.age);

            }
        }
        if(gender.equals("Female")) {
            if(excercise.equals("Little to none")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(0, user.lifeExpFromBirth, timeLeft, user.age);

            }

            if (excercise.equals("Moderate amounts")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if( excercise.equals("Alot")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(5, user.lifeExpFromBirth, timeLeft, user.age);

            }
        }


        if(gender.equals("Other")) { //1 year less than either gender
            if(excercise.equals("Little to none")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(-2, user.lifeExpFromBirth, timeLeft, user.age);

            }

            if (excercise.equals("Moderate amounts")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(1, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if( excercise.equals("Alot")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);

            }

        }




    }

    public static void calcLifeFromHealth() {
        String health = user.health;
        String gender = user.gender;


        if (gender.equals("Male")) {
            if(health.equals("Very")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if(health.equals("Moderately")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if(health.equals("Unhealthy")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(-2, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }

        if (gender.equals("Female")) {
            if(health.equals("Very")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(5, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if(health.equals("Moderately")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(+4, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if(health.equals("Unhealthy")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(-1, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }

        if (gender.equals("Other")) {
            if(health.equals("Very")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if(health.equals("Moderately")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if(health.equals("Unhealthy")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }





    }




    public static void calcLifeFromOutlook() {
        String outlook = user.outlook;
        String gender = user.gender;

        if (gender.equals("Male")) {

            if(outlook.equals("Optimistic")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);
            }
            if (outlook.equals("Neutral")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(+1, user.lifeExpFromBirth, timeLeft, user.age);
            }
            if (outlook.equals("Pessimistic")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);

            }
        }

        if(gender.equals("Female")) {

            if(outlook.equals("Optimistic")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);
            }
            if (outlook.equals("Neutral")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
            }
            if (outlook.equals("Pessimistic")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(-2, user.lifeExpFromBirth, timeLeft, user.age);

            }
        }

        if(gender.equals("Other")) {

            if(outlook.equals("Optimistic")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
            }
            if (outlook.equals("Neutral")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(0, user.lifeExpFromBirth, timeLeft, user.age);
            }
            if (outlook.equals("Pessimistic")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(-4, user.lifeExpFromBirth, timeLeft, user.age);

            }
        }





    }

    public static void calcLifeFromAlcohol() {
        String alcohol = user.alcohol;
        String gender = user.gender;

        if (gender.equals("Male")) {
            if(alcohol.equals("Little to none")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(1, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if(alcohol.equals("Moderate amounts")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(3, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (alcohol.equals("Alot")) {

                timeLeft =  Utilities.calcCorrectTimeLeft(-4, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }

        if(gender.equals("Female")) {
            if(alcohol.equals("Little to none")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if(alcohol.equals("Moderate amounts")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(4, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (alcohol.equals("Alot")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }

        if(gender.equals("Other")) {
            if(alcohol.equals("Little to none")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(0, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if(alcohol.equals("Moderate amounts")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);
            }

            if (alcohol.equals("Alot")) {
                timeLeft =  Utilities.calcCorrectTimeLeft(-5, user.lifeExpFromBirth, timeLeft, user.age);
            }
        }




    }

    public  static void calcLifeFromSmoke() {
        double age = user.age;
        String smoke = user.smoke;
        String gender = user.gender;

        if (gender.equals("Male")) {
            if (age > 70) {
                if (smoke.equals("Yes Alot") || smoke.equals("Yes")) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-6, user.lifeExpFromBirth, timeLeft, user.age);
                }

                if (smoke.equals("No I quit")) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-2, user.lifeExpFromBirth, timeLeft, user.age);
                }

                if (smoke.equals("No")) {
                    timeLeft = Utilities.calcCorrectTimeLeft(1, user.lifeExpFromBirth, timeLeft, user.age);
                }
            }

            else if (age < 70) {
                if (smoke.equals("Yes Alot")) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-10, user.lifeExpFromBirth, timeLeft, user.age);
                }

                if (smoke.equals("Yes")) {
                    timeLeft = Utilities.calcCorrectTimeLeft(-6, user.lifeExpFromBirth, timeLeft, user.age);
                }

                if (smoke.equals("No I quit")) {
                    timeLeft = Utilities.calcCorrectTimeLeft(2, user.lifeExpFromBirth, timeLeft, user.age);

                }

                if (smoke.equals("No")) {

                    timeLeft = Utilities.calcCorrectTimeLeft(1, user.lifeExpFromBirth, timeLeft, user.age);

                }
            }
        }


        if(gender.equals("Female")) {
            if(age > 70) {
                if(smoke.equals("Yes Alot") || smoke.equals("Yes")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(-5, user.lifeExpFromBirth, timeLeft, user.age);
                }

                if(smoke.equals("No I quit")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(-1, user.lifeExpFromBirth, timeLeft, user.age);

                }

                if(smoke.equals("No")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(+2, user.lifeExpFromBirth, timeLeft, user.age);
                }
            }

            if(age< 70) {
                if(smoke.equals("Yes Alot")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(-8, user.lifeExpFromBirth, timeLeft, user.age);
                }

                if(smoke.equals("Yes")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(-4, user.lifeExpFromBirth, timeLeft, user.age);

                }
                if(smoke.equals("No I quit")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(+3, user.lifeExpFromBirth, timeLeft, user.age);

                }
                if(smoke.equals("No")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(-2, user.lifeExpFromBirth, timeLeft, user.age);
                }
            }
        }

        if(gender.equals("Other")) {

            if(age > 70) {
                if(smoke.equals("Yes Alot") || smoke.equals("Yes")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(-7, user.lifeExpFromBirth, timeLeft, user.age);
                }

                if(smoke.equals("No I quit")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(-3, user.lifeExpFromBirth, timeLeft, user.age);

                }

                if(smoke.equals("No")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(0, user.lifeExpFromBirth, timeLeft, user.age);
                }
            }

            if(age< 70) {
                if(smoke.equals("Yes Alot")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(-11, user.lifeExpFromBirth, timeLeft, user.age);
                }

                if(smoke.equals("Yes")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(-7, user.lifeExpFromBirth, timeLeft, user.age);

                }
                if(smoke.equals("No I quit")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(1, user.lifeExpFromBirth, timeLeft, user.age);

                }

                if(smoke.equals("No")) {
                    timeLeft =  Utilities.calcCorrectTimeLeft(0, user.lifeExpFromBirth, timeLeft, user.age);
                }
            }
        }
    }
}

