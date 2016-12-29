package com.snapjay.android.garagedoor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.snapjay.android.garagedoor.utilites.NetworkUtils;
import com.snapjay.android.garagedoor.utilites.Theme;
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
    public static final String OI_EVENT_STATUS_CHANGE = "statusChange";
    public static final String OI_EVENT_ALERT = "alert";
    public static final String OI_ALERT_DOOR_OPEN = "DOOR_OPEN";
    public static final String OI_ALERT_DOOR_TRANSITION = "DOOR_TRANSITION";


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
        Theme.onActivityCreateSetTheme(this);
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
        mSocket.on(OI_EVENT_STATUS_CHANGE, ioStatusChange);
        mSocket.on(OI_EVENT_ALERT, ioAlert);

        getStatus();
    }


    public void toggleTheme(View view){
        Theme.toggle(this);
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

                        mDoorStatus.setText("No Internet Connection");
                        Log.e("getStatus", error.getMessage());
                    }
                });

        queue.add(jsObjRequest);

    }


    public void getStatus(View view){
        getStatus();
    }

    public void notify(View view){

        displayNotify(OI_ALERT_DOOR_OPEN, 15*60 );
        displayNotify(OI_ALERT_DOOR_TRANSITION, 30);

    }

    public int NOTIFICATION_ID;
    public void displayNotify(String status, int time){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //icon appears in device notification bar and right hand corner of notification
        builder.setSmallIcon(R.drawable.ic_speaker_dark);

        // This intent is fired when notification is clicked
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Large icon appears on the left of the notification
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        // Content title, which appears in large type at the top of the notification

        builder.setContentTitle(getString(R.string.app_name));
        builder.setSubText(getString(R.string.notifyWarning));
        builder.setAutoCancel(true);
        builder.setVibrate(new long[] {1000, 1000, 1000});
        builder.setLights(Color.RED, 3000, 3000);
        builder.setDefaults(Notification.DEFAULT_SOUND);


        if (status.equals(OI_ALERT_DOOR_OPEN)) {
            builder.setContentText(String.format(getString(R.string.notifyDOOR_OPEN), (time/60)));
            NOTIFICATION_ID = 1;
        } else if (status.equals(OI_ALERT_DOOR_TRANSITION)) {
            builder.setContentText(String.format(getString(R.string.notifyDOOR_TRANSITION), time));
            NOTIFICATION_ID = 2;
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        //notificationManager.cancel(NOTIFICATION_ID);
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

    private Emitter.Listener ioAlert = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    String status;
                    Integer time;
                    try {
                        status = data.getString("status");
                        time = data.getInt("time");

                        Log.d("ioAlert", status);
                        displayNotify(status, time);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

}
