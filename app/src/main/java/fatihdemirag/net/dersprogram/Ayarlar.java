package fatihdemirag.net.dersprogram;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;

//fxd
public class Ayarlar extends Activity {

    TimePicker dersBaslangicSaati;
    NumberPicker dersSuresi;
    Button ayarKaydet;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dersBaslangicSaati = findViewById(R.id.dersBaslangicSaati);
        dersSuresi = findViewById(R.id.dersSuresi);
        ayarKaydet = findViewById(R.id.ayarKaydet);

        dersBaslangicSaati.setIs24HourView(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        if (!sharedPreferences.getString("dersBaslangicSaati", "").equals("") && !sharedPreferences.getString("dersBaslangicDakikasi", "").equals("")) {
            dersBaslangicSaati.setCurrentHour(Integer.parseInt(sharedPreferences.getString("dersBaslangicSaati", "")));
            dersBaslangicSaati.setCurrentMinute(Integer.parseInt(sharedPreferences.getString("dersBaslangicDakikasi", "")));
        } else {
            dersBaslangicSaati.setCurrentHour(8);
            dersBaslangicSaati.setCurrentMinute(0);
        }
        dersSuresi.setMinValue(30);
        dersSuresi.setMaxValue(50);
        if (!sharedPreferences.getString("dersSuresi", "").equals(""))
            dersSuresi.setValue(Integer.parseInt(sharedPreferences.getString("dersSuresi", "")));


        ayarKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dersBaslangicSaati.getCurrentHour() < 10)
                    editor.putString("dersBaslangicSaati", "0" + String.valueOf(dersBaslangicSaati.getCurrentHour()));
                else
                    editor.putString("dersBaslangicSaati", String.valueOf(dersBaslangicSaati.getCurrentHour()));
                if (dersBaslangicSaati.getCurrentMinute() < 10)
                    editor.putString("dersBaslangicDakikasi", "0" + String.valueOf(dersBaslangicSaati.getCurrentMinute()));
                else
                    editor.putString("dersBaslangicDakikasi", String.valueOf(dersBaslangicSaati.getCurrentMinute()));
                editor.putString("dersSuresi", String.valueOf(dersSuresi.getValue()));
                editor.commit();
                Toast.makeText(Ayarlar.this, "Ayarlar Kaydedildi", Toast.LENGTH_SHORT).show();
            }
        });

//        String str="7:0";
//        System.out.println("İki nokta : "+str.indexOf(':'));
//        System.out.println("İki nokta sol :"+str.substring(0,str.indexOf(':')));
//        System.out.println("İki nokta sağ :"+str.substring(str.indexOf(':')+1,str.length()));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
