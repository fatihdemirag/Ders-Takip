package fatihdemirag.net.dersprogram.DersNotlariP;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import fatihdemirag.net.dersprogram.CustomAdapters_Listviews.Custom_Adapter2;
import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.Sınıflar.DersNotu;

public class DersNotlari extends Activity {

    Bundle bundle;

    ListView notListesi;

    Button fabButton;

    Animation fabAcilis, fabKapanis;

    ArrayList<DersNotu> dersNotuArrayList=new ArrayList<>();

    String ders;

    Cursor cursor;
    DbHelper dbHelper;

    Custom_Adapter2 custom_adapter2;

    DersNotu dersNotu;

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_notlari);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        fabButton = findViewById(R.id.fabButton);
        fabAcilis = AnimationUtils.loadAnimation(this, R.anim.fab_acilis);
        fabKapanis = AnimationUtils.loadAnimation(this, R.anim.fab_kapanis);

        dbHelper=new DbHelper(this);
        bundle=getIntent().getExtras();
        ders=bundle.getString("Ders");
        getActionBar().setTitle(ders+" Dersinin Notları");

        notListesi = findViewById(R.id.notListesi);
        adView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("47F268874164B56F4CA084A336DE0B42").build();
        adView.loadAd(adRequest);

        custom_adapter2 = new Custom_Adapter2(this, dersNotuArrayList);
        notListesi.setAdapter(custom_adapter2);

        KayitYukle();

        notListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), DersNotuGor.class);
                Bundle bundle=new Bundle();

                TextView secilenNotBaslik=(TextView)(view.findViewById(R.id.konuText));
                TextView secilenNot=(TextView)(view.findViewById(R.id.notText));
                ImageView secilenNotResmi=(ImageView)view.findViewById(R.id.notImage);
                TextView secilenNotId=(TextView)(view.findViewById(R.id.notId));

                bundle.putString("Seçilen Not Id",secilenNotId.getText().toString());

                bundle.putString("Seçilen Başlık",secilenNotBaslik.getText().toString());
                bundle.putString("Seçilen Not",secilenNot.getText().toString());

                DbHelper dbHelper=new DbHelper(getApplicationContext());
                Cursor cursor=dbHelper.ResimBul(secilenNotId.getText().toString());
                while (cursor.moveToNext())
                    dersNotu.setNotResmi(cursor.getBlob(0));
                //Resmi byteArraya dönüştürme kodu
                //
                //Bitmap bitmap=((BitmapDrawable)secilenNotResmi.getDrawable()).getBitmap();
                //ByteArrayOutputStream stream=new ByteArrayOutputStream();
                //bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                //byte[] i=stream.toByteArray();

                bundle.putByteArray("Seçilen Not Resmi",dersNotu.getNotResmi());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
        if (dersNotuArrayList.size() == 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage(ders + " dersinin notu bulunamadı !\nNot Eklensin mi ?");
            alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(DersNotlari.this, DersNotuEkle.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Ders", ders);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Hayır", null);
            alertDialog.show();
        }
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DersNotlari.this, DersNotuEkle.class);

                Bundle bundle = new Bundle();
                bundle.putString("Ders", ders);

                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        notListesi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DersNotlari.this);
                alertDialog.setTitle("Ders Notunu Silmek İstiyor Musunuz ?").setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            dbHelper.NotSilTekli(dersNotuArrayList.get(position).getId());
                            Toast.makeText(DersNotlari.this, "Not Silindi", Toast.LENGTH_SHORT).show();

                            dersNotuArrayList.clear();
                            KayitYukle();
                            custom_adapter2.notifyDataSetChanged();
                        } catch (Exception e) {
                            Toast.makeText(DersNotlari.this, "Not Silinemedi", Toast.LENGTH_SHORT).show();
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
    public void KayitYukle()
    {
        try {
            cursor = dbHelper.dersNotlari(ders);
            while(cursor.moveToNext())
            {
                dersNotu=new DersNotu();
                dersNotu.setDersKonusu(cursor.getString(1));
                dersNotu.setDersNotu(cursor.getString(2));
                dersNotu.setNotResmi(cursor.getBlob(3));
                dersNotu.setDers(cursor.getString(4));
                dersNotu.setId(cursor.getInt(0));
                dersNotu=new DersNotu(dersNotu.getDersKonusu(),dersNotu.getDersNotu(),dersNotu.getNotResmi(),dersNotu.getDers(),dersNotu.getId());
                dersNotuArrayList.add(dersNotu);
            }
            custom_adapter2.notifyDataSetChanged();

        }catch (SQLException e)
        {
            Toast.makeText(getApplicationContext(),"Not Yok",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        dersNotuArrayList.clear();
        KayitYukle();
        custom_adapter2.notifyDataSetChanged();
        super.onResume();
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
