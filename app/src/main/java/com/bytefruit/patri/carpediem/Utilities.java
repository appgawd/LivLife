package com.bytefruit.patri.carpediem;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.gson.Gson;

public class Utilities {

    final static String[] months = {1 + "", 2 + "", 3 + "", 4 + "", 5 + "", 6 + "", 7 + "", 8 + "", 9 + "", 10 + "", 11 + "", 12 + ""};
    final static String[] days = {1 + "", 2 + "", 3 + "", 4 + "", 5 + "", 6 + "", 7 + "", 8 + "", 9 + "", 10 + "", 11 + "", 12 + "", 13 + "", 14 + "", 15 + "", 16 + "", 17 + "", 18 + "", 19 + "", 20 + "", 21 + "", 22 + "", 23 + "", 24 + "", 25 + "", 26 + "", 27 + "", 28 + "", 29 + "", 30 + "", 31 + ""};

    final static String[] hours = new String[12];
    final static String[] min = new String[60];
    final static String[] amPm = {"AM", "PM"};

    //test
    final static String[] fonts = {"heebo_thin", "heebo_thin", "bebas", "heebo_thin", "nova", "iceland", "heebo_thin", "nova", "iceland", "heebo_thin", "nova", "iceland"};
    final static String[] fontsC = {"heebo_light", "heebo_light", "ubuntu_thin", "nova", "nova", "roboto", "iceland", "iceland", "nova", "nova", "nova", "heebo_thin"};

    public static final int mainBg  = R.drawable.white;

    public static void datePickerHourMinAmPM(final NumberPicker datePickerMonths, final NumberPicker datePickerDays, final NumberPicker datePickerYears){
        //months is hours days is min years is amPm I was too lazy to rewrite
        Date date = Calendar.getInstance().getTime();

        for(int i = 0; i < hours.length; i++){
            hours[i] = (i+1) +"";
        }

        for(int i = 0; i < min.length; i++){
            min[i] = i +"";
        }

        datePickerMonths.setDisplayedValues(hours);
        datePickerDays.setDisplayedValues(min);
        datePickerYears.setDisplayedValues(amPm);

        //Populate NumberPicker values from String array values
        //Set the minimum value of NumberPicker
        datePickerMonths.setMinValue(0);
        datePickerDays.setMinValue(0);
        datePickerYears.setMinValue(0);

        //Specify the maximum value/number of NumberPicker
        datePickerMonths.setMaxValue(hours.length-1); //to array last value
        datePickerDays.setMaxValue(min.length-1); //to array last value
        datePickerYears.setMaxValue(amPm.length-1); //to array last value

        datePickerMonths.setWrapSelectorWheel(true);
        datePickerDays.setWrapSelectorWheel(true);
        datePickerYears.setWrapSelectorWheel(true);

        datePickerMonths.setValue(getHour24To12System(date) - 1);
        datePickerDays.setValue(date.getMinutes());

        if(!isAm(date)) {
            datePickerYears.setValue(1);
        }
        //Log.d("","sdf");
    }

    public static void datePicker(final NumberPicker datePickerMonths, final NumberPicker datePickerDays, final NumberPicker datePickerYears, final boolean isFutureDate){

        Date currentDate = Calendar.getInstance().getTime();

        final int thisYear = getYear(currentDate);
        final int thisMonth = getMonth(currentDate);
        final int thisDay = getDayOfMonth(currentDate);

        final String[] years = getYearsPossiblePicker(isFutureDate, thisYear);



        datePickerMonths.setDisplayedValues(months);
        datePickerDays.setDisplayedValues(days);
        datePickerYears.setDisplayedValues(years);

        //Populate NumberPicker values from String array values
        //Set the minimum value of NumberPicker
        datePickerMonths.setMinValue(0);
        datePickerDays.setMinValue(0);
        datePickerYears.setMinValue(0);

        //Specify the maximum value/number of NumberPicker
        datePickerMonths.setMaxValue(months.length-1); //to array last value
        datePickerDays.setMaxValue(days.length - 1 - (31 - getDaysInMonth(thisMonth, thisYear)));
        datePickerYears.setMaxValue(years.length-1); //to array last value

        datePickerMonths.setWrapSelectorWheel(true);
        datePickerDays.setWrapSelectorWheel(true);
        datePickerYears.setWrapSelectorWheel(true);

        datePickerMonths.setValue(thisMonth-1);
        datePickerDays.setValue(thisDay-1);
        //datePickerYears.setValue(-1);

        //Set a value change listener for NumberPicker
        datePickerYears.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                int yearSelected = Integer.parseInt(years[newVal]);
                int monthSelected = (datePickerMonths.getValue() + 1);

                //Display the newly selected value from picker
                //Log.d("dfs", years[newVal]);
                if (isLeap(yearSelected) && monthSelected == 2) {
                    datePickerDays.setMaxValue(days.length - 3);
                }
                else if(!isLeap(yearSelected) && monthSelected == 2){
                    datePickerDays.setMaxValue(days.length - 4);
                }
            }
        });

        //Set a value change listener for NumberPicker
        datePickerMonths.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                int yearSelected = Integer.parseInt(years[datePickerYears.getValue()]);
                int monthSelected = newVal + 1;

                //Display the newly selected value from picker
                //Log.d("dfs", months[newVal]);

                datePickerDays.setMaxValue(days.length - 1 - (31 - getDaysInMonth(monthSelected, yearSelected)));
            }
        });

        //Set a value change listener for NumberPicker
        datePickerDays.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected value from picker
                //Log.d("dfs", days[newVal]);
            }
        });
    }

    public static String[] getYearsPossiblePicker(boolean isFutureDate, int thisYear){

        final int yearsAllowed;

        if (isFutureDate) {
            yearsAllowed = 140;
        } else {
            yearsAllowed = 117;
        }

        final String[] years = new String[yearsAllowed + 1];
        for (int i = 0; i <= yearsAllowed; i++) {
            if (isFutureDate) {
                years[i] = (i + thisYear) + "";
            } else {
                years[i] = (thisYear - i) + "";
            }
        }
        return  years;
    }

    public static boolean isLeap(int year){
        if((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))){
            return true;
        }else{
            return false;
        }
    }

    public static int getDaysInMonth(int month, int year) {
        int days = 0;
        switch (month) {
            case 1:
                days += 31;
                break;

            case 2:
                //Log.d("sdf", ""+year);
                if (isLeap(year)) {
                    days += 29;
                }
                else {
                    days += 28;
                }
                break;

            case 3:
                days += 31;
                break;

            case 4:
                days += 30;
                break;

            case 5:
                days += 31;
                break;

            case 6:
                days += 30;
                break;

            case 7:
                days += 31;
                break;

            case 8:
                days += 31;
                break;

            case 9:
                days += 30;
                break;

            case 10:
                days += 31;
                break;

            case 11:
                days += 30;
                break;

            case 12:
                days += 31;
                break;
        }

        return days;
    }

    public static boolean isAm(Date date){
        if(date.getHours() >= 12){
            return false;
        }
        return true;
    }

    public static int getHour24To12System(Date date){
        if(date.getHours() > 12){
            return date.getHours() - 12;
        }
        return date.getHours();
    }

    public static int getYear(Date date){
        return date.getYear() + 1900;
    }

    public static int getMonth(Date date){
        return date.getMonth() + 1;
    }

    public static int getDayOfMonth(Date date){
        return date.getDate();//get date returns day of month
    }



    public static double millisToYears(long millis, boolean isLeap){
        if(isLeap){
            return millis / (86400000.0 * 366.0);
        }else{
            return millis / (86400000.0 * 365.0);
        }
    }

    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    public static double getAgeYearsDecimal(Date birthday){
        Date currentDate = java.util.Calendar.getInstance().getTime();
        int numFullYearsSinceBirthday =  getYear(currentDate) - Utilities.getYear(birthday) - 1;
        Date firstYearEnds = new Date(birthday.getYear() + 1, 0, 1);
        long timeLeftFirstYearMillis = firstYearEnds.getTime() - birthday.getTime();
        Date lastYearBeginsDate = new Date(currentDate.getYear(), 0, 1);
        long distanceIntoLastYearMillis = currentDate.getTime() - lastYearBeginsDate.getTime();
        double timeLeftFirstYearYears = millisToYears(timeLeftFirstYearMillis, isLeap(getYear(birthday)));
        double distanceIntoLastYearYears = millisToYears(distanceIntoLastYearMillis, isLeap(getYear(currentDate)));
        double ageYearsDecimal = numFullYearsSinceBirthday + timeLeftFirstYearYears + distanceIntoLastYearYears;
        //Log.d("Age", ageYearsDecimal+"");
        return ageYearsDecimal;
    }

    public static Date getDateObjectFromNormalDate(int year, int month, int day, int hour, int min, String amPm){
        if(amPm.equals("PM") && hour != 12){
            hour = hour + 12;
        }
        if(amPm.equals("AM") && hour == 12){
            hour = 0;
        }

        Date date = new Date(year - 1900, month - 1, day, hour, min);
        //Log.d("date", date+"");
        return date;
    }

    public static double calcCorrectTimeLeft(int meanLifeDiffFromMean, double lifeExpectancyAtBirth, double lifeLeft, double age) {
        if(meanLifeDiffFromMean > 0){
            double maxPossibleAge = 125.0;
            double maxPossibleLifeLeftAtBirth = (maxPossibleAge - lifeExpectancyAtBirth);
            double maxPossibleLifeLeft = (maxPossibleAge - (lifeLeft + age));
            double coefficient = meanLifeDiffFromMean / maxPossibleLifeLeftAtBirth; // this times life possible left then add that to the life left
            return (coefficient * maxPossibleLifeLeft) + lifeLeft;
        }
        else {
            double coefficient = ((lifeExpectancyAtBirth + meanLifeDiffFromMean) / lifeExpectancyAtBirth);
            return lifeLeft * coefficient;
        }
    }

    public static int NumberOfLeapYears(int startYear, int endYear)
    {
        int counter = 0;

        for (int year = startYear; year <= endYear; year++)
            counter += isLeap(year) ? 1 : 0;

        return counter;
    }

    // can make this algorithm more accurate later
    public static Date getDateYearsAhead(double years){
        Date currentDate = Calendar.getInstance().getTime();
        Date dateYearBegins = getDateObjectFromNormalDate(getYear(currentDate), 1, 1, 12, 0, "AM");
        long millisIntoYear = currentDate.getTime() - dateYearBegins.getTime();

        long millisInASec = 1000;
        long millisInAMin = millisInASec * 60;
        long millisInAHour = millisInAMin * 60;
        long millisInAday = millisInAHour * 24;
        long millisInLeapYear = millisInAday * 366; // should get theses two longs from runcoundown thread for better code style
        long millisInRegularYear = millisInAday * 365;


        double proportionIntoYear = (double) millisIntoYear / millisInRegularYear;

        if(isLeap(getYear(currentDate))) {
            proportionIntoYear = millisIntoYear / millisInLeapYear;
        }

        int numWholeYears = (int) years;
        int finalYear = getYear(currentDate) + numWholeYears;

        double yearsDecimalLeft = years - numWholeYears;

        if(proportionIntoYear + yearsDecimalLeft >= 1){
            finalYear = finalYear + 1;
            yearsDecimalLeft = (proportionIntoYear + yearsDecimalLeft) - 1;
        }else{
            yearsDecimalLeft = proportionIntoYear + yearsDecimalLeft;
        }

        int daysIntoFinalYear;
        //int hoursIntoFinalYear;
        //int minIntoFinalYear;

        int daysInFinalYear;
        if(isLeap(finalYear)){
            daysInFinalYear = 366;
        }else{
            daysInFinalYear = 365;
        }

        daysIntoFinalYear = (int) (yearsDecimalLeft * daysInFinalYear);

        //yearsDecimalLeft = yearsDecimalLeft - (daysIntoFinalYear / daysInFinalYear);

        //hoursIntoFinalYear = yearsDecimalLeft * hoursInFinalYear;
        //yearsDecimalLeft = yearsDecimalLeft - (hoursIntoFinalYear / hoursInFinalYear);

        //minIntoFinalYear = yearsDecimalLeft * minInFinalYear;

        int monthsIntoFinalYear = daysIntoFinalYear / 30;// can fix to make more accurate

        int daysIntoFinalMonth = daysIntoFinalYear - (monthsIntoFinalYear * 30);

        return getDateObjectFromNormalDate(finalYear, monthsIntoFinalYear + 1, daysIntoFinalMonth + 1, 12, 0, "AM");
    }

    public static int getNavigationBarHeight(Resources resources){
        int result = 0;
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getStatusBarHeight(Resources resources) {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static void setFontsBg(int bgImage, TextView[] tvs, Typeface[] fonts, Typeface[] fontsC, boolean isCountdown){
        if(!isCountdown) {
            for (TextView tv : tvs) {
                switch (bgImage) {
                    case R.drawable.white:
                        setFont(fonts[0], tv);//heebo thin
                        tv.setTextColor(Color.BLACK);
                        break;
                    case R.drawable.black:
                        setFont(fonts[1], tv); //nova
                        tv.setTextColor(Color.WHITE);
                        break;
                    case R.drawable.lightgrey:
                        setFont(fonts[2], tv);//heebo thin
                        tv.setTextColor(Color.WHITE);
                        break;
                    case R.drawable.darkgrey:
                        setFont(fonts[3], tv);//heebo_thin
                        tv.setTextColor(Color.WHITE);
                        break;
                    case R.drawable.yellow:
                        setFont(fonts[4], tv);//iceland
                        tv.setTextColor(Color.BLACK);
                        break;
                    case R.drawable.orange:
                        setFont(fonts[5], tv);
                        tv.setTextColor(Color.BLACK);
                        break;
                    case R.drawable.emerald:
                        setFont(fonts[6], tv);//heebo thin
                        tv.setTextColor(Color.WHITE);
                        break;
                    case R.drawable.blue:
                        setFont(fonts[7], tv); //nova
                        tv.setTextColor(Color.WHITE);
                        break;
                    case R.drawable.purple:
                        setFont(fonts[8], tv);//heebo thin
                        tv.setTextColor(Color.WHITE);
                        break;
                    case R.drawable.pink:
                        setFont(fonts[9], tv);//heebo_thin
                        tv.setTextColor(Color.WHITE);
                        break;
                    case R.drawable.red:
                        setFont(fonts[10], tv);//iceland
                        tv.setTextColor(Color.WHITE);
                        break;
                    case R.drawable.space:
                        setFont(fonts[11], tv);
                        tv.setTextColor(Color.WHITE);
                        break;
                }
            }
        }else{
            switch (bgImage) {
                case R.drawable.white:
                    setFont(fontsC[0], tvs[0]);//heebo thin
                    tvs[0].setTextColor(Color.BLACK);
                    break;
                case R.drawable.black:
                    setFont(fontsC[1], tvs[0]); //nova
                    tvs[0].setTextColor(Color.WHITE);
                    break;
                case R.drawable.lightgrey:
                    setFont(fontsC[2], tvs[0]);//heebo thin
                    tvs[0].setTextColor(Color.WHITE);
                    break;
                case R.drawable.darkgrey:
                    setFont(fontsC[3], tvs[0]);//heebo_thin
                    tvs[0].setTextColor(Color.WHITE);
                    break;
                case R.drawable.yellow:
                    setFont(fontsC[4], tvs[0]);//iceland
                    tvs[0].setTextColor(Color.WHITE);
                    break;
                case R.drawable.orange:
                    setFont(fontsC[5], tvs[0]);
                    tvs[0].setTextColor(Color.BLACK);
                    break;
                case R.drawable.emerald:
                    setFont(fontsC[6], tvs[0]);//heebo thin
                    tvs[0].setTextColor(Color.WHITE);
                    break;
                case R.drawable.blue:
                    setFont(fontsC[7], tvs[0]); //nova
                    tvs[0].setTextColor(Color.WHITE);
                    break;
                case R.drawable.purple:
                    setFont(fontsC[8], tvs[0]);//heebo thin
                    tvs[0].setTextColor(Color.WHITE);
                    break;
                case R.drawable.pink:
                    setFont(fontsC[9], tvs[0]);//heebo_thin
                    tvs[0].setTextColor(Color.WHITE);
                    break;
                case R.drawable.red:
                    setFont(fontsC[10], tvs[0]);//iceland
                    tvs[0].setTextColor(Color.WHITE);
                    break;
                case R.drawable.space:
                    setFont(fontsC[11], tvs[0]);
                    tvs[0].setTextColor(Color.WHITE);
                    break;
            }
        }
    }

    public static void setFont(Typeface font, TextView tv){
        Log.d("chode", font+" two"+tv);
        tv.setTypeface(font);
    }
}
