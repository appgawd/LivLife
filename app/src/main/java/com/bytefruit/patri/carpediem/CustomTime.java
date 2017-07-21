package com.bytefruit.patri.carpediem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class CustomTime extends AppCompatActivity implements  View.OnClickListener{

    private NumberPicker datePickerMonths;
    private NumberPicker datePickerDays;
    private NumberPicker datePickerYears;
    private NumberPicker datePickerHours;
    private NumberPicker datePickerMin;
    private NumberPicker datePickerAmPm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.activity_custom_time);

        startCustomTimePickers();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calculator_button:
                startActivity(new Intent(CustomTime.this, LifespanCalculator.class));
                overridePendingTransition(R.anim.fadeinback, R.anim.fadeoutback);

                break;
            case R.id.set_custom_time_as_countdown:
                setCustomTimeAsCountdown();
                break;
        }
    }

    private void startCustomTimePickers(){
        datePickerMonths = (NumberPicker) findViewById(R.id.custom_month);
        datePickerDays = (NumberPicker) findViewById(R.id.custom_day);
        datePickerYears = (NumberPicker) findViewById(R.id.custom_year);
        Utilities.datePicker(datePickerMonths, datePickerDays, datePickerYears, true);

        datePickerHours = (NumberPicker) findViewById(R.id.custom_hour);
        datePickerMin = (NumberPicker) findViewById(R.id.custom_min);
        datePickerAmPm = (NumberPicker) findViewById(R.id.custom_am_pm);
        Utilities.datePickerHourMinAmPM(datePickerHours, datePickerMin, datePickerAmPm);

       /* NumberPicker timePickerMonths = (NumberPicker) findViewById(R.id.custom_month);
        NumberPicker timePickerDays = (NumberPicker) findViewById(R.id.custom_day);
        NumberPicker timePickerYears = (NumberPicker) findViewById(R.id.custom_year);
        Utilities.datePicker(datePickerMonths, datePickerDays, datePickerYears, true);*/
    }




    public void setCustomTimeAsCountdown(){
        boolean isTimeValid = true;
        Date date = Calendar.getInstance().getTime();
        int thisYear = Utilities.getYear(date);
        int thisMonth = Utilities.getMonth(date);
        int thisDay = Utilities.getDayOfMonth(date);
        int thisHour = Utilities.getHour24To12System(date);
        int thisMin = date.getMinutes();

        String[] years = Utilities.getYearsPossiblePicker(true, thisYear);
        String[] months = Utilities.months;
        String[] days = Utilities.days;
        String[] hours = Utilities.hours;
        String[] min = Utilities.min;
        String[] amPm = Utilities.amPm;

        int yearSelected = Integer.parseInt(years[datePickerYears.getValue()]);
        int monthSelected = Integer.parseInt(months[datePickerMonths.getValue()]);
        int daySelected = Integer.parseInt(days[datePickerDays.getValue()]);
        int hourSelected = Integer.parseInt(hours[datePickerHours.getValue()]);
        int minSelected = Integer.parseInt(min[datePickerMin.getValue()]);
        String amPmSelected = amPm[datePickerAmPm.getValue()];

        if(yearSelected == thisYear){
            if(monthSelected == thisMonth){
                if(daySelected == thisDay){
                    if(!Utilities.isAm(date) && amPmSelected.equals("AM")){
                        isTimeValid = false;
                    }else {
                        if(!Utilities.isAm(date)) {
                            if (hourSelected == thisHour) {
                                 if (minSelected <= thisMin) {
                                    isTimeValid = false;
                                }
                            } else if (hourSelected < thisHour) {
                                isTimeValid = false;
                            }
                        }
                        if(Utilities.isAm(date)) {
                            if(amPmSelected.equals("AM")){
                                if (hourSelected == thisHour) {
                                    if (minSelected <= thisMin) {
                                        isTimeValid = false;
                                    }
                                } else if (hourSelected < thisHour) {
                                    isTimeValid = false;
                                }
                            }
                        }
                    }
                }
                else if(daySelected < thisDay){
                    isTimeValid = false;
                }
            }
            else if(monthSelected < thisMonth){
                isTimeValid = false;
            }
        }

        if(isTimeValid){
            //Date deathDate = Utilities.getDateObjectFromNormalDate(yearSelected, monthSelected, daySelected, hourSelected, minSelected, amPmSelected);
            //Date deathDate = new Date(2019-1900,1,1);
            Date deathDate = Utilities.getDateObjectFromNormalDate(yearSelected, monthSelected, daySelected, hourSelected, minSelected, amPmSelected);

            Utilities.saveObjectToSharedPreference(this, "deathDate", "deathDateKey", deathDate);

            startActivity(new Intent(CustomTime.this, MainActivity.class));
            //call ricks algo start the timer
        }else{
            Toast.makeText(this, "Must input a future date and time.", Toast.LENGTH_SHORT).show();
        }

        //do shit and same as in the life set countdown
    }

    public void onStop(){
        super.onStop();
        finish();
    }
}

