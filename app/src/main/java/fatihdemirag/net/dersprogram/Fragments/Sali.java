package fatihdemirag.net.dersprogram.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import fatihdemirag.net.dersprogram.CustomAdapters_Listviews.CardViewAdapterDersProgrami;
import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;
import fatihdemirag.net.dersprogram.Sınıflar.Ders;
import fatihdemirag.net.dersprogram.R;


public class Sali extends Fragment {

    ArrayList<Ders> liste=new ArrayList<>();
    DbHelper dbHelper;
    Ders ders;

    Button dersEkleButton;

    RecyclerView dersProgramiRecycler;
    RecyclerView.LayoutManager layoutManager;
    CardViewAdapterDersProgrami cardViewAdapterDersProgrami;

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sali, container, false);
        dersProgramiRecycler = view.findViewById(R.id.saliListe);

        layoutManager = new LinearLayoutManager(getActivity());
        cardViewAdapterDersProgrami = new CardViewAdapterDersProgrami(getActivity(), liste);

        dersEkleButton = view.findViewById(R.id.dersEkleButton);

        dersProgramiRecycler.setLayoutManager(layoutManager);
        dersProgramiRecycler.setAdapter(cardViewAdapterDersProgrami);

        dbHelper=new DbHelper(getActivity());

        KayitYukle("Salı", dbHelper, ders, liste);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        dersEkleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liste.clear();
                KayitYukle("Salı", dbHelper, ders, liste);

                ders = new Ders();
                liste.add(ders);
                cardViewAdapterDersProgrami.notifyDataSetChanged();

                editor.putString("gun", "Salı");
                editor.commit();

            }
        });

        return view;
    }

    public void KayitYukle(String gun, DbHelper dbHelper, Ders ders, ArrayList<Ders> liste) {
        liste.clear();
        Cursor cursor = dbHelper.getAllData();
        while (cursor.moveToNext()) {
            if (cursor.getString(2).equals(gun)) {
                ders = new Ders();
                ders.setDersAdi(cursor.getString(1));
                ders.setDersBaslangicSaati(cursor.getString(3));
                ders.setDersBitisSaati(cursor.getString(4));
                ders.setDersId((cursor.getInt(0)));
                ders = new Ders(ders.getDersAdi(), gun, ders.getDersBaslangicSaati(), ders.getDersBitisSaati(), ders.getDersId(), ders.getDersPozisyon());
                liste.add(ders);
            }
        }
        cardViewAdapterDersProgrami.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        cardViewAdapterDersProgrami.notifyDataSetChanged();
        KayitYukle("Salı", dbHelper, ders, liste);
        super.onResume();
    }
}
