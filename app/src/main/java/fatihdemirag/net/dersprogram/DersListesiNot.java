package fatihdemirag.net.dersprogram;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

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



        dersNotuListesi=(ListView)findViewById(R.id.dersListesi);
        dbHelper=new DbHelper(this);
        cursor= dbHelper.getAllDataT();
        ders=new Ders();
        while (cursor.moveToNext())
        {
            ders.setDersAdi(cursor.getString(0));
            dersler.add(ders.getDersAdi());
        }

        final ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,dersler);
        dersNotuListesi.setAdapter(adapter);

        dersNotuListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent=new Intent(getApplicationContext(),DersNotlari.class);
                bundle.putString("Ders",dersNotuListesi.getItemAtPosition(position)+"");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        dersNotuListesi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(DersListesiNot.this);
                alertDialog.setTitle(dersNotuListesi.getItemAtPosition(position)+" Dersinin Tüm Notlarını Sil").setMessage(dersNotuListesi.getItemAtPosition(position)+" Dersinin Silmek İstediğinize Emin Misiniz").setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            String ders=dersNotuListesi.getItemAtPosition(position).toString();
                            dbHelper.NotSil(ders);
                            Toast.makeText(DersListesiNot.this, "Notlar Silindi", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            dersNotuListesi.setAdapter(adapter);
                        }catch (Exception e)
                        {
                            Toast.makeText(DersListesiNot.this, "Notlar Silinemedi", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
                return false;
            }
        });
    }
}
