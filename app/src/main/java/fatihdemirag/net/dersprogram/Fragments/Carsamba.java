package fatihdemirag.net.dersprogram.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fatihdemirag.net.dersprogram.CustomAdapters_Listviews.Custom_Adapter;
import fatihdemirag.net.dersprogram.DbHelper;
import fatihdemirag.net.dersprogram.Sınıflar.Ders;
import fatihdemirag.net.dersprogram.DersEkle;
import fatihdemirag.net.dersprogram.R;


public class Carsamba extends Fragment {

    ArrayList<Ders> liste=new ArrayList<>();
    ListView listView;
    DbHelper dbHelper;
    Ders ders;
    String id;
    Custom_Adapter custom_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_carsamba,container,false);
        listView=(ListView)view.findViewById(R.id.carsambaListe);
        listView.setDividerHeight(0);
        dbHelper=new DbHelper(getActivity());

        custom_adapter=new Custom_Adapter(getActivity(),liste);
        Button circleButton = view.findViewById(R.id.circle);
        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent=new Intent(getActivity(), DersEkle.class);
                Bundle bundle=new Bundle();
                bundle.putString("Gün","Çarşamba");
                ıntent.putExtras(bundle);
                startActivity(ıntent);
            }
        });
        custom_adapter.clear();
        Custom_Adapter.KayitYukle("Çarşamba",dbHelper,ders,liste);
        listView.setAdapter(custom_adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                id=((TextView)view.findViewById(R.id.dersId)).getText().toString();
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Dersi Sil").setMessage("Dersi Silmek İstediğinize Emin Misiniz").setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //getActivity().recreate();

                        custom_adapter.KayitSil(id);
                        custom_adapter.clear();
                        custom_adapter.notifyDataSetChanged();
                        Custom_Adapter.KayitYukle("Çarşamba",dbHelper,ders,liste);
                    }
                }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
                return false;
            }
        });
        listView.setAdapter(custom_adapter);

        return view;
    }
    @Override
    public void onResume() {
        custom_adapter.clear();
        custom_adapter.notifyDataSetChanged();
        Custom_Adapter.KayitYukle("Çarşamba",dbHelper,ders,liste);
        super.onResume();
    }
}
