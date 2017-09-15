package fatihdemirag.net.dersprogram;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DersNotlari extends Activity {

    Bundle bundle;
    ListView notListesi;
    ArrayList<DersNotu> dersNotuArrayList=new ArrayList<>();
    String ders;
    Cursor cursor;
    DbHelper dbHelper;
    DersNotu dersNotu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_notlari);

        dbHelper=new DbHelper(this);
        bundle=getIntent().getExtras();
        ders=bundle.getString("Ders");
        getActionBar().setTitle(ders+" Dersinin Notları");

        Toast.makeText(getApplicationContext(),bundle.getString("Ders"),Toast.LENGTH_SHORT).show();
        notListesi=(ListView)findViewById(R.id.notListesi);

        KayitYukle();

        Custom_Adapter2 custom_adapter2=new Custom_Adapter2(this,dersNotuArrayList);
        notListesi.setAdapter(custom_adapter2);

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
            }
        });
    }
    public void KayitYukle()
    {
        try {
            cursor= dbHelper.getAllData2(ders);
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
        }catch (SQLException e)
        {
            Toast.makeText(getApplicationContext(),"Not Yok",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
