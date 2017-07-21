package com.bytefruit.patri.carpediem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import static com.bytefruit.patri.carpediem.Utilities.mainBg;
import static com.bytefruit.patri.carpediem.Utilities.setFontsBg;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Page page = new Page();
        int resource = imageSwitch(position);
        page.image = resource;
        return page;
    }

    @Override
    public int getCount() {
        return 12;
    }

    public static int imageSwitch(int position){
        int resource = mainBg;
        switch (position) {
            case 0:
                resource = R.drawable.white;
                break;
            case 1:
                resource = R.drawable.black;
                break;
            case 2:
                resource = R.drawable.lightgrey;
                break;
            case 3:
                resource = R.drawable.darkgrey;
                break;
            case 4:
                resource = R.drawable.yellow;
                break;
            case 5:
                resource = R.drawable.orange;
                break;
            case 6:
                resource = R.drawable.emerald;
                break;
            case 7:
                resource = R.drawable.blue;
                break;
            case 8:
                resource = R.drawable.purple;
                break;
            case 9:
                resource = R.drawable.pink;
                break;
            case 10:
                resource = R.drawable.red;
                break;
            case 11:
                resource = R.drawable.space;
                break;
        }
        return resource;
    }
}