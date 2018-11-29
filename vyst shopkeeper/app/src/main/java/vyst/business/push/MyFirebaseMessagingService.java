package vyst.business.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import vyst.business.R;
import vyst.business.activites.LoginActivity;


/**
 * Created by Prerna on 4/3/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String msg = "",title="";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().containsKey("body")) {
            msg= remoteMessage.getData().get("body");
        }
        if (remoteMessage.getData().containsKey("title")) {
            title= remoteMessage.getData().get("title");
        }
        sendMyNotifictaion();

        EventBus.getDefault().post("");


    }

    private void sendMyNotifictaion() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Vyst")
             /*   .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))*/
                .setContentText(title).setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }
}
