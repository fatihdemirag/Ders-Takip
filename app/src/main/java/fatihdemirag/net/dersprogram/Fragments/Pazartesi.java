package fatihdemirag.net.dersprogram.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;
import fatihdemirag.net.dersprogram.Custom_Adapter;
import fatihdemirag.net.dersprogram.DbHelper;
import fatihdemirag.net.dersprogram.Ders;
import fatihdemirag.net.dersprogram.DersEkle;
import fatihdemirag.net.dersprogram.DersNotuEkle;
import fatihdemirag.net.dersprogram.R;


public class Pazartesi extends Fragment {


    ArrayList<Ders> liste=new ArrayList<>();
    ListView listView;
    DbHelper dbHelper;
    Ders ders;
    Custom_Adapter custom_adapter;
    String id;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view=inflater.inflate(R.layout.fragment_pazartesi,container,false);
        listView=(ListView)view.findViewById(R.id.pazartesiListe);
        listView.setDividerHeight(0);


        dbHelper=new DbHelper(getActivity());
        CircleButton circleButton=(CircleButton)view.findViewById(R.id.circle);
        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent=new Intent(getActivity(), DersEkle.class);
                Bundle bundle=new Bundle();
                bundle.putString("Gün","Pazartesi");
                ıntent.putExtras(bundle);
                startActivity(ıntent);
            }
        });
        custom_adapter=new Custom_Adapter(getActivity(),liste);

        custom_adapter.clear();
        Custom_Adapter.KayitYukle("Pazartesi",dbHelper,ders,liste);
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
                        Custom_Adapter.KayitYukle("Pazartesi",dbHelper,ders,liste);
                    }
                }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
                return false;

            }
        });
        return view;

    }

    @Override
    public void onResume() {
        custom_adapter.clear();
        custom_adapter.notifyDataSetChanged();
        Custom_Adapter.KayitYukle("Pazartesi",dbHelper,ders,liste);
        super.onResume();
    }
}