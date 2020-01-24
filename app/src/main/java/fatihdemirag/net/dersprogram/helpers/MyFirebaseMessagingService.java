package fatihdemirag.net.dersprogram.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;
import fatihdemirag.net.dersprogram.MainActivity;
import fatihdemirag.net.dersprogram.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Bildirim(remoteMessage.getNotification().getBody());
        super.onMessageReceived(remoteMessage);

    }
    private void Bildirim(String mesaj)
    {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(mesaj)
                .setSmallIcon(R.mipmap.ic_icon)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }
}