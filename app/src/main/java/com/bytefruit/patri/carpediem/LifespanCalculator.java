package com.bytefruit.patri.carpediem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class LifespanCalculator extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener{

    private ArrayList<LifeExpObject> CountriesAndLifeSpanArray = new  ArrayList<LifeExpObject>();
    public static double LifeLeftToLive;

    private NumberPicker countryPicker;
    private int numCheckBoxSets = 8;

    int[][] checkBoxSets = new int[numCheckBoxSets][];
    int[] gender = new int[3];
    int[] race = new int[5];
    int[] maritalStatus = new int[5];
    int[] exercise = new int[3];
    int[] health = new int[3];
    int[] alcohol = new int[3];
    int[] smoke = new int[4];
    int[] outlook = new int[3];

    int genderPos = 0;
    int racePos = 1;
    int maritalStatusPos = 2;
    int exercisePos = 3;
    int healthPos = 4;
    int alcoholPos = 5;
    int smokePos = 6;
    int outlookPos = 7;


    String[] isAllChecksComplete = new String[numCheckBoxSets];
    CheckBox metric;

    private SeekBar height;
    private SeekBar weight;
    private SeekBar education;
    private SeekBar yearsWorked;
    private SeekBar yearsPlannedToWork;
    private SeekBar annualIncome;

    private NumberPicker datePickerMonths;
    private NumberPicker datePickerDays;
    private NumberPicker datePickerYears;

    private boolean isMetric = false;

    final private String[] countries = {"Afghanistan", "Albania", "Algeria", "American Samoa",
            "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda",
            "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
            "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize",
            "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana",
            "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria",
            "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
            "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands",
            "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands",
            "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic",
            "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt",
            "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)",
            "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia",
            "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece",
            "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
            "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary",
            "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy",
            "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of",
            "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho",
            "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau",
            "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives",
            "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico",
            "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat",
            "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
            "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island",
            "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea",
            "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar",
            "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
            "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia",
            "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia",
            "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands",
            "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname",
            "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland",
            "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan",
            "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago",
            "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
            "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands",
            "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)",
            "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen",
            "Yugoslavia", "Zambia", "Zimbabwe"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lifespan_calculator);

        startCountryPicker();

        startCheckBoxes();

        startSeekBars();

        startDatePicker();
    }

    private void startDatePicker(){
        datePickerMonths = (NumberPicker) findViewById(R.id.birthday_month);
        datePickerDays = (NumberPicker) findViewById(R.id.birthday_day);
        datePickerYears = (NumberPicker) findViewById(R.id.birthday_year);
        Utilities.datePicker(datePickerMonths, datePickerDays, datePickerYears, false);
    }

    private void startCountryPicker(){
        countryPicker = (NumberPicker) findViewById(R.id.country_chooser);

        countryPicker.setDisplayedValues(countries);

        //Populate NumberPicker values from String array values
        //Set the minimum value of NumberPicker
        countryPicker.setMinValue(0); //from array first value
        //Specify the maximum value/number of NumberPicker
        countryPicker.setMaxValue(countries.length-1); //to array last value

        countryPicker.setWrapSelectorWheel(true);

        countryPicker.setValue(Arrays.binarySearch(countries, "United States"));

        //Set a value change listener for NumberPicker
        countryPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected value from picker
                //Log.d("dfs", countries[newVal]);
            }
        });
    }

    private void startCheckBoxes(){
        gender[0] = R.id.male;
        gender[1] = R.id.female;
        gender[2] = R.id.gender_other;

        race[0] = R.id.race_black;
        race[1] = R.id.race_white;
        race[2] = R.id.race_hispanic;
        race[3] = R.id.race_asian;
        race[4] = R.id.race_other;

        maritalStatus[0] = R.id.married_married;
        maritalStatus[1] = R.id.married_widowed;
        maritalStatus[2] = R.id.married_divorced;
        maritalStatus[3] = R.id.married_never;
        maritalStatus[4] = R.id.married_separated;

        exercise[0] = R.id.exercise_little;
        exercise[1] = R.id.exercise_moderate;
        exercise[2] = R.id.exercise_alot;


        health[0] = R.id.health_very;
        health[1] = R.id.health_moderately;
        health[2] = R.id.health_unhealthy;

        outlook[0] = R.id.outlook_optimistic;
        outlook[1] = R.id.outlook_neutral;
        outlook[2] = R.id.outlook_pessimistic;

        alcohol[0] = R.id.alcohol_little;
        alcohol[1] = R.id.alcohol_moderate;
        alcohol[2] = R.id.alcohol_alot;

        smoke[0] = R.id.smoke_yes_alot;
        smoke[1] = R.id.smoke_yes;
        smoke[2] = R.id.smoke_quit;
        smoke[3] = R.id.smoke_no;

        checkBoxSets[genderPos] = gender;
        checkBoxSets[racePos] = race;
        checkBoxSets[maritalStatusPos] = maritalStatus;
        checkBoxSets[exercisePos] = exercise;
        checkBoxSets[healthPos] = health;
        checkBoxSets[alcoholPos] = alcohol;
        checkBoxSets[smokePos] = smoke;
        checkBoxSets[outlookPos] = outlook;

        metric = (CheckBox) findViewById(R.id.metric_or_not);
        metric.setChecked(false);
    }

    public void onCheckBoxClicked(View buttonView)
    {
        CheckBox checkBox=(CheckBox) buttonView;
        int checkBoxSet = -1;
        int checkBoxSelected = -1;

        if(checkBox.isChecked())
        {
            for(int i = 0; i < checkBoxSets.length; i++) {
                for(int j = 0; j < checkBoxSets[i].length; j++) {
                    if (checkBoxSets[i][j] == checkBox.getId()) {
                        checkBoxSet = i;
                        checkBoxSelected = j;
                    }
                }
            }

            isAllChecksComplete[checkBoxSet] = ((CheckBox) findViewById(checkBoxSets[checkBoxSet][checkBoxSelected])).getText() + "";
        }

        if(checkBoxSet != -1){
            for(int i = 0; i < checkBoxSets[checkBoxSet].length; i++){
                if(i != checkBoxSelected) {
                    ((CheckBox) findViewById((checkBoxSets[checkBoxSet][i]))).setChecked(false);
                }
            }
        }else{
            checkBox.setChecked(true);
        }
    }

    public void onMetricClicked(View buttonView){
        int progress = height.getProgress();
        int progressWeight = weight.getProgress();

        CheckBox checkBox = (CheckBox) buttonView;
        if(checkBox.isChecked()){
            isMetric = true;
            TextView textView=(TextView)findViewById(R.id.weight_pound);
            textView.setText(getWeight(progressWeight, false));

            TextView textViewHeight=(TextView)findViewById(R.id.height_feet_inches);
            textViewHeight.setText(getHeight(progress, false));
        }else{
            isMetric = false;
            TextView textView=(TextView)findViewById(R.id.weight_pound);
            textView.setText(getWeight(progressWeight, false));

            TextView textViewHeight=(TextView)findViewById(R.id.height_feet_inches);
            textViewHeight.setText(getHeight(progress, false));
        }
    }

    public void startSeekBars(){
        height = (SeekBar)findViewById(R.id.seek_bar_height);
        weight = (SeekBar)findViewById(R.id.seek_bar_weight);
        education = (SeekBar)findViewById(R.id.seek_education);
        yearsWorked = (SeekBar)findViewById(R.id.seek_years_already_worked);
        yearsPlannedToWork = (SeekBar)findViewById(R.id.seek_years_plan_to_work);
        annualIncome = (SeekBar)findViewById(R.id.seek_bar_income);

        height.setOnSeekBarChangeListener(this);
        TextView textViewHeight=(TextView)findViewById(R.id.height_feet_inches);
        textViewHeight.setText("0' 8 ft\"");
        weight.setOnSeekBarChangeListener(this);
        TextView textViewWeight=(TextView)findViewById(R.id.weight_pound);
        textViewWeight.setText("1 lb");
        education.setOnSeekBarChangeListener(this);
        yearsWorked.setOnSeekBarChangeListener(this);
        yearsPlannedToWork.setOnSeekBarChangeListener(this);
        annualIncome.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
        // TODO Auto-generated method stub
        String sliderString = "";
        int correspondingTextView = -1;
        switch (getResources().getResourceEntryName(seekBar.getId())) {
            case "seek_bar_height":
                sliderString = getHeight(progress, false);
                correspondingTextView = R.id.height_feet_inches;
                break;
            case "seek_bar_weight":
                sliderString = getWeight(progress, false);
                correspondingTextView = R.id.weight_pound;
                break;
            case "seek_education":
                sliderString = progress +" Years";
                correspondingTextView = R.id.education_years;
                break;
            case "seek_years_already_worked":
                sliderString = progress +" Years";
                correspondingTextView = R.id.work_years;
                break;
            case "seek_years_plan_to_work":
                sliderString = progress +" Years";
                correspondingTextView = R.id.work_years_future;
                break;
            case "seek_bar_income":
                sliderString = "$"+progress +"K";
                correspondingTextView = R.id.income_dollars_k;
                break;
        }

        //Log.d("correspondingTextView", correspondingTextView+"");
        TextView textView=(TextView)findViewById(correspondingTextView);
        textView.setText(sliderString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_button:
                startActivity(new Intent(LifespanCalculator.this, CustomTime.class));
                break;
            case R.id.set_lifespan_estimate_as_time_for_countdown:
                setLifespanEstimateAsTimeForCountDown();
                break;
        }
    }

    public void setLifespanEstimateAsTimeForCountDown(){

        boolean isAllFieldsComplete = true;

        for(int i = 0; i < isAllChecksComplete.length - 1; i++){
            if(isAllChecksComplete[i] == null){
                i = isAllChecksComplete.length;
                isAllFieldsComplete = false;
                Toast.makeText(this, "Oops! All fields must be complete.", Toast.LENGTH_SHORT).show();
            }
        }

        if(isAllFieldsComplete) {
            boolean isBirthdayValid = true;
            Toast notValidBday = Toast.makeText(this, "Oops! Date of birth must be set to a past date.", Toast.LENGTH_LONG);

            //check if valid bday
            Date currentDate = Calendar.getInstance().getTime();
            int thisYear = Utilities.getYear(currentDate);
            int thisMonth = Utilities.getMonth(currentDate);
            int thisDay = Utilities.getDayOfMonth(currentDate);

            String[] years = Utilities.getYearsPossiblePicker(false, thisYear);
            String[] months = Utilities.months;
            String[] days = Utilities.days;

            int yearSelected = Integer.parseInt(years[datePickerYears.getValue()]);
            int monthSelected = Integer.parseInt(months[datePickerMonths.getValue()]);
            int daySelected = Integer.parseInt(days[datePickerDays.getValue()]);

            if(yearSelected == thisYear && monthSelected > thisMonth){
                isBirthdayValid = false;
                notValidBday.show();
            }else if(yearSelected == thisYear && monthSelected == thisMonth && daySelected > thisDay){
                isBirthdayValid = false;
                notValidBday.show();
            }

            boolean yeasPlusEducationValid = true;
            Date birthday = Utilities.getDateObjectFromNormalDate(yearSelected, monthSelected, daySelected, 0, 0, "AM");
            double ageYears = Utilities.getAgeYearsDecimal(birthday);

            if(yearsWorked.getProgress() + education.getProgress() > (int) ageYears){
                Toast.makeText(this, "Oops! Total years worked + Total years of education must be less than your age.", Toast.LENGTH_LONG).show();
                yeasPlusEducationValid = false;
            }

            if(isBirthdayValid && yeasPlusEducationValid) {

                double weightPounds = Double.parseDouble(getWeight(weight.getProgress(), true));
                double heightInches = Double.parseDouble(getHeight(height.getProgress(),true));
                if(isMetric) {
                    double poundsInKg = 2.20462;
                    double inchesInMeter = 39.3701;
                    weightPounds = weightPounds * poundsInKg;
                    heightInches = heightInches * inchesInMeter;
                }

                double inches = heightInches;
                double pounds = weightPounds;

                double kg = pounds * .453592;
                double meters = inches * .0254;

                double BMI = kg / (meters * meters);

                if(BMI > 11.5) {
                    UserDate bday = new UserDate(yearSelected, monthSelected, daySelected, 0, 0, 0);
                    int yearsOfEd = education.getProgress();
                    int yearsOfWork = yearsWorked.getProgress();
                    int yearsPlanned2Work = yearsPlannedToWork.getProgress();
                    double Income = annualIncome.getProgress();


                    String exercise = isAllChecksComplete[exercisePos];
                    String health = isAllChecksComplete[healthPos];
                    String outlook = isAllChecksComplete[outlookPos];
                    String alcohol = isAllChecksComplete[alcoholPos];

                    String country = countries[countryPicker.getValue()];
                    CheckBox countryCheck = (CheckBox) findViewById(R.id.other_country);
                    if(countryCheck.isChecked()){
                        country = "Other";
                    }

                    String marriageStatus = isAllChecksComplete[maritalStatusPos];
                    String gender = isAllChecksComplete[genderPos];
                    String race = isAllChecksComplete[racePos];
                    String smoke = isAllChecksComplete[smokePos];


                    UserProfile publicUser = returnLifeSpan(bday, inches, pounds, gender, race, country, marriageStatus, yearsOfEd, yearsOfWork, yearsPlanned2Work,
                            Income, exercise, health, outlook, alcohol, smoke);


                    LifeLeftToLive = publicUser.timeLeft; //life left

                    //boolean lifespan should be in another method not necessary for calc death

                    Date deathDate = Utilities.getDateYearsAhead(LifeLeftToLive);
                    Utilities.saveObjectToSharedPreference(this, "deathDate", "deathDateKey", deathDate);

                    startActivity(new Intent(this, MainActivity.class));
                }
                else{
                    Toast.makeText(this, "Oops! Height and weight appear to be unrealistic.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public String getHeight(int progress, boolean returnPlainDouble){
        if(metric.isChecked()){
            String metersDecimal = (progress / 4)+"";
            if(metersDecimal.length() > 1) {
                metersDecimal = metersDecimal.substring(1);
            }
            double height = Double.parseDouble((progress / 40) + "." + metersDecimal) + .2;
            int heightTimes10 = ((int) (height * 10));
            height = ((double) heightTimes10 / 10.0);
            if(returnPlainDouble){
                return  height + "";
            }else {
                return  height + " m";
            }
        }else{
            //code is wierd to add 8 inches to height
            int feet = progress / 12;
            int inches = feet * 12 + (progress - (feet * 12));
            inches = inches + 8;
            if(returnPlainDouble){
                return inches + "";
            }else {
                feet = inches / 12;

                //prevents spazzing when slider is moved
                String noSpazz = (inches - (feet * 12)) + "";
                if(noSpazz.length() == 1) {
                    noSpazz = "  " + noSpazz;
                }

                return feet + "' " + noSpazz + "'' ft";
            }
        }
    }

    public String getWeight(int progress, boolean returnPlainDouble){
        if(isMetric){
            if(returnPlainDouble){
                return ((progress * 2.5) + .45) + "";
            }else {
                return ((progress * 2.5) + .45) + " kg";
            }
        }else {
            if(returnPlainDouble){
                return ((progress * 5) + 1) + "";
            }else {
                return ((progress * 5) + 1) + " lb";
            }
        }
    }

    public void doNothing(View buttonView){
        // this can be called by a checkbox so that a clicking noise is made
    }

    public UserProfile returnLifeSpan (UserDate bday,
                                       double inches,
                                       double pounds,
                                       String gender,
                                       String race,
                                       String country,
                                       String marriageStatus,
                                       int yearsOfEd,
                                       int yearsOfWork,
                                       int  yearsPlannedToWork,
                                       double annualIncome,
                                       String exercise,
                                       String health,
                                       String outlook,
                                       String alcohol,
                                       String smoke) {

        LifeExpectFromBirthAndCountries.setArray(); //sets country array in associated class
        setArray(); //sets local array


        Date bdayDate = Utilities.getDateObjectFromNormalDate(bday.getYears(), bday.getDays(), bday.getHours(), 0, 0, "AM");
        double age = Utilities.getAgeYearsDecimal(bdayDate); //method call to calc age
        double lifeExpectancyFromBirth = returnUserLifeExpectFirstTime(country, gender);



        UserProfile finalUser = new UserProfile(bday, inches, age, pounds, yearsOfEd, yearsOfWork, yearsPlannedToWork,
                annualIncome, lifeExpectancyFromBirth, exercise, health, outlook, alcohol, smoke, country, marriageStatus,
                gender, race);



        return CalcLifeSpan.calculate(finalUser,CountriesAndLifeSpanArray);


    }

    public void setArray() { //sets local array
        for(int i = 0;i<LifeExpectFromBirthAndCountries.countries_lifeExp.size();i++) { //sets arraylist equal arraylist
            CountriesAndLifeSpanArray.add(i, LifeExpectFromBirthAndCountries.countries_lifeExp.get(i));
        }
    }

    public double returnUserLifeExpectFirstTime (String country, String gender) {
        double result = 0.0;



        int countryIndex = 0; //sets index to the country of the user, in the country array
        for (int i = 0; i < CountriesAndLifeSpanArray.size(); i++) {
            if (CountriesAndLifeSpanArray.get(i).country.equals(country)) {
                countryIndex = i;
            }
        }

        if (gender.equals("Male")) {//male life expectancy

            result =  (CountriesAndLifeSpanArray.get(countryIndex).yearsMale);

        }
        else if (gender.equals("Female")) {//female life expectancy

            result  = CountriesAndLifeSpanArray.get(countryIndex).yearsFemale;

        }
        else if (gender.equals("Other")) {//alien life expectancy
            result  = ((CountriesAndLifeSpanArray.get(countryIndex).yearsFemale + CountriesAndLifeSpanArray.get(countryIndex).yearsMale)/2);
        }

        return result;
    }

    public void onStop(){
        super.onStop();
        finish();
    }
}
