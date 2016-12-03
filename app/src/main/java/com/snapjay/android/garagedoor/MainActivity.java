package com.snapjay.android.garagedoor;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.Geofence;
import com.snapjay.android.garagedoor.utilites.NetworkUtils;
import com.snapjay.android.garagedoor.utilites.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private TextView mDoorStatus;
    private Button mActionDoor;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(String.valueOf(R.string.url));
            //mSocket = IO.socket(String.valueOf("http://door.snapjay.com:8080"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // https://code.tutsplus.com/tutorials/how-to-work-with-geofences-on-android--cms-26639
        Geofence geoFence = new Geofence.Builder()
                .setRequestId("door") // Geofence ID
                .setCircularRegion( 43.958084, -79.354174, 150) // defining fence region
                .setExpirationDuration( Geofence.NEVER_EXPIRE ) // expiring date
                // Transition types that it should look for
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();






        mDoorStatus = (TextView) findViewById(R.id.doorStatus);
        mActionDoor = (Button) findViewById(R.id.actionDoor);


        URL buildUrl = NetworkUtils.buildUrl("getStatus");
        Log.d("MainActivity", buildUrl.toString());
        new queryTask().execute(buildUrl);


        mSocket.on("statusChange", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.d("MainActivity", "statusChange");
                Log.d("MainActivity", args.toString());
                JSONObject obj = (JSONObject)args[0];
                Log.d("MainActivity", obj.toString());

            }

        });

        mSocket.connect();

        // on click
        // makeGithubSearchQuery();
    }



    public void getStatus(View view) {

        URL buildUrl = NetworkUtils.buildUrl("getStatus");
        Log.d("MainActivity", buildUrl.toString());
        new queryTask().execute(buildUrl);
    }

  public void actionDoor(View view) {

        URL buildUrl = NetworkUtils.buildUrl("action");
        Log.d("MainActivity", buildUrl.toString());
        new queryTask().execute(buildUrl);
    }




    public class queryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected void onPreExecute() {

            mDoorStatus.setText("Connecting...");
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
                    //TODO: If request is 'action' Response is result:bool
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
            //      Log.d("onPostExecute", status.toString());
;
                    mDoorStatus.setText(Utils.toTitleCase(status));

                } catch (JSONException e){
                    e.printStackTrace();
                }


            }
        }
    }
}
