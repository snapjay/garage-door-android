package com.snapjay.android.garagedoor.utilites;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.snapjay.android.garagedoor.R;

/**
 * Created by dan on 11/28/2016.
 */

public class Utils {

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


}
