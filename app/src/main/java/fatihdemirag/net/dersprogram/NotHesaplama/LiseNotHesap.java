package fatihdemirag.net.dersprogram.NotHesaplama;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;
import fatihdemirag.net.dersprogram.R;

public class LiseNotHesap extends Activity {

    ListView dersListesi;

    ArrayList<String> dersArrayList = new ArrayList<>();
    ArrayAdapter<String> dersAdapter;

    DbHelper dbHelper;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lise_not_hesap);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        dersListesi = findViewById(R.id.dersListesi);

        dersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dersArrayList);
        dersListesi.setAdapter(dersAdapter);

        dbHelper = new DbHelper(this);
        cursor = dbHelper.getAllDataT();

        while (cursor.moveToNext()) {
            dersArrayList.add(cursor.getString(0));
        }
        dersAdapter.notifyDataSetChanged();

        dersListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LiseNotHesap.this, LiseNotEkle.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
