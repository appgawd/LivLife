package com.bytefruit.patri.carpediem;

public class UserDate {
    int years;
    int months;
    int days;
    int hours;
    int minutes;
    int seconds;

    public UserDate(int years, int months, int days, int hours, int minutes, int seconds) {
        this.years = years;
        this.months = months;
        this.days = days;
        this.minutes = minutes;
        this.seconds = seconds;
    }
    public UserDate() {

    }



    public void setYears(int years) {
        this.years = years;
    }
    public void setMonths(int months) {
        this.months = months;
    }
    public void setDays(int days) {
        this.days = days;
    }
    public void setHours(int hours) {
        this.hours = hours;
    }
    public void setMinutes(int minutes) {
        this.minutes= minutes;
    }
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }


    public int getYears() {
        return years;
    }
    public int getMonths() {
        return months;
    }
    public int getDays() {
        return days;
    }
    public int getHours() {
        return hours;
    }
    public int getMinutes() {
        return minutes;
    }
    public int getSeconds() {
        return seconds;
    }


    public String toString() {

        String result = "";
        result += "Years: " + years;
        result += "Months: " + months;
        result += "Days: " + days;
        result += "Hours: " + hours;
        result += "Minutes: " + minutes;
        result += "Seconds: " + seconds;

        return result;
    }
}