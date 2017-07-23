package com.bytefruit.patri.carpediem;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.Date;

public class ViewLiveWallpaper extends AppCompatActivity {

    TextView countdownString;
    RunCountdownThread runCountdownThread;

    boolean isGregorian = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_live_wallpaper);

        setWallpaperStyle();

        startCountdown();
    }

    public void setWallpaperStyle() {
        SharedPreferences prefs = this.getSharedPreferences("WallpaperStyleValues", MODE_PRIVATE);

        isGregorian = prefs.getBoolean("isGregorian", false);

        int textColor = prefs.getInt("textColor", ContextCompat.getColor(this, R.color.androidDefaultTextViewTextColor));
        TextView countdownText = (TextView) findViewById(R.id.countdown);
        countdownText.setTextColor(textColor);

        TextView quoteText = (TextView) findViewById(R.id.quote_text);
        quoteText.setTextColor(textColor);
        quoteText.setText(prefs.getString("quoteText", getString(R.string.default_quote_text2)));

        int bgColor = prefs.getInt("bgColor", ContextCompat.getColor(this, R.color.androidDefaultActivityBackgroundWhite));
        findViewById(R.id.activity_view_live_wallpaper).setBackgroundColor(bgColor);

        /*if(prefs.getBoolean("isBackButtonBlack", true)) {
            TextView textView = (TextView) findViewById(R.id.homepage_button_text);
            textView.setTextColor(Color.BLACK);

            ImageView imageView = (ImageView) findViewById(R.id.back_triangle);
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.carpe_diem_triangle_black));
        }else{
            TextView textView = (TextView) findViewById(R.id.homepage_button_text);
            textView.setTextColor(Color.WHITE);

            ImageView imageView = (ImageView) findViewById(R.id.back_triangle);
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.carpe_diem_triangle_white));
        }*/

        View countdownAndQuoteLayout = findViewById(R.id.countdown_and_quote_layout);

        countdownAndQuoteLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        View countdownAndQuoteLayout = findViewById(R.id.countdown_and_quote_layout);

                        SharedPreferences prefs = getSharedPreferences("WallpaperStyleValues", MODE_PRIVATE);//may be able to set prefs to final to prevent redundancy same for the view of the countdown

                        //if(prefs.contains("quoteTextHeight")) {
                        float lastQuoteTextHeight = prefs.getFloat("quoteTextHeight", countdownAndQuoteLayout.getY());
                        //} else{
                        //}

                        // if(prefs.contains("quoteTextWidth")) {
                        float lastQuoteTextWidth = prefs.getFloat("quoteTextWidth", countdownAndQuoteLayout.getX());/////////////////three positions need to make sure position of text is saved
                        //}else{
                        // }

                        // Layout has happened here.

                        // Don't forget to remove your listener when you are done with it.
                        if (Build.VERSION.SDK_INT < 16) {
                            countdownAndQuoteLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            countdownAndQuoteLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                        countdownAndQuoteLayout.setY(lastQuoteTextHeight);
                        countdownAndQuoteLayout.setX(lastQuoteTextWidth);
                    }
                });
    }

    void onClick(View v){
        switch (v.getId()){
            /*case R.id.homepage_button:
                startActivity(new Intent(this, MainActivity.class));
                break;*/
        }
    }

    //method somewhat redundant with method in customize page
    public void startCountdown(){
        countdownString = (TextView) findViewById(R.id.countdown);

        //Date deathDate = new Date(2019-1900,1,1);
        Date deathDate = Utilities.getSavedObjectFromPreference(this, "deathDate", "deathDateKey", Date.class);
        runCountdownThread = new RunCountdownThread(countdownString, deathDate, isGregorian, this);////////////////////////////false needs to be changed to is gregorian
        //runCountdownThread.cancel(false);
        runCountdownThread.execute();
    }

    public void onStop(){
        super.onStop();
        runCountdownThread.cancel(true);
    }

    protected void onRestart() {
        super.onRestart();
        startCountdown();
    }
}