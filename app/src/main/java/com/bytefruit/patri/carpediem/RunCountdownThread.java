package com.bytefruit.patri.carpediem;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.util.Date;

import static com.bytefruit.patri.carpediem.Utilities.isLeap;

public class RunCountdownThread extends AsyncTask<String, String, String> {
    TextView countdownString;
    boolean runCountdown = true;
    long timeLastPublished = System.currentTimeMillis();
    double frameRate = 1000.0 / 30.0;
    Date deathDate;
    boolean isGregorian = false;
    Context context;

    RunCountdownThread(TextView countdownString, Date deathDate, boolean isGregorian, Context context){
        this.countdownString = countdownString;
        this.deathDate = deathDate;
        this.isGregorian = isGregorian;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        while(runCountdown && !isCancelled()) {
            if(System.currentTimeMillis() - timeLastPublished >= frameRate) {
                this.publishProgress("");
                timeLastPublished = System.currentTimeMillis();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
// need to deal with running to zero time left
        if(isGregorian){
            countdownString.setText(countdownGregorian(deathDate, context));
        }else {
            countdownString.setText(countdownYears(deathDate, context));
        }
    }

    public static String countdownYears(Date deathDate, Context context) {
        Date currentDate = java.util.Calendar.getInstance().getTime(); //probly set this to final
        long currentTimeMillis = currentDate.getTime();//current date should be gotten as close to this as possible
        int thisYear = Utilities.getYear(currentDate);
        try {
            if (currentTimeMillis < deathDate.getTime()) {
                Date yearEndsDate = new Date(currentDate.getYear() + 1, 0, 1);
                long yearEndsMillis = yearEndsDate.getTime();
                long millisTillYearEnds = yearEndsMillis - currentTimeMillis;
                double yearsTillYearEnds = Utilities.millisToYears(millisTillYearEnds, Utilities.isLeap(Utilities.getYear(deathDate)));
                int numFullYearsAfterThisYear = Utilities.getYear(deathDate) - thisYear - 1;
                Date lastYearBeginsDate = new Date(deathDate.getYear(), 0, 1);
                long millisIntoFinalYear = deathDate.getTime() - lastYearBeginsDate.getTime();
                double yearsIntoFinalYear = Utilities.millisToYears(millisIntoFinalYear, isLeap(Utilities.getYear(deathDate)));

                double timeLeftToLiveLifeYears = yearsTillYearEnds + numFullYearsAfterThisYear + yearsIntoFinalYear;

                String timeLeftToLiveLifeString = timeLeftToLiveLifeYears + "";
                String timeLefttoLiveLifeStringCutToSize;
                //Log.d("fixit", "leftString: "+ timeLeftToLiveLifeString);
                String zeros = "";
                if (timeLeftToLiveLifeString.contains("-")) {
                    //Log.d("chode", "" + timeLeftToLiveLifeString.substring((timeLeftToLiveLifeString.indexOf("-") + 1), timeLeftToLiveLifeString.length()));
                    int power = Integer.parseInt(timeLeftToLiveLifeString.substring((timeLeftToLiveLifeString.indexOf("-") + 1), timeLeftToLiveLifeString.length()));
                    for (int i = 0; i < power; i++) {
                        zeros = zeros + "0";
                    }

                    String nonScientific = timeLeftToLiveLifeString.substring(0, (timeLeftToLiveLifeString.indexOf("E") - 1));
                    StringBuilder stringBuilder = new StringBuilder(nonScientific);
                    stringBuilder.deleteCharAt(nonScientific.indexOf("."));
                    nonScientific = "." + zeros + stringBuilder.toString();
                    timeLeftToLiveLifeString = nonScientific;
                }
                String afterDecimal = timeLeftToLiveLifeString.substring((timeLeftToLiveLifeString.indexOf(".") + 1), timeLeftToLiveLifeString.length());

                while(afterDecimal.length() < 11){
                    afterDecimal = afterDecimal + "0";
                }

                String finalDecimal = "";
                int accuracyOfDecimal = 11;
                for (int i = 0; i < accuracyOfDecimal; i++) {
                    finalDecimal = finalDecimal + afterDecimal.charAt(i);
                }

                int numWholeYears = (int) timeLeftToLiveLifeYears;

                timeLefttoLiveLifeStringCutToSize = numWholeYears + "." + finalDecimal;


                //Log.d("time", ""+ timeLeftToLiveLifeYears);
                return timeLefttoLiveLifeStringCutToSize + " Years";////////////////////////////make this always the same length string

                //Log.d("", "years " + millisTillYearEnds);
                //Log.d("", "yearsnumfull " + numFullYearsAfterThisYear);
                //Log.d("", "intofinal" + yearsIntoFinalYear);

                //Log.d("chode", ""+ (yearsTillYearEnds + numFullYearsAfterThisYear + yearsIntoFinalYear));
            } else {
                return context.getString(R.string.zero_time_years);
                //Log.d("chizer", "noo1");
            }
        } catch (Exception e) {
            return context.getString(R.string.zero_time_years);
            //Log.d("chizer", "noo2");
        }
    }

    public static String countdownGregorian(Date deathDate, Context context){
        try {
            Date currentDate = java.util.Calendar.getInstance().getTime();

            long timeLeftMillis = deathDate.getTime() - currentDate.getTime();

            if (timeLeftMillis > 0) {
                System.out.println(timeLeftMillis);

                int useLeap = 2020;

                boolean leapUsed = false;

                int thisYear = Utilities.getYear(currentDate);
                int thisMonth = Utilities.getMonth(currentDate);
                int thisDay = Utilities.getDayOfMonth(currentDate);

                int deathYear = Utilities.getYear(deathDate);
                int deathMonth = Utilities.getMonth(deathDate);
                int deathDay = Utilities.getDayOfMonth(deathDate);

                boolean deathAfterFeb = Utilities.getDateObjectFromNormalDate(deathYear, 3, 1, 12, 0, "AM").getTime() <= deathDate.getTime();

                long millisInASec = 1000;
                long millisInAMin = millisInASec * 60;
                long millisInAHour = millisInAMin * 60;
                long millisInAday = millisInAHour * 24;
                long millisInLeapYear = millisInAday * 366;
                long millisInRegularYear = millisInAday * 365;
                int yr = thisYear;        //

                int yearsLeft = 0;
                long millisSubtractedLast = 0;
                long millisToSubtract = 0;

                while (timeLeftMillis >= 0) {
                    if (Utilities.isLeap(yr + 1) && deathAfterFeb) {
                        millisToSubtract = millisInLeapYear;

                        if (yr + 1 <= deathYear) {
                            leapUsed = true;
                        }
                    } else if (Utilities.isLeap(yr) && !deathAfterFeb && thisMonth < 3) {
                        millisToSubtract = millisInLeapYear;
                        leapUsed = true;
                    } else {
                        millisToSubtract = millisInRegularYear;
                        leapUsed = false;
                    }

                    yr++;
                    yearsLeft++;

                    timeLeftMillis = timeLeftMillis - millisToSubtract;
                    millisSubtractedLast = millisToSubtract;
                }

                timeLeftMillis = timeLeftMillis + millisSubtractedLast;
                yearsLeft--;

                boolean monthsSumToYear = ((deathYear - thisYear) == yearsLeft);

                if (!leapUsed) {
                    useLeap = 2019;
                }

                int monthsLeft = 0;
                int monthBeginDays = 0;

                if (monthsSumToYear) {

                    int month = 0;
                    millisToSubtract = 0;
                    millisSubtractedLast = 0;
                    while (timeLeftMillis >= 0) {

                        int monthDays = Utilities.getDaysInMonth(thisMonth + month, useLeap);
                        millisToSubtract = monthDays * millisInAday;

                        if (thisMonth + month == 3) {
                            Date startDate = Utilities.getDateObjectFromNormalDate(deathYear, 3, thisDay, currentDate.getHours(), 0, "AM");
                            Date endDate = Utilities.getDateObjectFromNormalDate(deathYear, 4, thisDay, currentDate.getHours(), 0, "AM");
                            if (endDate.getTime() - startDate.getTime() < 31 * millisInAday) {
                                millisToSubtract = millisToSubtract - millisInAHour;
                            }
                        } else if (thisMonth + month == 2) {
                            Date startDate = Utilities.getDateObjectFromNormalDate(deathYear, 2, thisDay, currentDate.getHours(), 0, "AM");
                            Date endDate = Utilities.getDateObjectFromNormalDate(deathYear, 3, thisDay, currentDate.getHours(), 0, "AM");
                            if (endDate.getTime() - startDate.getTime() < Utilities.getDaysInMonth(2, deathYear) * millisInAday) {
                                millisToSubtract = millisToSubtract - millisInAHour;
                            }
                        } else if (thisMonth + month == 11) {
                            Date startDate = Utilities.getDateObjectFromNormalDate(deathYear, 11, thisDay, currentDate.getHours(), 0, "AM");
                            Date endDate = Utilities.getDateObjectFromNormalDate(deathYear, 12, thisDay, currentDate.getHours(), 0, "AM");
                            if (endDate.getTime() - startDate.getTime() > 30 * millisInAday) {
                                millisToSubtract = millisToSubtract + millisInAHour;
                            }
                        } else if (thisMonth + month == 10) {
                            Date startDate = Utilities.getDateObjectFromNormalDate(deathYear, 10, thisDay, currentDate.getHours(), 0, "AM");
                            Date endDate = Utilities.getDateObjectFromNormalDate(deathYear, 11, thisDay, currentDate.getHours(), 0, "AM");
                            if (endDate.getTime() - startDate.getTime() > 31 * millisInAday) {
                                millisToSubtract = millisToSubtract + millisInAHour;
                            }
                        }

                        month++;
                        monthsLeft++;

                        timeLeftMillis = timeLeftMillis - millisToSubtract;
                        millisSubtractedLast = millisToSubtract;
                    }

                    timeLeftMillis = timeLeftMillis + millisSubtractedLast;
                    monthsLeft--;

                    monthBeginDays = thisMonth + month - 1;
                } else {
                    int month = 0;
                    millisToSubtract = 0;
                    millisSubtractedLast = 0;
                    while (thisMonth + month <= 12) {

                        int monthDays = Utilities.getDaysInMonth(thisMonth + month, useLeap);
                        millisToSubtract = monthDays * millisInAday;

                        if (thisMonth + month == 3) {
                            Date startDate = Utilities.getDateObjectFromNormalDate(deathYear - 1, 3, thisDay, currentDate.getHours(), 0, "AM");
                            Date endDate = Utilities.getDateObjectFromNormalDate(deathYear - 1, 4, thisDay, currentDate.getHours(), 0, "AM");
                            if (endDate.getTime() - startDate.getTime() < 31 * millisInAday) {
                                millisToSubtract = millisToSubtract - millisInAHour;
                            }
                        } else if (thisMonth + month == 2) {
                            Date startDate = Utilities.getDateObjectFromNormalDate(deathYear - 1, 2, thisDay, currentDate.getHours(), 0, "AM");
                            Date endDate = Utilities.getDateObjectFromNormalDate(deathYear - 1, 3, thisDay, currentDate.getHours(), 0, "AM");
                            if (endDate.getTime() - startDate.getTime() < Utilities.getDaysInMonth(2, deathYear) * millisInAday) {
                                millisToSubtract = millisToSubtract - millisInAHour;
                            }
                        } else if (thisMonth + month == 11) {
                            Date startDate = Utilities.getDateObjectFromNormalDate(deathYear - 1, 11, thisDay, currentDate.getHours(), 0, "AM");
                            Date endDate = Utilities.getDateObjectFromNormalDate(deathYear - 1, 12, thisDay, currentDate.getHours(), 0, "AM");
                            if (endDate.getTime() - startDate.getTime() > 30 * millisInAday) {
                                millisToSubtract = millisToSubtract + millisInAHour;
                            }
                        } else if (thisMonth + month == 10) {
                            Date startDate = Utilities.getDateObjectFromNormalDate(deathYear - 1, 10, thisDay, currentDate.getHours(), 0, "AM");
                            Date endDate = Utilities.getDateObjectFromNormalDate(deathYear - 1, 11, thisDay, currentDate.getHours(), 0, "AM");
                            if (endDate.getTime() - startDate.getTime() > 31 * millisInAday) {
                                millisToSubtract = millisToSubtract + millisInAHour;
                            }
                        }

                        month++;
                        monthsLeft++;

                        timeLeftMillis = timeLeftMillis - millisToSubtract;
                        millisSubtractedLast = millisToSubtract;
                    }

                    if (timeLeftMillis < 0) {
                        timeLeftMillis = timeLeftMillis + millisSubtractedLast;
                        monthsLeft--;
                    }

                    int month2 = 0;
                    millisToSubtract = 0;
                    millisSubtractedLast = 0;
                    while (timeLeftMillis >= 0) {

                        int monthDays = Utilities.getDaysInMonth(1 + month2, useLeap);
                        millisToSubtract = monthDays * millisInAday;

                        if (1 + month2 == 3) {
                            Date startDate = Utilities.getDateObjectFromNormalDate(deathYear, 3, thisDay, currentDate.getHours(), 0, "AM");
                            Date endDate = Utilities.getDateObjectFromNormalDate(deathYear, 4, thisDay, currentDate.getHours(), 0, "AM");//needs to be death month not this month same for other cases this is the bug mabey
                            if (endDate.getTime() - startDate.getTime() < 31 * millisInAday) {
                                millisToSubtract = millisToSubtract - millisInAHour;
                            }
                        } else if (1 + month2 == 2) {
                            Date startDate = Utilities.getDateObjectFromNormalDate(deathYear, 2, thisDay, currentDate.getHours(), 0, "AM");
                            Date endDate = Utilities.getDateObjectFromNormalDate(deathYear, 3, thisDay, currentDate.getHours(), 0, "AM");
                            if (endDate.getTime() - startDate.getTime() < Utilities.getDaysInMonth(2, deathYear) * millisInAday) {
                                millisToSubtract = millisToSubtract - millisInAHour;
                            }
                        } else if (1 + month2 == 10) {
                            Date startDate = Utilities.getDateObjectFromNormalDate(deathYear, 10, thisDay, currentDate.getHours(), 0, "AM");
                            Date endDate = Utilities.getDateObjectFromNormalDate(deathYear, 11, thisDay, currentDate.getHours(), 0, "AM");
                            if (endDate.getTime() - startDate.getTime() > 31 * millisInAday) {
                                millisToSubtract = millisToSubtract + millisInAHour;
                            }
                        }

                        month2++;
                        monthsLeft++;

                        timeLeftMillis = timeLeftMillis - millisToSubtract;
                        millisSubtractedLast = millisToSubtract;
                    }

                    timeLeftMillis = timeLeftMillis + millisSubtractedLast;
                    monthsLeft--;

                    monthBeginDays = 1 + month2 - 1;
                }

                if (monthBeginDays == 2 || monthBeginDays == 3) {
                    Date startDate = Utilities.getDateObjectFromNormalDate(deathYear, monthBeginDays, thisDay, currentDate.getHours(), 0, "AM");
                    Date endDate = Utilities.getDateObjectFromNormalDate(deathYear, deathMonth, deathDay, currentDate.getHours(), 0, "AM");


                    if ((endDate.getTime() - startDate.getTime()) % millisInAday != 0) {
                        timeLeftMillis = timeLeftMillis + millisInAHour;
                    }
                } else if (monthBeginDays == 11 || monthBeginDays == 10) {
                    Date startDate = Utilities.getDateObjectFromNormalDate(deathYear, monthBeginDays, thisDay, currentDate.getHours(), 0, "AM");
                    Date endDate = Utilities.getDateObjectFromNormalDate(deathYear, deathMonth, deathDay, currentDate.getHours(), 0, "AM");
                    if ((endDate.getTime() - startDate.getTime()) % millisInAday != 0) {
                        timeLeftMillis = timeLeftMillis - millisInAHour;
                    }
                }

                long daysLeft = timeLeftMillis / millisInAday;
                timeLeftMillis = timeLeftMillis - (daysLeft * millisInAday);

                long hoursLeft = timeLeftMillis / millisInAHour;
                timeLeftMillis = timeLeftMillis - (hoursLeft * millisInAHour);

                long minLeft = timeLeftMillis / millisInAMin;
                timeLeftMillis = timeLeftMillis - (minLeft * millisInAMin);

                long secLeft = timeLeftMillis / millisInASec;
                timeLeftMillis = timeLeftMillis - (secLeft * millisInASec);

                //countdownString.setText(yearsLeft + "y " + forceNumLength(monthsLeft, 2) + "m " + forceNumLength(daysLeft, 2) + "d \n" + forceNumLength(hoursLeft, 2) + "h " + forceNumLength(minLeft, 2) + "m " + forceNumLength(secLeft, 2) + "s " + forceNumLength(timeLeftMillis, 3) + " ms");
                return yearsLeft + "y " + forceNumLength(monthsLeft, 2) + "m " + forceNumLength(daysLeft, 2) + "d\n" + forceNumLength(hoursLeft, 2) + "h " + forceNumLength(minLeft, 2) + "m " + forceNumLength(secLeft, 2) + "s " + forceNumLength(timeLeftMillis, 3) + "ms";
                //Log.d("countdown Gregorian", yearsLeft + "years " + monthsLeft + "months " + daysLeft + "days " + hoursLeft + "h " + minLeft + "min " + secLeft + "s " + timeLeftMillis + " millis");
            } else {
                //countdownString.setText(R.string.zero_time_gregorian);
                //return Utilities.getZeroTimeGregorian();
                return context.getString(R.string.zero_time_gregorian);
            }
        }catch (Exception e) {
            //countdownString.setText(R.string.zero_time_gregorian);
            //return Utilities.getZeroTimeGregorian();
            return context.getString(R.string.zero_time_gregorian);
        }
    }

    static String forceNumLength(long numLong, int length){
        String num = numLong + "";
        while(num.length() < length){
            num = "0"+num;
        }
        return num;
    }
}