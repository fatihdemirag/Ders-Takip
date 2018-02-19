package fatihdemirag.net.dersprogram;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;

/**
 * Created by fxd on 15.02.2018.
 */

public class BildirimServisi extends Service {
    public BildirimServisi() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    int sayac = 0;

    @Override
    public void onCreate() {
        Log.e("servis", "Servis Başladı");

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                BildirimGonder();
                DersKontrol();

                sayac++;
                if (sayac == 5)
                    timer.cancel();
            }
        };
        timer.schedule(timerTask, 0, 1000);


        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("servis", "Servis Durdu");
        super.onDestroy();
    }

    void BildirimGonder() {
        Calendar calendar = Calendar.getInstance();
        Log.e("takvim", calendar.getTime().getMinutes() + "");

        Intent i = new Intent(this, MainPage.class); // Bildirime basıldığında hangi aktiviteye gidilecekse
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setAutoCancel(true) // Kullanıcı bildirime girdiğinde otomatik olarak silinsin. False derseniz bildirim kalıcı olur.
                .setContentTitle("Birazdan ders başlayacak") // Bildirim başlığı
                .setContentText(calendar.getTime().getHours() + "") // Bildirim mesajı
                .setSmallIcon(R.drawable.homework)// Bildirim simgesi
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());

        int bildirimDakikasi = 0;
        int bildirimSaati = Integer.parseInt(simdikiSaat.substring(0, simdikiSaat.indexOf(':')));

        if (Integer.parseInt(simdikiSaat.substring(simdikiSaat.indexOf(':') + 1, simdikiSaat.length())) + bildirimSuresi >= 60) {
            bildirimDakikasi = 60 - (Integer.parseInt(simdikiSaat.substring(simdikiSaat.indexOf(':') + 1, simdikiSaat.length())) + bildirimSuresi);
            bildirimSaati += 1;
        } else
            bildirimDakikasi = Integer.parseInt(simdikiSaat.substring(simdikiSaat.indexOf(':') + 1, simdikiSaat.length())) + bildirimSuresi;

//        System.out.println(bildirimSaati+":"+bildirimDakikasi);


//        System.out.println("İki nokta sol :"+str.substring(0,str.indexOf(':')));
//        System.out.println("İki nokta sağ :"+str.substring(str.indexOf(':')+1,str.length()));

    }

    String simdikiSaat = "8:0";
    int bildirimSuresi = 5;
    int saat = Integer.parseInt(simdikiSaat.substring(0, simdikiSaat.indexOf(':')));
    int dakika = Integer.parseInt(simdikiSaat.substring(simdikiSaat.indexOf(':') + 1, simdikiSaat.length()));
    int bildirimZamani = Integer.parseInt(simdikiSaat.substring(simdikiSaat.indexOf(':') + 1, simdikiSaat.length())) - bildirimSuresi;

    void DersKontrol() {
        DbHelper dbHelper = new DbHelper(this);
        Cursor cursor = dbHelper.dersKontrol(simdikiSaat);
        while (cursor.moveToNext()) {
            Log.e("kontrol", cursor.getString(3));
        }
    }
}