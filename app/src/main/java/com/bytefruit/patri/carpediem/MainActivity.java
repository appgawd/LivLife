package com.bytefruit.patri.carpediem;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.lang.reflect.GenericArrayType;
import java.util.Date;
import java.util.Random;

import static com.bytefruit.patri.carpediem.Utilities.setFont;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivityTag";

    RunCountdownThread runCountdownThread;

    TextView countdown;

    private InterstitialAd mInterstitialAd;
    public int delay = 10000;

    /*private static final String TAG = "InAppBilling";
    IabHelper mHelper;
    static final String NO_ADS_SKU = "android.test.purchased";
    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;
    // Has the user purchased no ads?
    boolean noAdsPurchased = false;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startIAB();
        //String countdownFont = "roboto";
        Typeface roboto = Typeface.createFromAsset(this.getAssets(), "fonts/roboto.ttf");
        countdown = (TextView) findViewById(R.id.countdown);
        //setFont(countdownFont, countdown ,this);
        TextView quoteText = (TextView) findViewById(R.id.countdown);
        countdown.setTypeface(roboto);
        quoteText.setTypeface(roboto);
        //setFont(countdownFont, quoteText, this);

        //runAds();

        //startButtonListeners();


        startCountdown();

        Glide.with(this).load(R.drawable.llcover).into((ImageView) findViewById(R.id.logo));
    }

    /*void startIAB(){
        String base64EncodedPublicKey = "<YOUR KEY HERE>";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.enableDebugLogging(false);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(MainActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             *commentEndsHEre

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(NO_ADS_SKU);
            noAdsPurchased = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (noAdsPurchased ? "No Ads True" : "No Ads False"));

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    public void receivedBroadcast() {

    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }*/

    int random(int low, int high){
        Random r = new Random();
        int result = r.nextInt(high-low) + low;
        return result;
    }

    void runAds(){
        MobileAds.initialize(this,"ca-app-pub-3015684364358485~7252467057");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3015684364358485/8729200250");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();//////////////////////////////////////////////////////////////////////////////
                    //Log.d(TAG, "The interstitial wasn't loaded yet.");

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            // Load the next interstitial.
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        }

                    });

                } else {
                    if(!mInterstitialAd.isLoading()) {
                        //Log.d(TAG, "Interstitial wasn't loading.");
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                }
                handler.postDelayed(this, random(1000, 200000));
            }
        };

        handler.postDelayed(runnable, delay);
    }

    public void startCountdown(){

        SharedPreferences prefs = getSharedPreferences("WallpaperStyleValues", MODE_PRIVATE);
        boolean isGregorian = prefs.getBoolean("isGregorian", false);
        Date deathDate = Utilities.getSavedObjectFromPreference(this, "deathDate", "deathDateKey", Date.class);
        runCountdownThread = new RunCountdownThread(countdown, deathDate, isGregorian, this);
        runCountdownThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        View countdown = findViewById(R.id.countdown);
        if(isGregorian) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) countdown.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.quote_text);
            countdown.setLayoutParams(params);
        }else{
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) countdown.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.addRule(RelativeLayout.CENTER_VERTICAL, 0);
            params.addRule(RelativeLayout.ALIGN_LEFT, 0);
            countdown.setLayoutParams(params);
        }
    }

    /*public void startButtonListeners(){
        findViewById(R.id.customize_wallpaper).setOnClickListener(this);
        (R.id.set_as_live_wallpaper).setOnClickListener(this);
        findViewById(R.id.set_time_for_countdown).setOnClickListener(this);
    }*/

    @Override
    public void onClick(View v) {
        //Log.d("chode", "");
        switch (v.getId()) {
            case R.id.customize_wallpaper:
                Intent startCustomizeWallpaper = new Intent(this, DisplayLifespanEstimate.class);
                startCustomizeWallpaper.putExtra("isCustomizePage", true);
                startActivity(startCustomizeWallpaper);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                break;
            case R.id.set_as_live_wallpaper:
                setLiveWallpaper();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.set_time_for_countdown:
                startActivity(new Intent(this, LifespanCalculator.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                break;
            case R.id.view_wallpaper:
                Intent startViewWallpaper = new Intent(this, DisplayLifespanEstimate.class);
                startViewWallpaper.putExtra("isCustomizePage", false);
                startActivity(startViewWallpaper);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                break;
        }
    }

    public void setLiveWallpaper(){
        Intent i = new Intent();

        if (Build.VERSION.SDK_INT > 15)
        {
            i.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            String pkg = LiveWallpaper.class.getPackage().getName();
            String cls = LiveWallpaper.class.getCanonicalName();
            i.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(pkg, cls));
        }
        else
        {
            i.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        }
        startActivityForResult(i, 0);
    }

    public void onStop(){
        super.onStop();
        runCountdownThread.cancel(true);
    }

    protected void onRestart() {
        super.onRestart();
        startCountdown();
    }

    public void restartCountdown(){//this shit probly aint needed
        runCountdownThread.cancel(true);
        startCountdown();
    }
}

