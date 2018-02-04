package fatihdemirag.net.dersprogram.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import fatihdemirag.net.dersprogram.CustomAdapters_Listviews.CardViewAdapterDersProgrami;
import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;
import fatihdemirag.net.dersprogram.Sınıflar.Ders;
import fatihdemirag.net.dersprogram.DersEkle;
import fatihdemirag.net.dersprogram.R;

public class Cuma extends Fragment {

    ArrayList<Ders> liste=new ArrayList<>();
    DbHelper dbHelper;
    Ders ders;

    Button dersEkleButton;
    Button fabButton;

    Button dersEkle, derslerListesi;

    Dialog dialog;

    RecyclerView dersProgramiRecycler;
    RecyclerView.LayoutManager layoutManager;
    CardViewAdapterDersProgrami cardViewAdapterDersProgrami;

    View view;

    EditText dialogDersAdi;
    private AdView adView;

    Animation fabAcilis, fabKapanis, butonlarAcilis, butonlarKapanis;

    boolean fabTiklandi = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cuma, container, false);
        dersProgramiRecycler = view.findViewById(R.id.cumaListe);
        fabButton = view.findViewById(R.id.fabButton);
        dersEkle = view.findViewById(R.id.dersEkle);
        derslerListesi = view.findViewById(R.id.derslerListesi);

        fabAcilis = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_acilis);
        fabKapanis = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_kapanis);
        butonlarAcilis = AnimationUtils.loadAnimation(getActivity(), R.anim.butonlar_acilis);
        butonlarKapanis = AnimationUtils.loadAnimation(getActivity(), R.anim.butonlar_kapanis);

        layoutManager = new LinearLayoutManager(getActivity());
        cardViewAdapterDersProgrami = new CardViewAdapterDersProgrami(getActivity(), liste);

        dersEkleButton = view.findViewById(R.id.dersEkleButton);

        dersProgramiRecycler.setLayoutManager(layoutManager);
        dersProgramiRecycler.setAdapter(cardViewAdapterDersProgrami);

        dbHelper=new DbHelper(getActivity());


        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.activity_ders_ekle);
        dialog.setCancelable(true);
        dialog.setTitle("Ders Ekle");

        dialogDersAdi = dialog.findViewById(R.id.dersAdi);
        adView = dialog.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("47F268874164B56F4CA084A336DE0B42").build();
        adView.loadAd(adRequest);

        dialog.findViewById(R.id.dersEkle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!dialogDersAdi.getText().toString().equals("")) {
                        if (dbHelper.insertData3(dialogDersAdi.getText().toString())) {
                            Toast.makeText(getActivity(), "Ders Eklendi", Toast.LENGTH_SHORT).show();
                            cardViewAdapterDersProgrami.notifyDataSetChanged();

                            fabButton.startAnimation(fabKapanis);
                            dersEkle.startAnimation(butonlarKapanis);
                            derslerListesi.startAnimation(butonlarKapanis);

                            dialog.dismiss();
                        } else
                            Toast.makeText(getActivity(), "Ders Eklenemedi", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Ders Adı Boş Geçilemez", Toast.LENGTH_SHORT).show();

                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Ders Eklenemedi", Toast.LENGTH_SHORT).show();

                }
            }
        });

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fabTiklandi) {
                    fabButton.startAnimation(fabAcilis);
                    dersEkle.startAnimation(butonlarAcilis);
                    derslerListesi.startAnimation(butonlarAcilis);
                    fabTiklandi = !fabTiklandi;
                } else {
                    fabButton.startAnimation(fabKapanis);
                    dersEkle.startAnimation(butonlarKapanis);
                    derslerListesi.startAnimation(butonlarKapanis);
                    fabTiklandi = !fabTiklandi;
                }

            }
        });
        dersEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


        KayitYukle("Cuma", dbHelper, ders, liste);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        dersEkleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ders = new Ders();
                liste.add(ders);
                cardViewAdapterDersProgrami.notifyDataSetChanged();

                editor.putString("gun", "Cuma");
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
        KayitYukle("Cuma", dbHelper, ders, liste);
        super.onResume();
    }
}
