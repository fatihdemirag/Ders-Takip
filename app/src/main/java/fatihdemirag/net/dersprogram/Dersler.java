package fatihdemirag.net.dersprogram;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;

public class Dersler extends Activity {

    ListView derslerListesi;

    Button dersEkleButton;


    EditText dialogDersAdi;

    private AdView adView;

    Animation fabAcilis, fabKapanis;

    Dialog dialog;

    ArrayList<String> dersArrayList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    Cursor cursor;
    DbHelper dbHelper;

    boolean fabTiklandi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dersler);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        derslerListesi = findViewById(R.id.derslerListesi);
        dersEkleButton = findViewById(R.id.dersEkleButton);

        fabAcilis = AnimationUtils.loadAnimation(this, R.anim.fab_acilis);
        fabKapanis = AnimationUtils.loadAnimation(this, R.anim.fab_kapanis);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dersArrayList);
        derslerListesi.setAdapter(arrayAdapter);

        dbHelper = new DbHelper(this);
        cursor = dbHelper.getAllData3();

        KayitYukle();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_ders_ekle);
        dialog.setCancelable(true);
        dialog.setTitle("Ders Ekle");

        dialogDersAdi = dialog.findViewById(R.id.dersAdi);
        adView = dialog.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("47F268874164B56F4CA084A336DE0B42").build();
        adView.loadAd(adRequest);

        dersEkleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.findViewById(R.id.dersEkle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (!dialogDersAdi.getText().toString().equals("")) {
                                if (dbHelper.insertData3(dialogDersAdi.getText().toString())) {

                                    dersEkleButton.startAnimation(fabKapanis);

                                    dialog.dismiss();
                                    KayitYukle();

                                    dersArrayList.add(dialogDersAdi.getText().toString());

                                    Toast.makeText(Dersler.this, "Ders Eklendi", Toast.LENGTH_SHORT).show();

                                } else
                                    Toast.makeText(Dersler.this, "Ders Eklenemedi", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(Dersler.this, "Ders Adı Boş Geçilemez", Toast.LENGTH_SHORT).show();

                        } catch (SQLException e) {
                            e.printStackTrace();
                            Toast.makeText(Dersler.this, "Ders Eklenemedi", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                dersEkleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!fabTiklandi) {
                            dersEkleButton.startAnimation(fabAcilis);
                            dialog.show();
                            fabTiklandi = !fabTiklandi;

                        } else {
                            dersEkleButton.startAnimation(fabKapanis);
                            fabTiklandi = !fabTiklandi;
                        }

                    }
                });


            }
        });
    }

    void KayitYukle() {
        while (cursor.moveToNext()) {
            dersArrayList.add(cursor.getString(0));
        }
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
