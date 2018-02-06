package fatihdemirag.net.dersprogram;

import android.app.ActionBar;
import android.app.Activity;
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

//fxd
public class Ayarlar extends Activity {

    TimePicker dersBaslangicSaati;
    NumberPicker dersSuresi, tenefusSuresi;
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
        tenefusSuresi = findViewById(R.id.tenefusSuresi);
        ayarKaydet = findViewById(R.id.ayarKaydet);

        dersBaslangicSaati.setIs24HourView(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        if (!sharedPreferences.getString("dersBaslangicSaati", "").equals("") && !sharedPreferences.getString("dersBaslangicDakikasi", "").equals("")) {
            dersBaslangicSaati.setHour(Integer.parseInt(sharedPreferences.getString("dersBaslangicSaati", "")));
            dersBaslangicSaati.setMinute(Integer.parseInt(sharedPreferences.getString("dersBaslangicDakikasi", "")));
        }
        dersSuresi.setMinValue(10);
        dersSuresi.setMaxValue(100);
        if (!sharedPreferences.getString("dersSuresi", "").equals(""))
            dersSuresi.setValue(Integer.parseInt(sharedPreferences.getString("dersSuresi", "")));

        tenefusSuresi.setMinValue(5);
        tenefusSuresi.setMaxValue(30);
        if (!sharedPreferences.getString("tenefusSuresi", "").equals(""))
            tenefusSuresi.setValue(Integer.parseInt(sharedPreferences.getString("tenefusSuresi", "")));

        ayarKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dersBaslangicSaati.getHour() < 10)
                    editor.putString("dersBaslangicSaati", "0" + String.valueOf(dersBaslangicSaati.getHour()));
                else
                    editor.putString("dersBaslangicSaati", String.valueOf(dersBaslangicSaati.getHour()));
                if (dersBaslangicSaati.getMinute() < 10)
                    editor.putString("dersBaslangicDakikasi", "0" + String.valueOf(dersBaslangicSaati.getMinute()));
                else
                    editor.putString("dersBaslangicDakikasi", String.valueOf(dersBaslangicSaati.getMinute()));
                editor.putString("dersSuresi", String.valueOf(dersSuresi.getValue()));
                editor.putString("tenefusSuresi", String.valueOf(tenefusSuresi.getValue()));
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
