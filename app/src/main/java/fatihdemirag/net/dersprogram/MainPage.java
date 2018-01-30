package fatihdemirag.net.dersprogram;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import fatihdemirag.net.dersprogram.CustomAdapters_Listviews.Custom_Adapter;
import fatihdemirag.net.dersprogram.DersNotlariP.DersListesiNot;
import fatihdemirag.net.dersprogram.Sınıflar.Ders;

public class MainPage extends Activity {
    Calendar calendar;
    Cursor cursor;
    Ders ders;
    private static ListView liste;
    ArrayList<Ders> dersListe = new ArrayList<>();
    DbHelper dbHelper;
    private static TextView gunText;
    Custom_Adapter custom_adapter;

    Intent intent;

    Button dersProgrami, notlar;

    SwipeRefreshLayout yenile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        liste = (ListView) findViewById(R.id.gunlukListe);
        gunText = (TextView) findViewById(R.id.gunText);
        dersProgrami = findViewById(R.id.dersProgrami);
        notlar = findViewById(R.id.notlar);
        yenile = findViewById(R.id.yenile);


        dbHelper = new DbHelper(this);

        DersleriYukle();

        dersProgrami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainPage.this, HaftalikDersler.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        notlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainPage.this, DersListesiNot.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        yenile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DersleriYukle();
                custom_adapter.notifyDataSetChanged();
                yenile.setRefreshing(false);
            }
        });
    }

    void DersleriYukle() {
        calendar = Calendar.getInstance();
        int gun = calendar.get(Calendar.DAY_OF_WEEK);
        switch (gun) {
            case Calendar.MONDAY:
                GunlukDers("Pazartesi");
                gunText.setText("Pazartesi");
                break;
            case Calendar.TUESDAY:
                GunlukDers("Salı");
                gunText.setText("Salı");
                break;
            case Calendar.WEDNESDAY:
                GunlukDers("Çarşamba");
                gunText.setText("Çarşamba");
                break;
            case Calendar.THURSDAY:
                GunlukDers("Perşembe");
                gunText.setText("Perşembe");
                break;
            case Calendar.FRIDAY:
                GunlukDers("Cuma");
                gunText.setText("Cuma");
                break;
            case Calendar.SATURDAY:
                GunlukDers("Cumartesi");
                gunText.setText("Cumartesi");
                break;
            case Calendar.SUNDAY:
                GunlukDers("Pazar");
                gunText.setText("Pazar");
                break;
        }
    }

    public void GunlukDers(String gun) {
        dersListe.clear();
        cursor = dbHelper.getAllData();
        while (cursor.moveToNext()) {
            if (cursor.getString(2).equals(gun)) {
                ders = new Ders();
                ders.setDersAdi(cursor.getString(1));
                ders.setDersBaslangicSaati(cursor.getString(3));
                ders.setDersBitisSaati(cursor.getString(4));
                ders.setDersId((cursor.getInt(0)));
                ders = new Ders(ders.getDersAdi(), ders.getDersBaslangicSaati(), ders.getDersBitisSaati(), ders.getDersId());
                dersListe.add(ders);
            }
        }
        custom_adapter = new Custom_Adapter(this, dersListe);

        liste.setAdapter(custom_adapter);
        custom_adapter.notifyDataSetChanged();
    }

}
