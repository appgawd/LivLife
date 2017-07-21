package com.bytefruit.patri.carpediem;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.viewpagerindicator.CirclePageIndicator;

import org.w3c.dom.Text;

import static com.bytefruit.patri.carpediem.Utilities.getNavigationBarHeight;
import static com.bytefruit.patri.carpediem.Utilities.pxFromDp;
import static com.bytefruit.patri.carpediem.Utilities.setFontsBg;

/**
 * Created by patri on 5/17/2017.
 */

public class FragmentDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener{

    // ------------------------------------------------------------------------
// members
// ------------------------------------------------------------------------

    private ScreenSlidePagerAdapter screenSlidePagerAdapter;
    private ViewPager viewPager;

    float initialCountdownStringWidth;
    float initialQuoteTextUneditableWidth;
    float initialQuoteTextWidth;
    DisplayLifespanEstimate d;

// ------------------------------------------------------------------------
// public usage
// ------------------------------------------------------------------------

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container);
        view.findViewById(R.id.set_background).setOnClickListener(this);

        // tab slider
        screenSlidePagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        height = height + getNavigationBarHeight(getResources());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager)view.findViewById(R.id. pager);
        RelativeLayout.LayoutParams  params = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
        params.height = (height * 5) / 8;
        params.width = (width * 5)  / 8;

        //Log.d("chode",((height * 5) / 8)+" "+ (width * 5)  / 8);
        viewPager.setLayoutParams(params);
        viewPager.setAdapter(screenSlidePagerAdapter);

        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.fragment_dialog_rl);
        rl.setLayoutParams(params);

        CirclePageIndicator indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_background:
                int bgImage = ScreenSlidePagerAdapter.imageSwitch(viewPager.getCurrentItem());
                d = (DisplayLifespanEstimate) getActivity();
                d.currentMaxProgressCountdown = d.sliderMax;
                d.currentMaxProgressQuote = d.sliderMax;
                d.bgImage = bgImage;
                ImageView iv = (ImageView) getActivity().findViewById(R.id.background_image);
                setFontsBg(bgImage, d.tvs, ((DisplayLifespanEstimate) getActivity()).fonts, ((DisplayLifespanEstimate) getActivity()).fontsC, false);
                TextView[] tvc = {d.countdownString};
                setFontsBg(bgImage, tvc, ((DisplayLifespanEstimate) getActivity()).fonts, ((DisplayLifespanEstimate) getActivity()).fontsC, true);
                Glide.with(getActivity()).load(bgImage).into(iv);
                this.dismiss();

                d.isCheckFontSizeChangeCountdown = true;

                d.lastWidthForFont = d.quoteText.getWidth();
                d.checkFontSizeChange = true;

                //d.countdownString.setX((d.activityWidth / 2)  - (d.countdownString.getWidth() / 2));
                //d.countdownString.setY((d.activityHeight / 2) - (d.countdownString.getHeight() / 2));

                /*float textSize = d.activityHeight / 48;
                d.quoteText.setTextSize(textSize);
                d.countdownString.setTextSize(textSize);
                d.quoteTextUneditable.setTextSize(textSize);*/

                /*d.quoteTextUneditable.setX(d.activityWidth / 2);
                d.quoteText.setX(d.activityWidth / 2);
                d.countdownString.setX(d.activityWidth / 2);

                d.quoteText.setY((d.activityHeight / 2) - textSize);*/
                break;
        }
    }
}
