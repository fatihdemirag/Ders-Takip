package fatihdemirag.net.dersprogram;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

//fxd
public class Ayarlar extends Activity {

    TimePicker dersBaslangicSaati;
    NumberPicker dersSuresi;
    Button ayarKaydet;

    Switch bildirim;

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
        bildirim = findViewById(R.id.bildirim);

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


        if (sharedPreferences.getString("bildirim", "").equals("1"))
            bildirim.setChecked(true);
        else
            bildirim.setChecked(false);

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
                editor.apply();
                Toast.makeText(Ayarlar.this, "Ayarlar Kaydedildi", Toast.LENGTH_SHORT).show();
            }
        });

        bildirim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putString("bildirim", "1");
                    editor.apply();
                    Intent servisIntent = new Intent(Ayarlar.this, BildirimServisi.class);

                    startService(servisIntent);

                    Toast.makeText(Ayarlar.this, "Ders başlangıcından 5 dakika önce bildirim gönderilecektir.", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("bildirim", "0");
                    editor.apply();
                    Intent servisIntent = new Intent(Ayarlar.this, BildirimServisi.class);

                    stopService(servisIntent);
                }
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
