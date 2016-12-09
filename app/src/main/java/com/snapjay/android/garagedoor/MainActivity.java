package com.snapjay.android.garagedoor;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.Geofence;
import com.snapjay.android.garagedoor.utilites.NetworkUtils;
import com.snapjay.android.garagedoor.utilites.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.net.URL;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private TextView mDoorStatus;
    private Button mActionDoor;

    private Socket mSocket;
    {
        try {
             mSocket = IO.socket("http://door.snapjay.com:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // https://code.tutsplus.com/tutorials/how-to-work-with-geofences-on-android--cms-26639
//        Geofence geoFence = new Geofence.Builder()
//                .setRequestId("door") // Geofence ID
//                .setCircularRegion( 43.958084, -79.354174, 150) // defining fence region
//                .setExpirationDuration( Geofence.NEVER_EXPIRE ) // expiring date
//                // Transition types that it should look for
//                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT )
//                .build();

        mDoorStatus = (TextView) findViewById(R.id.doorStatus);
        mActionDoor = (Button) findViewById(R.id.actionDoor);


        mSocket.connect();
        mSocket.on("statusChange", ioStatusChange);

        getStatus();
    }

// SAMPLE:  /getStatus Request
// {error: null, status: "closed"}
    public void getStatus() {
        URL getStatusURL = NetworkUtils.buildUrl("getStatus");

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, getStatusURL.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mDoorStatus.setText(Utils.toTitleCase(response.getString("status")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("getStatus", error.getMessage());
                    }
                });

        queue.add(jsObjRequest);

    }

//  SAMPLE:  /action Request
//  {error: null, result: true}

  public void actionDoor(View view) {

      URL getStatusURL = NetworkUtils.buildUrl("action");

      RequestQueue queue = Volley.newRequestQueue(this);

      JsonObjectRequest jsObjRequest = new JsonObjectRequest
              (Request.Method.GET, getStatusURL.toString(), null, new Response.Listener<JSONObject>() {

                  @Override
                  public void onResponse(JSONObject response) {
                      try {
                          Log.d("getStatus", response.getBoolean("result") ? "true" : "false");
                          response.getBoolean("result");
                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                  }
              }, new Response.ErrorListener() {

                  @Override
                  public void onErrorResponse(VolleyError error) {
                      Log.e("getStatus", error.getMessage());

                  }
              });

      queue.add(jsObjRequest);

  }



    private Emitter.Listener ioStatusChange = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    String status;
                    try {
                    status = data.getString("status");
                        mDoorStatus.setText(Utils.toTitleCase(status));
                        Log.d("ioStatusChange", status);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

}
