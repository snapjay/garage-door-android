package com.snapjay.android.garagedoor.utilites;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.snapjay.android.garagedoor.R;

import java.util.Calendar;

/**
 * Manage Theme
 */

public class Theme {

    public final static int DAY_MODE = 1;
    public final static int NIGHT_MODE = 2;
    private static int _sTheme;


    public static  int getTheme(){
        return _sTheme;
    }
    public static void setTheme(int theme){
        _sTheme = theme;
    }

    // http://mrbool.com/how-to-change-the-layout-theme-of-an-android-application/25837
    public static void onActivityCreateSetTheme(Activity activity) {

        if (getTheme() == 0){
             _setDefaultTheme();
        }

        switch (getTheme()) {
            case DAY_MODE:
                activity.setTheme(R.style.DayMode);
                Log.i("Theme", "SET DAY MODE");
                break;
            case NIGHT_MODE:
                activity.setTheme(R.style.NightMode);
                Log.i("Theme", "SET NIGHT MODE");
                break;
        }
    }

    public static void toggle(Activity activity){

        if (getTheme() == DAY_MODE){
            setTo(activity, NIGHT_MODE);
        } else {
            setTo(activity, DAY_MODE);
        }
    }

    public static void setTo(Activity activity, int theme){
        setTheme(theme);
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    private static void _setDefaultTheme(){
        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);

        if (h >= 6 && h <= 19) {
            setTheme(DAY_MODE);
        } else {
            setTheme(NIGHT_MODE);
        }

    }

}
