package com.snapjay.android.garagedoor.utilites;

import android.net.Uri;
import android.util.Log;

import com.snapjay.android.garagedoor.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String BASE_URL =  "http://door.snapjay.com:8080" + "/api";


    /**
     * Builds the URL used to query .
     *
     * @param path The API resource name.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String path) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(path)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("NetworkUtils", url.toString());

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}