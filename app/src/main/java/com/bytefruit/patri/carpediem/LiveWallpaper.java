package com.bytefruit.patri.carpediem;

/**
 * Created by patri on 3/20/2017.
 */

import android.app.Service;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.SurfaceHolder;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;

import static com.bytefruit.patri.carpediem.Utilities.dpFromPx;
import static com.bytefruit.patri.carpediem.Utilities.getNavigationBarHeight;
import static com.bytefruit.patri.carpediem.Utilities.getStatusBarHeight;
import static com.bytefruit.patri.carpediem.Utilities.mainBg;
import static com.bytefruit.patri.carpediem.Utilities.setFontsBg;

public class LiveWallpaper extends WallpaperService {

    int textColor;
    int bgColor;
    float lastQuoteTextHeight = -1;
    float lastQuoteTextWidth = -1;
    float countdownX = -1;
    float countdownY = -1;
    Date deathDate;
    RelativeLayout rl;
    int countdown_font_size_years = 24;//this has gotta be same as the value in the resources
    boolean first = true;
    LinearLayout countDownAndQuoteLayout;
    //int actionBarSizePx;
    TextView countdown;
    Boolean isGregorian = false;
    String quoteTextString;
    float countdownTextSize = -1;
    float quoteTextSize = -1;
    int bgImage = mainBg;
    Typeface[] fonts = new Typeface[12];
    Typeface[] fontsC = new Typeface[12];

    @Override
    public Engine onCreateEngine() {

        SharedPreferences prefs = this.getSharedPreferences("WallpaperStyleValues", MODE_PRIVATE);
        textColor = prefs.getInt("textColor", ContextCompat.getColor(this, R.color.androidDefaultTextViewTextColor));
        bgColor = prefs.getInt("bgColor", ContextCompat.getColor(this, R.color.androidDefaultActivityBackgroundWhite));
        deathDate = Utilities.getSavedObjectFromPreference(this, "deathDate", "deathDateKey", Date.class);
        isGregorian = prefs.getBoolean("isGregorian", false);
        //actionBarSizePx = prefs.getInt("actionBarSizePx", 0);
        quoteTextString = prefs.getString("quoteText", getString(R.string.default_quote_text));

        lastQuoteTextHeight = prefs.getFloat("quoteTextHeight", -1);// 0 needs to be put to default value
        lastQuoteTextWidth = prefs.getFloat("quoteTextWidth", -1);

        countdownTextSize = prefs.getInt("countdownTextSize", -1);
        quoteTextSize = prefs.getInt("quoteTextSize", -1);

        countdownX = prefs.getFloat("countdownX", -1);
        countdownY = prefs.getFloat("countdownY", -1);

        bgImage = prefs.getInt("bgImage", mainBg);

        //float countdownX = prefs.getFloat("countdownX", activityWidth / 2f - countdownString.getWidth() / 2f);
        //float countdownY = prefs.getFloat("countdownY", activityHeight / 4f - countdownString.getHeight() / 2f);

        first = true;

        loadFonts();

        return new DemoWallpaperEngine();
    }

    void loadFonts(){
        for(int i = 0; i < fonts.length; i++){
            fonts[i] = Typeface.createFromAsset(this.getAssets(), "fonts/"+Utilities.fonts[i]+".ttf");
            fontsC[i] = Typeface.createFromAsset(this.getAssets(), "fonts/"+Utilities.fontsC[i]+".ttf");
        }
    }

    private class DemoWallpaperEngine extends Engine {


        private boolean mVisible = false;
        private final Handler mHandler = new Handler();
        private final Runnable mUpdateDisplay = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {

                    //android.os.Debug.waitForDebugger();
                    Paint numberP = new Paint();
                    numberP.setStyle(Paint.Style.FILL);
                    numberP.setColor(Color.WHITE);
                    c.drawPaint(numberP);

                    numberP.setColor(bgColor);
                    c.drawRect(0, 0, c.getWidth(), c.getHeight(), numberP);

                    String num;
                    if (isGregorian) {
                        num = "" + RunCountdownThread.countdownGregorian(deathDate, getApplicationContext());
                    } else {
                        num = "" + RunCountdownThread.countdownYears(deathDate, getApplicationContext());
                    }

                    if (first) {
                        int statusBarHeight = getStatusBarHeight(getResources());
                        int navigationBarHeight = getNavigationBarHeight(getResources());
                        float pxHeightRectangleAtTop = getResources().getDimension(R.dimen.rectangle_at_top_height);
                        float bgHeight = ((float) c.getHeight() - pxHeightRectangleAtTop - statusBarHeight);
                        int fullScreenHeight = c.getHeight() + navigationBarHeight;

                        rl = new RelativeLayout(getApplicationContext());

                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT
                        );

                        //lp.height = c.getHeight(); //- actionBarSizePx;
                        //lp.width = c.getWidth();

                        // Set RelativeLayout LayoutParams
                        rl.setLayoutParams(lp);

                        ImageView iv = new ImageView(getApplicationContext());
                        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        iv.setImageResource(bgImage);
                        RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(c.getWidth(), fullScreenHeight);
                        iv.setLayoutParams(ivParams);
                        rl.addView(iv);


                        if(quoteTextSize == -1){
                            quoteTextSize = dpFromPx(getApplicationContext(), getResources().getDimension(R.dimen.countdown_font_size_years));
                        }

                        TextView quoteText = new TextView(getApplicationContext());
                        quoteText.setText(quoteTextString);
                        quoteText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, quoteTextSize);
                        TextView[] tvQ = {quoteText};
                        rl.addView(quoteText);

                        if(countdownTextSize == -1){
                            countdownTextSize = dpFromPx(getApplicationContext(), getResources().getDimension(R.dimen.countdown_font_size_years));
                        }

                        countdown = new TextView(getApplicationContext());
                        countdown.setText(num);
                        countdown.setTextSize(TypedValue.COMPLEX_UNIT_DIP, countdownTextSize);
                        TextView[] tvC = {countdown};
                        setFontsBg(bgImage, tvC, fonts, fontsC, true);
                        rl.addView(countdown);

                        setFontsBg(bgImage, tvQ, fonts, fontsC, false);

                        rl.measure(c.getWidth(), c.getHeight());
                        rl.layout(0, 0, c.getWidth(), c.getHeight());

                        if (lastQuoteTextWidth == -1) {
                            lastQuoteTextWidth = centerTextX(quoteText, c.getWidth());
                        }
                        if (lastQuoteTextHeight == -1) {
                            lastQuoteTextHeight = centerTextY(quoteText, bgHeight) - getResources().getDimension(R.dimen.countdown_font_size_years);
                        }

                        if(countdownX == -1){
                            countdownX = centerTextX(countdown, c.getWidth());
                        }
                        if(countdownY == -1){
                            countdownY =  centerTextY(countdown, bgHeight);
                        }

                        quoteText.setX(lastQuoteTextWidth);
                        quoteText.setY(setSimilarY(lastQuoteTextHeight, statusBarHeight));

                        countdown.setX(countdownX);
                        countdown.setY(setSimilarY(countdownY, statusBarHeight));

                        /*// Initialize a new RelativeLayout
                        rl = new RelativeLayout(getApplicationContext());
                        // Create LayoutParams for RelativeLayout
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT
                        );

                        lp.height = c.getHeight() - actionBarSizePx;
                        lp.width = c.getWidth();
                        ;
                        // Set RelativeLayout LayoutParams
                        rl.setLayoutParams(lp);

                        countDownAndQuoteLayout = new LinearLayout(getApplicationContext());
                        countDownAndQuoteLayout.setId(R.id.countdown_linear_layout_live_wallpaper);
                        countDownAndQuoteLayout.setOrientation(LinearLayout.VERTICAL);

                        rl.addView(countDownAndQuoteLayout);

                        // Ad the TextView to RelativeLayout as child View
                        TextView quoteText = new TextView(getApplicationContext());
                        rl.addView(quoteText);

                        // Set a text for TextView
                        quoteText.setText(quoteTextString);
                        quoteText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, countdown_font_size_years);
                        quoteText.setTextColor(textColor);

                        // Initialize a new TextView widget
                        countdown = new TextView(getApplicationContext());

                        // Set a text for TextView
                        countdown.setText(num);
                        countdown.setTextSize(TypedValue.COMPLEX_UNIT_DIP, countdown_font_size_years);
                        countdown.setTextColor(textColor);
                        countDownAndQuoteLayout.addView(countdown);
                        rl.measure(c.getWidth(), c.getHeight());
                        rl.layout(0, 0, c.getWidth(), c.getHeight());

                        if (lastQuoteTextHeight == -1) {
                            lastQuoteTextHeight = DisplayLifespanEstimate.defaultQuoteTextYMult * c.getWidth();
                        }
                        if (lastQuoteTextWidth == -1) {
                            lastQuoteTextWidth = DisplayLifespanEstimate.defaultQuoteTextXMult * c.getHeight();
                        }

                        countDownAndQuoteLayout.setX(lastQuoteTextWidth);
                        countDownAndQuoteLayout.setY(lastQuoteTextHeight);

                        countDownAndQuoteLayout.measure(c.getWidth(), c.getHeight());*/
                    }

                    countdown.setText(num);
                    rl.draw(c);

                    /*Resources res = getResources();
                    Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.test_image);

                    float canvasAspectRatio = (float) c.getHeight() / (float) c.getWidth();
                    float backgroundAspectRation = (float) (bm.getHeight()) / (float) (bm.getWidth());

                    Log.d("sdflkj", "fsd");
                    Rect rect;
                    if(canvasAspectRatio < backgroundAspectRation){
                        rect = new Rect((bm.getWidth() / 2) + (c.getWidth() / 2), 0, c.getWidth(), bm.getHeight());
                    }else{
                        float scaledImageWidth =((float) (c.getWidth()) / backgroundAspectRation);
                        int leftBound = (int) (((float) (c.getWidth()) / 2f) - (scaledImageWidth / 2f));
                        rect = new Rect(leftBound, 0, (int) scaledImageWidth + leftBound, c.getHeight());
                    }
                    c.drawBitmap(bm, null, rect, null);*/
                    /*Drawable image = getApplicationContext().getResources().getDrawable(R.drawable.test_image);
                    Rect imageBounds = c.getClipBounds();  // Adjust this for where you want it

                    image.setBounds(imageBounds);
                    image.draw(c);*/

                    first = false;
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }
            mHandler.removeCallbacks(mUpdateDisplay);
            if (mVisible) {
                mHandler.postDelayed(mUpdateDisplay, 1);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                draw();
            } else {
                mHandler.removeCallbacks(mUpdateDisplay);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            draw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mUpdateDisplay);
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            mVisible = false;
            mHandler.removeCallbacks(mUpdateDisplay);
        }
    }


    /*public static int getDate() {

        return MainActivity.test;
    }*/

    static float centerTextY(TextView tv, float bgHeight){
        return (bgHeight / 2f) - ((float) tv.getHeight() / 2f);
    }

    static float centerTextX(TextView tv, float bgWidth){
        return (((float) bgWidth / 2f) - ((float)tv.getWidth() / 2f));
    }

    static float setSimilarY(float yPos, float spaceAbove){
        return yPos + spaceAbove;
    }
}




