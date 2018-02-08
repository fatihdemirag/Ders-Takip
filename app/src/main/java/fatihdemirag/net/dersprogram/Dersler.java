package fatihdemirag.net.dersprogram;

import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;

public class Dersler extends Activity {

    ListView derslerListesi;

    ArrayList<String> dersArrayList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    Cursor cursor;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dersler);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        derslerListesi = findViewById(R.id.derslerListesi);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dersArrayList);
        derslerListesi.setAdapter(arrayAdapter);

        dbHelper = new DbHelper(this);
        cursor = dbHelper.getAllData3();

        KayitYukle();
    }

    void KayitYukle() {
        while (cursor.moveToNext()) {
            dersArrayList.add(cursor.getString(0));
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
