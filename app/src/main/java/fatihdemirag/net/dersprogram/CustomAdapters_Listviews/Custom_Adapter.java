package fatihdemirag.net.dersprogram.CustomAdapters_Listviews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fatihdemirag.net.dersprogram.DbHelper;
import fatihdemirag.net.dersprogram.Sınıflar.Ders;
import fatihdemirag.net.dersprogram.DersNotlariP.DersNotuEkle;
import fatihdemirag.net.dersprogram.R;

/**
 * Created by fxd on 07.07.2017.
 */


public class Custom_Adapter extends ArrayAdapter<Ders>{

    Context context;
    ArrayList<Ders> derslerArrayList;
    LayoutInflater layoutInflater;
    TextView dersId,idAlert;
    public static Cursor cursor;
    Dialog dialog;
    EditText dersAd,dersGiris,dersCikis;
    DbHelper dbHelper;

    public Custom_Adapter(Context context, ArrayList<Ders> derslerArrayList) {
        super(context, R.layout.activity_custom__list_view, derslerArrayList);
        this.context=context;
        this.derslerArrayList=derslerArrayList;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        layoutInflater=(LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view=layoutInflater.inflate(R.layout.activity_custom__list_view,parent,false);
        dbHelper=new DbHelper(getContext());

        final TextView dersAdi=(TextView)view.findViewById(R.id.ders);
        TextView baslangic=(TextView)view.findViewById(R.id.baslangicSaat);
        TextView bitis=(TextView)view.findViewById(R.id.bitisSaat);
        dersId=(TextView)view.findViewById(R.id.dersId);

        dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.activity_custom__alert_dialog);
        dialog.setTitle(R.string.duzenle);

        idAlert=(TextView)dialog.findViewById(R.id.idAlert);
        dersAd=(EditText)dialog.findViewById(R.id.dersAdi);
        dersGiris=(EditText)dialog.findViewById(R.id.dersGiris);
        dersCikis=(EditText)dialog.findViewById(R.id.dersCikis);


        final Ders ders=derslerArrayList.get(position);
        dersAdi.setText(ders.getDersAdi());
        baslangic.setText(ders.getDersBaslangicSaati());
        bitis.setText(ders.getDersBitisSaati());
        dersId.setText(ders.getDersId()+"");

        ImageView edit=(ImageView)view.findViewById(R.id.editIcon);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idAlert.setText(ders.getDersId()+"");
                dersAd.setText(ders.getDersAdi()+"");
                dersGiris.setText(ders.getDersBaslangicSaati()+"");
                dersCikis.setText(ders.getDersBitisSaati()+"");
                dialog.show();
            }
        });
        dialog.findViewById(R.id.kaydetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean guncel=dbHelper.updateData(Integer.parseInt(idAlert.getText().toString()),dersAd.getText().toString()
                        ,dersGiris.getText().toString(),dersCikis.getText().toString());
                if(guncel)
                    Toast.makeText(getContext(),"Ders Güncellendi",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(),"Ders Güncellenemedi",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.findViewById(R.id.cikButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ImageView dersNotuEkleImage=(ImageView)view.findViewById(R.id.dersNotuEkleImage);
        dersNotuEkleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),DersNotuEkle.class);
                Bundle bundle=new Bundle();
                bundle.putString("Ders",ders.getDersAdi());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return view;
    }
    public boolean KayitSil(String id)
    {
        DbHelper dbHelper=new DbHelper(getContext());
        int delete=dbHelper.deleteData(id);
        if (delete>0) {
            Toast.makeText(getContext(), "Kayıt Silindi", Toast.LENGTH_SHORT).show();
            View view=layoutInflater.inflate(R.layout.activity_custom__list_view,null);
            view.invalidate();
            notifyDataSetChanged();


            return true;
        }
        else {
            Toast.makeText(getContext(), "Kayıt Silinemedi", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static void KayitYukle(String gun,DbHelper dbHelper,Ders ders,ArrayList<Ders> liste)
    {
        cursor= dbHelper.getAllData();
        while (cursor.moveToNext())
        {
            if (cursor.getString(2).equals(gun)) {
                ders=new Ders();
                ders.setDersAdi(cursor.getString(1));
                ders.setDersBaslangicSaati(cursor.getString(3));
                ders.setDersBitisSaati(cursor.getString(4));
                ders.setDersId((cursor.getInt(0)));
                ders=new Ders(ders.getDersAdi(),ders.getDersBaslangicSaati(),ders.getDersBitisSaati(),ders.getDersId());
                liste.add(ders);
            }
        }
    }
}
