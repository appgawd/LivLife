package com.bytefruit.patri.carpediem;

import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.KeyboardView;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.viewpagerindicator.UnderlinePageIndicator;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Date;

import static com.bytefruit.patri.carpediem.Utilities.getNavigationBarHeight;
import static com.bytefruit.patri.carpediem.Utilities.getStatusBarHeight;
import static com.bytefruit.patri.carpediem.Utilities.dpFromPx;
import static com.bytefruit.patri.carpediem.Utilities.pxFromDp;
import static com.bytefruit.patri.carpediem.Utilities.setFontsBg;
import static com.bytefruit.patri.carpediem.Utilities.mainBg;

public class DisplayLifespanEstimate extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener, TextWatcher, ViewTreeObserver.OnGlobalLayoutListener{

    private static final String TAG = "CustomizeActivity";

    View textBounds;
    float dX, dY;
    RunCountdownThread runCountdownThread;
    int textColor = 0;
    int bgColor = 0;
    String lastQuoteText;
    boolean bgColorWasSet = false;
    boolean textColorWasSet = false;

    //float lastQuoteTextHeight;
    //float lastQuoteTextWidth;
    //float newQuoteTextHeight;
    //float newQuoteTextWidth;

    boolean isGregorian;
    boolean countdownFormatWasSet = false;

    //boolean atTop = true;

    boolean isCustomizePage = true;

    TextView countdownString;
    EditText quoteText;
    TextView quoteTextUneditable;

    Date deathDate;

    Boolean sliderIsBehind = true;

    View sizeSliderRl;
    View sizeSliderBackground;
    SeekBar sizeSliderQuote;
    SeekBar sizeSliderCountdown;

    int sliderMax;
    boolean checkWidthOnSizeChangeQuote = false;
    float lastProgressWidthQuote;
    int lastQuoteTextSize;
    int currentMaxProgressQuote;
    //float lastMaxedWidthQuote;

    int currentMaxProgressCountdown;
    boolean checkWidthOnSizeChangeCountdown;
    int lastCountdownTextSize;
    float lastProgressWidthCountdown;

    int minTextSize = 15;
    int maxTextSize = 52;

    boolean sliding = false;

    float verticalMargin;
    View rectangleAtTop;
    float maxTextWidth;

    int quoteTextSize;
    int countdownTextSize;

    int activityHeight;
    int activityWidth;

    int textChangePos;
    boolean computerEdited = false;

    public TextView[] tvs = new TextView[2];

    boolean listenForFormatChange = false;
    float lastHeightOfCountdownText;

    public boolean listenForSizeChange = false;
    float lastCountdownWidth;

    public int bgImage = mainBg;

    public float lastWidthForFont;
    public boolean checkFontSizeChange = false;

    public boolean isCheckFontSizeChangeCountdown;

    boolean layoutAdjusted = false;

    public Typeface[] fonts = new Typeface[12];
    public Typeface[] fontsC = new Typeface[12];

    //public static final float defaultQuoteTextXMult = 1f / 9.2f;
    //public static final float defaultQuoteTextYMult = 1f / 4f;
    //public static final float defaultCountdownXMult = 1f / 4.6f;
    //public static final float defaultCountdownYMult = 1f / 3.3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lifespan_estimate);

        deathDate = Utilities.getSavedObjectFromPreference(this, "deathDate", "deathDateKey", Date.class);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        activityHeight = metrics.heightPixels;
        activityWidth = metrics.widthPixels;

        intializeUIElements();

        initializeForTextMovment();

        setStylePrefsToSavedOrDefault();

        setWallpaperStyle();

        startCountdown();

        makeViewWallpaper();

        loadImages();

        loadFonts();
    }

    void loadFonts(){
        for(int i = 0; i < fonts.length; i++){
            fonts[i] = Typeface.createFromAsset(this.getAssets(), "fonts/"+Utilities.fonts[i]+".ttf");
            fontsC[i] = Typeface.createFromAsset(this.getAssets(), "fonts/"+Utilities.fontsC[i]+".ttf");
        }
    }

    void loadImages(){
        Glide.with(this).load(R.drawable.clock_button_2).into((ImageView) findViewById(R.id.clock_button));
        Glide.with(this).load(R.drawable.check_mark).into((ImageView) findViewById(R.id.check_circle));
        Glide.with(this).load(R.drawable.font_size_button_2).into((ImageView) findViewById(R.id.font_size_button));
        Glide.with(this).load(R.drawable.pick_bg_button).into((ImageView) findViewById(R.id.pick_bg_button));
        Glide.with(this).load(R.drawable.text_button_2).into((ImageView) findViewById(R.id.text_button));
    }

    void intializeUIElements(){
        quoteText = (EditText) findViewById(R.id.quote_text);

        quoteText.addTextChangedListener(this);
        quoteText.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    float lastEditWidth = 0;

                    @Override
                    public boolean onPreDraw() {
                        View countdownAndQuoteLayout = findViewById(R.id.countdown_and_quote_layout);

                        if(checkWidthOnSizeChangeQuote && countdownAndQuoteLayout.getWidth() != lastProgressWidthQuote){

                            checkWidthOnSizeChangeQuote = false;

                            if(countdownAndQuoteLayout.getWidth() > maxTextWidth){

                                setQuoteTextSize(lastQuoteTextSize);
                                //sizeSliderQuote.setEnabled(false);
                                sizeSliderQuote.setProgress(lastQuoteTextSize - minTextSize);
                                currentMaxProgressQuote = lastQuoteTextSize - minTextSize;
                                //lastMaxedWidthQuote = quoteText.getWidth();//// this aint right punk
                                return false;
                            }
                            shiftViewToOnScreen(countdownAndQuoteLayout);
                        }

                        if(lastEditWidth != quoteText.getWidth()){
                            if(quoteText.getWidth() < maxTextWidth){
                                currentMaxProgressQuote = sliderMax;
                            }
                            if(quoteText.getWidth() > maxTextWidth && !computerEdited){
                                int savePos = textChangePos;
                                quoteText.setText(quoteText.getText().toString().substring(0, textChangePos)+ "" +quoteText.getText().toString().substring(textChangePos + 1, quoteText.length()));
                                quoteText.setSelection(savePos);
                                return false;
                            }
                            else {//the shift view code is a bit wastefull
                                shiftViewToOnScreen(countdownAndQuoteLayout);
                            }
                        }

                        if(checkFontSizeChange && (countdownAndQuoteLayout.getWidth() < maxTextWidth || (dpFromPx(getApplicationContext(), quoteText.getTextSize()) <= minTextSize && dpFromPx(getApplicationContext(), quoteTextUneditable.getTextSize()) <= minTextSize))){
                            if(countdownAndQuoteLayout.getWidth() > maxTextWidth){
                                quoteTextUneditable.setText(quoteText.getText().toString().substring(0, textChangePos)+ "" +quoteText.getText().toString().substring(textChangePos + 1, quoteText.length()));
                                quoteText.setText(quoteText.getText().toString().substring(0, textChangePos)+ "" +quoteText.getText().toString().substring(textChangePos + 1, quoteText.length()));
                            }else {
                                sizeSliderQuote.setProgress((int) dpFromPx(getApplicationContext(), quoteText.getTextSize()) - minTextSize);
                                shiftViewToOnScreen(countdownAndQuoteLayout);
                                checkFontSizeChange = false;
                                return true;
                            }
                        }
                        else if(checkFontSizeChange && countdownAndQuoteLayout.getWidth() != lastWidthForFont){
                            int reductionRate = 1;
                            quoteText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dpFromPx(getApplicationContext(), quoteText.getTextSize()) - reductionRate);
                            quoteTextUneditable.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dpFromPx(getApplicationContext(), quoteTextUneditable.getTextSize()) - reductionRate);
                            return false;
                        }

                        lastEditWidth = quoteText.getWidth();
                        return  true;
                    }
                });

        quoteTextUneditable = (TextView) findViewById(R.id.quote_text_uneditable);
        countdownString = (TextView) findViewById(R.id.countdown);

        countdownString.getViewTreeObserver().addOnPreDrawListener( // Should probably make the predraw listeners open for the event and close directly after not have them run the whole time
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        if(listenForFormatChange && lastHeightOfCountdownText != countdownString.getHeight()){
                            //countdownString.setY(activityHeight / 2f - countdownString.getHeight() / 2f);
                            //countdownString.setX(activityWidth / 2f - countdownString.getWidth() / 2f);
                            shiftViewToOnScreen(countdownString);
                            listenForFormatChange = false;
                            return false;
                        }

                        if(isCheckFontSizeChangeCountdown && (countdownString.getWidth() < maxTextWidth)) {
                            sizeSliderCountdown.setProgress((int) dpFromPx(getApplicationContext(), countdownString.getTextSize()) - minTextSize);
                            shiftViewToOnScreen(countdownString);
                            isCheckFontSizeChangeCountdown = false;
                            return true;
                        }
                        else if(isCheckFontSizeChangeCountdown && lastCountdownWidth != countdownString.getWidth()){
                            int reductionRate = 1;
                            countdownString.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dpFromPx(getApplicationContext(), countdownString.getTextSize()) - reductionRate);
                            return false;
                        }

                        if(listenForSizeChange && lastCountdownWidth != countdownString.getWidth()){
                            shiftViewToOnScreen(countdownString);
                            listenForSizeChange = false;
                            return false;
                        }
                        return true;
                    }
                });

        countdownString.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {

                        if(checkWidthOnSizeChangeCountdown && countdownString.getWidth() != lastProgressWidthCountdown){
                            shiftViewToOnScreen(countdownString);
                            checkWidthOnSizeChangeCountdown = false;

                            if(countdownString.getWidth() > maxTextWidth){
                                countdownString.setTextSize(TypedValue.COMPLEX_UNIT_DIP, lastCountdownTextSize);
                                countdownTextSize = lastCountdownTextSize;
                                sizeSliderCountdown.setProgress(lastCountdownTextSize - minTextSize);
                                currentMaxProgressCountdown = lastCountdownTextSize - minTextSize;
                                return false;
                            }
                        }
                        return  true;
                    }
                });

        sizeSliderRl = findViewById(R.id.size_slider_rl);
        sizeSliderBackground = findViewById(R.id.size_slider_background);
        sizeSliderQuote = (SeekBar) findViewById(R.id.size_slider_quote);
        sizeSliderQuote.setOnSeekBarChangeListener(this);
        sliderMax = maxTextSize - minTextSize;
        sizeSliderQuote.setMax(sliderMax);
        currentMaxProgressQuote = sliderMax;
        sizeSliderCountdown = (SeekBar) findViewById(R.id.size_slider_countdown);
        sizeSliderCountdown.setMax(sliderMax);
        sizeSliderCountdown.setOnSeekBarChangeListener(this);
        currentMaxProgressCountdown = sliderMax;
        // this line can set progress color
        // sizeSliderCountdown.getProgressDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);


        rectangleAtTop = findViewById(R.id.rectangle_at_the_top);
        rectangleAtTop.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textChangePos = start;
    }

    @Override
    public void afterTextChanged(Editable s) {

        /*if (quoteText.getWidth() > textBounds.getWidth() - verticalMargin * 6 && !justAppended && !computerEdited) {
            justAppended = true;
            s.delete(textChangePos, textChangePos + 1);

            textChanged = false;
        }*/

        /*newEdit = s;
        lastEditWidth = quoteText.getWidth();
        if(!computerEdited && !justAppended){
            quoteText.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            Log.d("sdf   fdgdf fdg", "sdfsgdfshvdsfd");
                            if(lastEditWidth != quoteText.getWidth()){
                                if(quoteText.getWidth() > textBounds.getWidth() - verticalMargin * 2 && !justAppended && !computerEdited){
                                    justAppended = true;
                                    newEdit.delete(textChangePos, textChangePos + 1);

                                    textChanged = false;
                                }else {
                                    View countdownAndQuoteLayout = findViewById(R.id.countdown_and_quote_layout);
                                    shiftViewToOnScreen(countdownAndQuoteLayout);
                                }

                                // Don't forget to remove your listener when you are done with it.
                                if (Build.VERSION.SDK_INT < 16) {
                                    quoteText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                } else {
                                    quoteText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }

                            }
                            lastEditWidth = quoteText.getWidth();
                        }
                    });
        }

        justAppended = false;
        computerEdited = false;*/
    }


    void shiftViewToOnScreen(View v){//should probly use this for the regular text moving too for fancier code
        //if(v.getWidth() > maxTextWidth){
          //  v.setX(verticalMargin);
        //}else {
            if (v.getX() + v.getWidth() > textBounds.getWidth() - verticalMargin) {

                v.setX(textBounds.getWidth() - verticalMargin - v.getWidth());
            }

            if (v.getX() < verticalMargin) {

                v.setX(verticalMargin);
            }
        //}

        if(v.getY() > textBounds.getHeight() - v.getHeight()){
            v.setY(textBounds.getHeight() - v.getHeight());
        }

        if(v.getY() < 0){
            v.setY(0);
        }
    }

    void makeViewWallpaper(){
        Bundle bundle = getIntent().getExtras();
        isCustomizePage = bundle.getBoolean("isCustomizePage", true);

        LinearLayout countdownAndQuoteLayout = (LinearLayout) findViewById(R.id.countdown_and_quote_layout);

        if(!isCustomizePage){
            quoteText.setVisibility(View.GONE);
            rectangleAtTop.setVisibility(View.GONE);
            sizeSliderRl.setVisibility(View.GONE);
            findViewById(R.id.place_holder_view).setVisibility(View.GONE);
        }else{
            quoteText.setVisibility(View.GONE);
            Toast.makeText(this, "Note: you may drag the text to change it's position.", Toast.LENGTH_LONG).show();
        }
    }

    //this method is redundant with the method in viewlivewallpaper you should change this
    public void setWallpaperStyle() {
        SharedPreferences prefs = this.getSharedPreferences("WallpaperStyleValues", MODE_PRIVATE);

        // i dunno why the hell i set the int textColor here but it works
        int textColor = prefs.getInt("textColor", ContextCompat.getColor(this, R.color.androidDefaultTextViewTextColor));
        TextView countdownText = (TextView) findViewById(R.id.countdown);
        countdownText.setTextColor(textColor);

        quoteText.setTextColor(textColor);
        quoteText.setText(lastQuoteText);
        quoteTextUneditable.setText(lastQuoteText);

        quoteTextUneditable.setTextColor(textColor);

        // i dunno why the hell i set the int bgColor here but it works
        int bgColor = prefs.getInt("bgColor", ContextCompat.getColor(this, R.color.androidDefaultActivityBackgroundWhite));
        findViewById(R.id.activity_display_lifespan_estimate).setBackgroundColor(bgColor);
    }

    public void setStylePrefsToSavedOrDefault(){
        SharedPreferences prefs = getSharedPreferences("WallpaperStyleValues", MODE_PRIVATE);

        bgImage = prefs.getInt("bgImage", mainBg);

        textColor = prefs.getInt("textColor", 0);/////////////////////
        bgColor = prefs.getInt("bgColor", 0);//////////////////////////
        lastQuoteText = prefs.getString("quoteText", getString(R.string.default_quote_text));
        isGregorian = prefs.getBoolean("isGregorian", false);

        TextView countdownText = (TextView) findViewById(R.id.countdown);
        if(isGregorian){
            countdownText.setText(RunCountdownThread.countdownGregorian(deathDate, this));
        }else{
            countdownText.setText(RunCountdownThread.countdownYears(deathDate, this));
        }

        int originalTextSizeDp = (int) dpFromPx(getApplicationContext(), getResources().getDimension(R.dimen.countdown_font_size_years));
        countdownTextSize = prefs.getInt("countdownTextSize", originalTextSizeDp);
        int startValueCountdownSlider = countdownTextSize - minTextSize;
        sizeSliderCountdown.setProgress(startValueCountdownSlider);
        countdownString.setTextSize(TypedValue.COMPLEX_UNIT_DIP, countdownTextSize);
        Log.d(TAG, ""+ countdownTextSize);


        countdownString.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {


                    @Override
                    public void onGlobalLayout() {
                        //makeSureNoElementOverlap(countdownString.getY());


                        SharedPreferences prefs = getSharedPreferences("WallpaperStyleValues", MODE_PRIVATE);//may be able to set prefs to final to prevent redundancy same for the view of the countdown

                        //float countdownWidthPx = prefs.getFloat("countdownWidthPx", countdownString.getWidth());
                        //float countdownHeightPx = prefs.getFloat("countdownHeightPx", countdownString.getHeight());

                        //float countdownX = prefs.getFloat("countdownX", activityWidth / 2f - countdownString.getWidth() / 2f);
                        //float countdownY = prefs.getFloat("countdownY", activityHeight / 4f - countdownString.getHeight() / 2f);

                        float countdownX = prefs.getFloat("countdownX", countdownString.getX());
                        float countdownY = prefs.getFloat("countdownY", countdownString.getY());

                        //initiallyPositionText(countdownString, countdownWidthPx, countdownHeightPx, countdownX, countdownY);


                        countdownString.setX(countdownX);
                        countdownString.setY(countdownY);

                        shiftViewToOnScreen(countdownString);

                        // Don't forget to remove your listener when you are done with it.
                        if (Build.VERSION.SDK_INT < 16) {
                            countdownString.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            countdownString.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });

        final View countdownAndQuoteLayout = findViewById(R.id.countdown_and_quote_layout);

        //quoteTextSize = currentTextSizeDp;
        quoteTextSize = prefs.getInt("quoteTextSize", originalTextSizeDp);
        setQuoteTextSize(quoteTextSize);
        int startValueQuoteSlider = quoteTextSize - minTextSize;
        sizeSliderQuote.setProgress(startValueQuoteSlider);

        countdownAndQuoteLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        SharedPreferences prefs = getSharedPreferences("WallpaperStyleValues", MODE_PRIVATE);//may be able to set prefs to final to prevent redundancy same for the view of the countdown

                        View countdownAndQuoteLayout = findViewById(R.id.countdown_and_quote_layout);

                        //lastQuoteTextHeight = prefs.getFloat("quoteTextHeight", activityHeight / 2f - countdownAndQuoteLayout.getHeight() / 2f);
                        //lastQuoteTextWidth = prefs.getFloat("quoteTextWidth", activityWidth / 2f - countdownAndQuoteLayout.getWidth() / 2f);/////////////////three positions need to make sure position of text is saved

                        float textSizePx = pxFromDp(getApplicationContext(), quoteTextSize);
                        float lastQuoteTextHeight = prefs.getFloat("quoteTextHeight", countdownString.getY() - textSizePx);
                        float lastQuoteTextWidth = prefs.getFloat("quoteTextWidth", countdownAndQuoteLayout.getX());

                        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) countdownAndQuoteLayout.getLayoutParams();
                        //params.addRule(RelativeLayout.ABOVE, 0);
                        //countdownAndQuoteLayout.setLayoutParams(params);

                        //countdownString.setY(activityHeight / 2f - countdownString.getHeight() / 2f);
                        //countdownString.setX(activityWidth / 2f - countdownString.getWidth() / 2f);

                        // Layout has happened here.

                        // Don't forget to remove your listener when you are done with it.
                        if (Build.VERSION.SDK_INT < 16) {
                            countdownAndQuoteLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            countdownAndQuoteLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                        //TextView textView = (TextView) findViewById(R.id.countdown);
                        //Log.d("beast", 0 + "y " + 11 + "m " + 30 + "d " + 22 + "h " + 44 + "m " + 57 + "s " +324 + " ms" + "");
                        //textView.setText(0 + "y " + 11 + "m " + 30 + "d " + 22 + "h " + 44 + "m " + 57 + "s " +324 + " ms" + "");

                        //float countdownAndQuoteLayoutWidthPx = prefs.getFloat("countdownAndQuoteLayoutWidthPx", countdownAndQuoteLayout.getWidth());
                        //float countdownAndQuoteLayoutHeightPx = prefs.getFloat("countdownAndQuoteLayoutHeightPx", countdownAndQuoteLayout.getHeight());

                        //initiallyPositionText(countdownAndQuoteLayout, countdownAndQuoteLayoutWidthPx, countdownAndQuoteLayoutHeightPx, lastQuoteTextWidth, lastQuoteTextHeight);

                        //makeSureNoElementOverlap(lastQuoteTextHeight);/////////////////dont work 100% make this method time based and make a new one of nooverlap methods for oncreate

                        countdownAndQuoteLayout.setY(lastQuoteTextHeight);
                        countdownAndQuoteLayout.setX(lastQuoteTextWidth);
                        shiftViewToOnScreen(countdownAndQuoteLayout);
                    }
                });
    }

    public void onStart(){
        super.onStart();

        tvs[0] = quoteText;
        tvs[1] = quoteTextUneditable;
        TextView[] tvc = {countdownString};
        setFontsBg(bgImage, tvs, fonts, fontsC, false);
        setFontsBg(bgImage, tvc, fonts, fontsC,  true);
    }

    /*void initiallyPositionText(View v, float originalWidth, float originalHeight, float posX, float posY){
        float addedWidthDueToTextSize = (originalWidth - v.getWidth()) / 2f;
        float addedHeightDueToTextSize = (originalHeight - v.getHeight()) / 2f;

        v.setY(posY + addedHeightDueToTextSize);
        v.setX(posX + addedWidthDueToTextSize);
    }*/

    public void startCountdown(){

        //Date deathDate = new Date(2019-1900,1,1);
        runCountdownThread = new RunCountdownThread(countdownString, deathDate, isGregorian, this);
        //runCountdownThread.cancel(false);
        runCountdownThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void initializeForTextMovment(){
        findViewById(R.id.countdown_and_quote_layout).setOnTouchListener(this);
        findViewById(R.id.countdown).setOnTouchListener(this);
        textBounds = findViewById(R.id.activity_display_lifespan_estimate);

        textBounds.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        verticalMargin = textBounds.getWidth() / 35.0f;
                        maxTextWidth = textBounds.getWidth() - verticalMargin * 2;

                        if (Build.VERSION.SDK_INT < 16) {
                            textBounds.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            textBounds.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }

        Rect viewRect = new Rect();
        sizeSliderBackground.getGlobalVisibleRect(viewRect);
        if(!viewRect.contains((int) ev.getRawX(), (int) ev.getRawY()) && !sliderIsBehind && !sliding) {
            sizeSliderRl.animate().translationY(0);
            sliderIsBehind = true;
            return true;
        }

        return super.dispatchTouchEvent(ev);
    }

    public void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);

            quoteText.setVisibility(View.GONE);
            quoteTextUneditable.setVisibility(View.VISIBLE);
            quoteTextUneditable.setText(quoteText.getText());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_size_button:
                animateSizeSlider();
                break;
            case R.id.edit_text_button:
                openEditText();
                break;
            case R.id.countdown_format_button:
                chooseCountDownFormatDialog();

                break;
            case R.id.set_background_button:
                FragmentManager fm = getSupportFragmentManager();
                FragmentDialog overlay = new FragmentDialog();
                overlay.show(fm, "Fragment Dialog");
                break;
            case R.id.save_wallpaper_style:
                saveWallpaperStyleValues();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    public void animateSizeSlider(){
        if(sliderIsBehind) {
            sizeSliderRl.animate().translationY(-sizeSliderBackground.getHeight());
            sliderIsBehind = false;
        }
    }

    public void openEditText(){
        quoteText.setVisibility(View.VISIBLE);
        quoteTextUneditable.setVisibility(View.GONE);

        quoteText.requestFocus();
        InputMethodManager keyboard=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(quoteText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void saveWallpaperStyleValues(){
        SharedPreferences.Editor editor = getSharedPreferences("WallpaperStyleValues", MODE_PRIVATE).edit();

        if(textColorWasSet) {
            editor.putInt("textColor", textColor);
        }

        if(countdownFormatWasSet){
            editor.putBoolean("isGregorian", isGregorian);
        }

        if(bgColorWasSet) {
            editor.putInt("bgColor", bgColor);
        }

        TextView quoteText = ((TextView) findViewById(R.id.quote_text));
        String currentQuoteText = quoteText.getText() + "";

        if(!currentQuoteText.equals(lastQuoteText)){
            editor.putString("quoteText", currentQuoteText);
        }

        View countdownAndQuoteLayout = findViewById(R.id.countdown_and_quote_layout);

        editor.putFloat("quoteTextHeight", countdownAndQuoteLayout.getY());
        editor.putFloat("quoteTextWidth", countdownAndQuoteLayout.getX());// this is the left side of the view

        View mainLayout = findViewById(R.id.activity_display_lifespan_estimate);

        float mainLayoutWidth = mainLayout.getWidth();
        float mainLayoutHeight = mainLayout.getHeight();

        float countdownPercentFromLeft = countdownAndQuoteLayout.getX() / mainLayoutWidth;
        float countdownPercentFromTop = countdownAndQuoteLayout.getY() / mainLayoutHeight;

        editor.putFloat("countdownPercentFromTop", countdownPercentFromTop);
        editor.putFloat("countdownPercentFromLeft", countdownPercentFromLeft);

        //TextView countdown = (TextView) findViewById(R.id.countdown);

        editor.putInt("actionBarSizePx", getStatusBarHeight(getResources()));

        editor.putInt("quoteTextSize", (int) dpFromPx(this, quoteText.getTextSize()));

        editor.putFloat("countdownX", countdownString.getX());
        editor.putFloat("countdownY", countdownString.getY());

        editor.putInt("countdownTextSize", (int) dpFromPx(this, countdownString.getTextSize()));

        Log.d(TAG, ""+ (int) dpFromPx(this, countdownString.getTextSize()));

        editor.putInt("bgImage", bgImage);

        editor.commit();
    }

    public void chooseCountDownFormatDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Pick a countdown format:");
        final FrameLayout frameView = new FrameLayout(this);
        builder.setView(frameView);

        final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        inflater.inflate(R.layout.date_format_picker, frameView);
        alertDialog.show();

        Button formatGregorianButton = (Button) alertDialog.findViewById(R.id.date_format_gregorian_button);

        formatGregorianButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences("WallpaperStyleValues", MODE_PRIVATE);//may be able to set prefs to final to prevent redundancy same for the view of the countdown

                if(!isGregorian) {
                    listenForFormatChange = true;
                    countdownString.setText(RunCountdownThread.countdownGregorian(deathDate, getApplicationContext()));
                    /*RelativeLayout.LayoutParams lp_countdownAndQuoteLayout = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    // Specify the TextView position in parent layout
                    lp_countdownAndQuoteLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
                    countdownAndQuoteLayout.setLayoutParams(lp_countdownAndQuoteLayout);*/
                }

                isGregorian = true;
                countdownFormatWasSet = true;
                alertDialog.hide();
                restartCountdown();
            }
        });

        Button yearsDecimalButton = (Button) alertDialog.findViewById(R.id.years_decimal_button);
        yearsDecimalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences("WallpaperStyleValues", MODE_PRIVATE);//may be able to set prefs to final to prevent redundancy same for the view of the countdown

                if(isGregorian) {
                    listenForFormatChange = true;
                    countdownString.setText(RunCountdownThread.countdownYears(deathDate, getApplicationContext()));
                    /*RelativeLayout.LayoutParams lp_countdownAndQuoteLayout = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    // Specify the TextView position in parent layout
                    lp_countdownAndQuoteLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
                    countdownAndQuoteLayout.setLayoutParams(lp_countdownAndQuoteLayout);*/
                }

                isGregorian = false;
                countdownFormatWasSet = true;
                alertDialog.hide();
                restartCountdown();
            }
        });
    }

    //this sets text postion with the draging of the text
    @Override
    public boolean onTouch(View view, MotionEvent event){
        if(isCustomizePage) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float setX = event.getRawX() + dX;
                    float setY = event.getRawY() + dY;

                    if(view.getWidth() > maxTextWidth){
                        view.animate()
                                .x(0 + verticalMargin)
                                .setDuration(0)
                                .start();
                    }else {
                        if (setX >= 0 + verticalMargin && setX <= textBounds.getWidth() - view.getWidth() - verticalMargin) {
                            view.animate()
                                    .x(setX)
                                    .setDuration(0)
                                    .start();
                        } else if (!(setX >= 0 + verticalMargin)) {
                            view.animate()
                                    .x(0 + verticalMargin)
                                    .setDuration(0)
                                    .start();
                        } else {
                            view.animate()
                                    .x(textBounds.getWidth() - view.getWidth() - verticalMargin)
                                    .setDuration(0)
                                    .start();
                        }
                    }
                    if (setY >= 0 && setY <= textBounds.getHeight() - view.getHeight()) {
                        view.animate()
                                .y(setY)
                                .setDuration(0)
                                .start();
                    } else if (!(setY >= 0)) {
                        view.animate()
                                .y(0)
                                .setDuration(0)
                                .start();
                    } else {
                        view.animate()
                                .y(textBounds.getHeight() - view.getHeight())
                                .setDuration(0)
                                .start();
                    }


                    //makeSureNoElementOverlap(setY);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    /*public void makeSureNoElementOverlap(float yPos){
        /*if(yPos < textBounds.getHeight() * .25 ) {
            if(atTop && lastSyncDone) {

                atTop = false;
                lastSyncDone = false;
            }
        }

        if(yPos > textBounds.getHeight() * .6) {
            if(!atTop && lastSyncDone) {

                atTop = true;
                lastSyncDone = false;
            }
        //}end commenthere

        RelativeLayout.LayoutParams sizeSliderRlLp= (RelativeLayout.LayoutParams) sizeSliderRl.getLayoutParams();
        RelativeLayout.LayoutParams rectangleAtTopLp= (RelativeLayout.LayoutParams) rectangleAtTop.getLayoutParams();

        if(yPos < sizeSliderRl.getHeight() * 2){
            sizeSliderRlLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            sizeSliderRl.setLayoutParams(sizeSliderRlLp);

            rectangleAtTopLp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rectangleAtTopLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            sizeSliderRl.setLayoutParams(rectangleAtTopLp);

            atTop = false;
        }
        if(yPos > sizeSliderRl.getHeight() * 2){

            sizeSliderRlLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            sizeSliderRl.setLayoutParams(sizeSliderRlLp);

            rectangleAtTopLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            rectangleAtTopLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            sizeSliderRl.setLayoutParams(rectangleAtTopLp);

            atTop = true;
        }
    }*/

    public void onStop(){
        super.onStop();
        runCountdownThread.cancel(true);
    }

    protected void onRestart() {
        super.onRestart();
        startCountdown();
    }

    public void restartCountdown(){
        runCountdownThread.cancel(true);
        startCountdown();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
            int txtSize = progress + minTextSize;

            if(progress > currentMaxProgressQuote && seekBar.getId() == R.id.size_slider_quote){
                sizeSliderQuote.setProgress(currentMaxProgressQuote);
            }
            if(seekBar.getId() == R.id.size_slider_quote && currentMaxProgressQuote > progress) {
                View countdownAndQuoteLayout = findViewById(R.id.countdown_and_quote_layout);

                checkWidthOnSizeChangeQuote = true;
                lastProgressWidthQuote = countdownAndQuoteLayout.getWidth();
                lastQuoteTextSize = txtSize - 1;

                setQuoteTextSize(txtSize);
            }

            if(progress > currentMaxProgressCountdown && seekBar.getId() == R.id.size_slider_countdown){
                sizeSliderCountdown.setProgress(currentMaxProgressCountdown);
            }
            if(seekBar.getId() == R.id.size_slider_countdown && currentMaxProgressCountdown > progress){
                countdownTextSize = txtSize;
                //shiftViewToOnScreen(countdownString);
                listenForSizeChange = true;
                lastCountdownWidth = countdownString.getWidth();

                checkWidthOnSizeChangeCountdown = true;
                lastProgressWidthCountdown = countdownString.getWidth();
                lastCountdownTextSize = txtSize - 1;

                countdownString.setTextSize(TypedValue.COMPLEX_UNIT_DIP, txtSize);
            }
        }
    }

    void setQuoteTextSize(int txtSize){
        quoteText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, txtSize);
        quoteTextUneditable.setTextSize(TypedValue.COMPLEX_UNIT_DIP, txtSize);
        quoteTextSize = txtSize;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        sliding = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        sliding = false;
    }

    @Override
    public void onGlobalLayout() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.background_rl);
        if(layoutAdjusted){

            ImageView iv = (ImageView) findViewById(R.id.background_image);
            //iv.setImageResource(bgImage);
            Glide.with(this).load(bgImage).into(iv);

            // Don't forget to remove your listener when you are done with it.
            if (Build.VERSION.SDK_INT < 16) {
                rl.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            } else {
                rl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl.getLayoutParams();
            int topMargin = -getStatusBarHeight(getResources());
            params.topMargin = topMargin;
            int bottomMargin = -getNavigationBarHeight(getResources()) + -findViewById(R.id.rectangle_at_the_top).getHeight();
            params.bottomMargin = bottomMargin;

            rl.getViewTreeObserver().addOnGlobalLayoutListener(this);
            layoutAdjusted = true;

            // Don't forget to remove your listener when you are done with it.
            if (Build.VERSION.SDK_INT < 16) {
                rectangleAtTop.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            } else {
                rectangleAtTop.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }

    // TextView countdown = (TextView) findViewById(R.id.countdown);
    // countdown.setText(Utilities.updateCountdownYears(startTime, countdown.getText() + ""));
}


