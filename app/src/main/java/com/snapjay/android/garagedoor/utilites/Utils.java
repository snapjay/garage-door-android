package com.snapjay.android.garagedoor.utilites;

import android.app.Activity;
import android.util.Log;

import com.snapjay.android.garagedoor.R;

/**
 * Created by dan on 11/28/2016.
 */

public class Utils {



    public final static int DAY_MODE = 1;
    public final static int NIGHT_MODE = 2;

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }


    public static void changeToTheme(Activity activity, int mode){
//http://mrbool.com/how-to-change-the-layout-theme-of-an-android-application/25837
        if (mode == DAY_MODE) {
            activity.setTheme(R.style.AppTheme);
        }     if (mode == NIGHT_MODE) {
            Log.d("UTILS", "SET NIGHT MODE");
            activity.setTheme(R.style.NightMode);
        }


    }

}
