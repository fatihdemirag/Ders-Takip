package fatihdemirag.net.dersprogram;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.TimePicker;

//fxd
public class Ayarlar extends Activity {

    TimePicker dersBaslangicSaati;
    NumberPicker dersSuresi, tenefusSuresi;


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

        dersBaslangicSaati.setIs24HourView(true);

        dersBaslangicSaati.setHour(10);
        dersBaslangicSaati.setMinute(50);

        dersSuresi.setMinValue(0);
        dersSuresi.setMaxValue(100);

        tenefusSuresi.setMinValue(0);
        tenefusSuresi.setMaxValue(30);
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
