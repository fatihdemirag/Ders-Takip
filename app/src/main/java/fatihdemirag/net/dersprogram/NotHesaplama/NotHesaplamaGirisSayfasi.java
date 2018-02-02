package fatihdemirag.net.dersprogram.NotHesaplama;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import fatihdemirag.net.dersprogram.R;

public class NotHesaplamaGirisSayfasi extends Activity {

    Button liseHesap, universiteHesap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_hesaplama_giris_sayfasi);

        liseHesap = findViewById(R.id.liseHesap);
        universiteHesap = findViewById(R.id.universiteHesap);


        liseHesap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotHesaplamaGirisSayfasi.this, LiseNotHesap.class);
                startActivity(intent);
            }
        });

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
