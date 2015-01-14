package com.hintdesk.Twitter_oAuth.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.hintdesk.Twitter_oAuth.Main.MainActivity;
import com.hintdesk.Twitter_oAuth.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Christopher Stokes on 2014-07-17.
 */
public class MyService extends Service {
    private static final String TAG = "MyService";

    private Timer myTimer;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {

        Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");
        System.out.println("onCreate MyService");
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("in timer run");
                TimerMethod();
            }

        }, 0, 1000);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
    }

    private void TimerMethod() {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
//        this.runOnUiThread(Timer_Tick);
        System.out.println("in timer method");
        //showNotification(Long.toString(System.currentTimeMillis()));
    }

    private void showNotification(String sub){
        System.out.println("showing notifactation");
        final String subject = sub;
        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent(MyService.this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(MyService.this, 0, intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)

                .setContentTitle(subject)
                .setContentText("Here's an awesome update for you!")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setSound(soundUri)


                .addAction(R.drawable.ic_launcher, "View", pIntent)
                .addAction(0, "Remind", pIntent)

                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);
    }
}
