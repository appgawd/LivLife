package com.bytefruit.patri.carpediem;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.TypeAdapterFactory;

import static com.bytefruit.patri.carpediem.Utilities.mainBg;
import static com.bytefruit.patri.carpediem.Utilities.setFontsBg;


public class Page extends Fragment {

    public int image = mainBg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_tab_1, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.bluegreen_mountains);

        setTextView(image, rootView);

        Glide.with(this).load(image).into(imageView);

        return rootView;
    }

    void setTextView(int id, ViewGroup rootView) {
        //switch (id) {
            //case R.drawable.hex:
                setTextQualities(R.id.hex_text, R.id.hex_time, rootView);
                //break;
            /*case R.drawable.birds:
                setTextQualities(R.id.birds_text, R.id.birds_time, rootView);
                break;
            case R.drawable.mosaic:
                setTextQualities(R.id.mosaic_text, R.id.mosaic_time, rootView);
                break;
            case R.drawable.heartbeat:
                setTextQualities(R.id.heartbeat_text, R.id.heartbeat_time, rootView);
                break;
            case R.drawable.blue_pyramid:
                setTextQualities(R.id.blue_pyramid_text, R.id.blue_pyramid_time, rootView);
                break;
            case R.drawable.orange_pink:
                setTextQualities(R.id.blue_pyramid_text, R.id.blue_pyramid_time, rootView);
                break;
            case R.drawable.black_diamonds:
                setTextQualities(R.id.hex_text, R.id.hex_time, rootView);
                break;
            case R.drawable.brush_fancy:
                setTextQualities(R.id.birds_text, R.id.birds_time, rootView);
                break;
            case R.drawable.green_haze:
                setTextQualities(R.id.mosaic_text, R.id.mosaic_time, rootView);
                break;
            case R.drawable.sky_black:
                setTextQualities(R.id.heartbeat_text, R.id.heartbeat_time, rootView);
                break;
            case R.drawable.turquois:
                setTextQualities(R.id.blue_pyramid_text, R.id.blue_pyramid_time, rootView);
                break;
            case R.drawable.violet:
                setTextQualities(R.id.blue_pyramid_text, R.id.blue_pyramid_time, rootView);
                break;*/
       // }
    }

    void setTextQualities(int quoteId, int countdownId, ViewGroup rootView) {
        TextView artboardText = (TextView) rootView.findViewById(quoteId);
        TextView artboardTime = (TextView) rootView.findViewById(countdownId);
        artboardText.setVisibility(View.VISIBLE);
        artboardTime.setVisibility(View.VISIBLE);
        TextView[] tvs = {artboardText};
        TextView[] tvc = {artboardTime};
        setFontsBg(image, tvs, ((DisplayLifespanEstimate) getActivity()).fonts, ((DisplayLifespanEstimate) getActivity()).fontsC, false);
        setFontsBg(image, tvc, ((DisplayLifespanEstimate) getActivity()).fonts, ((DisplayLifespanEstimate) getActivity()).fontsC, true);
    }

    /*public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }*/
}