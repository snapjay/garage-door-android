package com.snapjay.android.garagedoor.utilites;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.snapjay.android.garagedoor.R;

/**
 * Manage Theme
 */

public class Theme {

    public final static int DAY_MODE = 1;
    public final static int NIGHT_MODE = 2;

    private static int sTheme;

    public static void onActivityCreateSetTheme(Activity activity) {

        switch (sTheme) {
            default:
                sTheme = DAY_MODE;
                activity.setTheme(R.style.DayMode);
                Log.d("UTILS", "SET DAY MODE");
                break;
            case DAY_MODE:
                activity.setTheme(R.style.DayMode);
                Log.d("UTILS", "SET DAY MODE");
                break;
            case NIGHT_MODE:
                activity.setTheme(R.style.NightMode);
                Log.d("UTILS", "SET NIGHT MODE");
                break;
        }
    }

    public static void toggle(Activity activity){

        if (sTheme == DAY_MODE){
            setTo(activity, NIGHT_MODE);
        } else {
            setTo(activity, DAY_MODE);
        }
    }

    public static void setTo(Activity activity, int theme){
//http://mrbool.com/how-to-change-the-layout-theme-of-an-android-application/25837

        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));

    }

}
