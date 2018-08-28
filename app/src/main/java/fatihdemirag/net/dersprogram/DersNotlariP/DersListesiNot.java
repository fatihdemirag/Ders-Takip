package fatihdemirag.net.dersprogram.DersNotlariP;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;
import fatihdemirag.net.dersprogram.Sınıflar.Ders;
import fatihdemirag.net.dersprogram.R;

public class DersListesiNot extends Activity {

    ListView dersNotuListesi;
    ArrayList<String> dersler=new ArrayList<>();
    Cursor cursor;
    DbHelper dbHelper;
    Ders ders;
    Intent intent;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_listesi_not);
        bundle=new Bundle();

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.dersnotlari));

        dersNotuListesi=(ListView)findViewById(R.id.dersListesi);
        dbHelper=new DbHelper(this);
        cursor = dbHelper.dersler();
        ders=new Ders();
        while (cursor.moveToNext())
        {
            ders.setDersAdi(cursor.getString(0));
            dersler.add(ders.getDersAdi());
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dersler);
        dersNotuListesi.setAdapter(adapter);

        dersNotuListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent=new Intent(getApplicationContext(),DersNotlari.class);
                bundle.putString("Ders",dersNotuListesi.getItemAtPosition(position)+"");
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        dersNotuListesi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(DersListesiNot.this);
                alertDialog.setTitle(dersNotuListesi.getItemAtPosition(position) + " " + getString(R.string.dersnotsil)).
                        setMessage(dersNotuListesi.getItemAtPosition(position) + " " + getString(R.string.dersnotsilonay)).
                        setPositiveButton(getString(R.string.evet), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    String ders = dersNotuListesi.getItemAtPosition(position).toString();
                                    dbHelper.NotSil(ders);
                                    Toast.makeText(DersListesiNot.this, getString(R.string.notsilindi), Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                    dersNotuListesi.setAdapter(adapter);
                                } catch (Exception e) {
                                    Toast.makeText(DersListesiNot.this, getString(R.string.notsilinemedi), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

                            }
                        }).setNegativeButton(getString(R.string.hayir), null).show();
                return false;
            }
        });
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
