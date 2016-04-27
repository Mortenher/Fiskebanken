package com.example.morten.fiskebanken.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.morten.fiskebanken.R;
import com.example.morten.fiskebanken.activities.RegisterActivity;

/**
 * Created by Morten on 25.04.2016.
 */
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        showNotification(context);
    }
    protected void showNotification(final Context context){
        final Intent intent = new Intent(context, RegisterActivity.class);

            //Notification klasse som sier hva som skal stå, vises og lyden som skal lages.
            PendingIntent pi = PendingIntent.getActivity(context, 1, intent, 0);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.fishicon)
                    .setContentTitle("Fiskepåminnelse")
                    .setContentText("Lenge siden du har registrert noe, har du fisket i det siste?");

            mBuilder.setContentIntent(pi);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setAutoCancel(true);



            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());



    }
}
