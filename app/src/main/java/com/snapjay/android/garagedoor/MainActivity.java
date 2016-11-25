package com.snapjay.android.garagedoor;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.snapjay.android.garagedoor.utilites.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mDoorStatus;
    private Button mActionDoor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDoorStatus = (TextView) findViewById(R.id.doorStatus);
        mActionDoor = (Button) findViewById(R.id.actionDoor);

        // on click
        // makeGithubSearchQuery();
    }

    public void getStatus(View view) {

        URL buildUrl = NetworkUtils.buildUrl("getStatus");
        Log.d("MainActivity", buildUrl.toString());
        new GithubQueryTask().execute(buildUrl);
    }

  public void actionDoor(View view) {

        URL buildUrl = NetworkUtils.buildUrl("action");
        Log.d("MainActivity", buildUrl.toString());
        new GithubQueryTask().execute(buildUrl);
    }


    public class GithubQueryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }


        @Override
        protected void onPostExecute(String response) {

            if (response != null && !response.equals("")) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
            //      Log.d("onPostExecute", status.toString());

                    mDoorStatus.setText(jsonResponse.toString());
                    mDoorStatus.setText(status);

                } catch (JSONException e){
                    e.printStackTrace();
                }


            }
        }
    }
}
