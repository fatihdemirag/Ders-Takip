package fatihdemirag.net.dersprogram.CustomAdapters_Listviews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;
import fatihdemirag.net.dersprogram.DersNotlariP.DersNotuEkle;
import fatihdemirag.net.dersprogram.Dersler;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.Sınıflar.Ders;

/**
 * Created by fxd on 01.02.2018.
 */

public class CardViewAdapterDersProgrami extends RecyclerView.Adapter<CardViewAdapterDersProgrami.ViewHolder> {
    private Context context;
    private ArrayList<Ders> liste;
    private View view;
    private ViewHolder viewHolder;

    public CardViewAdapterDersProgrami(Context context, ArrayList<Ders> liste) {
        this.context = context;
        this.liste = liste;
    }


    @Override
    public CardViewAdapterDersProgrami.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.activity_custom__list_view, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CardViewAdapterDersProgrami.ViewHolder holder, final int position) {
        holder.baslangicSaati.setText(liste.get(position).getDersBaslangicSaati());
        holder.bitisSaati.setText(liste.get(position).getDersBitisSaati());
        holder.dersId.setText(liste.get(position).getDersId() + "");
        holder.ders.setText(liste.get(position).getDersAdi());
        holder.dersler.setSelection(liste.get(position).getDersPozisyon());
        if (liste.get(position).getDersTenefusSuresi() != null)
            holder.tenefusSuresiNumberPicker.setValue(Integer.parseInt(liste.get(position).getDersTenefusSuresi()));
        holder.tenefusSuresiNumberPicker.setEnabled(liste.get(position).isTenefusAktifMi());
        holder.onayButton.setText(liste.get(position).getButonYazisi());
        holder.sira.setText(liste.get(position).getSira() + "");
        holder.onayButton.setVisibility(liste.get(position).getOnayAktifMi());
        holder.dersNotuEkle.setVisibility(liste.get(position).getNotEkleAktifMi());
        holder.tenefusSuresiBaslik.setText(liste.get(position).getTenefusSuresiBaslik());
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView baslangicSaati, bitisSaati, dersId, ders, sira, tenefusSuresiBaslik;
        Button dersNotuEkle, onayButton;

        Spinner dersler;

        NumberPicker tenefusSuresiNumberPicker;

        DbHelper dbHelper;

        LinearLayout customList;

        CardView cardView;

        ArrayList<String> dersListesi = new ArrayList<>();


        int baslangicSaatiInt;
        int bitisSaatiInt;
        int dersSuresi;
        int tenefusSuresi;
        int dersBitisSaati;

        String dersBaslangicSaatiString;

        public ViewHolder(final View itemView) {
            super(itemView);

            baslangicSaati = itemView.findViewById(R.id.baslangicSaat);
            bitisSaati = itemView.findViewById(R.id.bitisSaat);
            dersId = itemView.findViewById(R.id.dersId);
            ders = itemView.findViewById(R.id.ders);
            dersNotuEkle = itemView.findViewById(R.id.dersNotuEkle);
            customList = itemView.findViewById(R.id.customList);
            dersler = itemView.findViewById(R.id.dersler);
            onayButton = itemView.findViewById(R.id.onayButton);
            tenefusSuresiNumberPicker = itemView.findViewById(R.id.tenefusSuresi);
            sira = itemView.findViewById(R.id.sira);
            cardView = itemView.findViewById(R.id.cardview);
            tenefusSuresiBaslik = itemView.findViewById(R.id.tenefusSuresiBaslik);


            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());

            tenefusSuresiNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            tenefusSuresiNumberPicker.setMinValue(5);
            tenefusSuresiNumberPicker.setMaxValue(30);

            onayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dersId.getText().toString().equals("0")) {
                        if (getAdapterPosition() > 0)
                            tenefusSuresi = Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersTenefusSuresi());
                        else
                            tenefusSuresi = tenefusSuresiNumberPicker.getValue();
                        dersSuresi = Integer.parseInt(sharedPreferences.getString("dersSuresi", ""));
                        dersBaslangicSaatiString = sharedPreferences.getString("dersBaslangicSaati", "") + ":" + sharedPreferences.getString("dersBaslangicDakikasi", "");

                        baslangicSaatiInt = Integer.parseInt(dersBaslangicSaatiString.substring(0, dersBaslangicSaatiString.indexOf(':')));
                        bitisSaatiInt = Integer.parseInt(dersBaslangicSaatiString.substring(dersBaslangicSaatiString.indexOf(':') + 1, dersBaslangicSaatiString.length()));

                        if (getAdapterPosition() == 0) {

                            if (dersler.getSelectedItem() != null) {
                                KayitEkle(dersler.getSelectedItem().toString(), sharedPreferences.getString("gun", ""), baslangicSaatiInt + ":" + bitisSaatiInt, baslangicSaatiInt + ":" + (dersBitisSaati + dersSuresi), dersler.getSelectedItemPosition());
                                baslangicSaati.setText(baslangicSaatiInt + ":" + bitisSaatiInt);
                                bitisSaati.setText(baslangicSaatiInt + ":" + (dersBitisSaati + dersSuresi));
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                                builder.setMessage("Ders tanımlanmamış ders eklemek ister misiniz ?");
                                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(itemView.getContext(), Dersler.class);
                                        itemView.getContext().startActivity(intent);
                                    }
                                });
                                builder.setNegativeButton("Hayır", null);
                                builder.show();
                            }
                        } else {
                            if (dersler.getSelectedItem().toString().equals("--Öğle Arası--")) {
                                String ogleBaslangic = liste.get(getAdapterPosition() - 1).getDersBitisSaati();
                                baslangicSaati.setText(ogleBaslangic);
                                bitisSaati.setText(String.valueOf((Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(0, liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':'))) + 1) + ":" + liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':') + 1, liste.get(getAdapterPosition() - 1).getDersBitisSaati().length())));
                                KayitEkle(dersler.getSelectedItem().toString(), sharedPreferences.getString("gun", ""), baslangicSaati.getText().toString(), bitisSaati.getText().toString(), dersler.getSelectedItemPosition());
                            } else {
                                String simdikiBaslangicSaati = "";
                                if (liste.get(getAdapterPosition() - 1).getDersAdi().equals("--Öğle Arası--")) {
                                    if (Integer.parseInt((String.valueOf(Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':') + 1, liste.get(getAdapterPosition() - 1).getDersBitisSaati().length()))))) >= 60)
                                        simdikiBaslangicSaati = String.valueOf(Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(0, liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':'))) + 1) + ":" + String.valueOf(Integer.parseInt(String.valueOf(Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':') + 1, liste.get(getAdapterPosition() - 1).getDersBitisSaati().length())))) - 60);
                                    else
                                        simdikiBaslangicSaati = liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(0, liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':')) + ":" + String.valueOf(Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':') + 1, liste.get(getAdapterPosition() - 1).getDersBitisSaati().length())));

                                    int simdikiBitisSaati = (Integer.parseInt(simdikiBaslangicSaati.substring(simdikiBaslangicSaati.indexOf(':') + 1, simdikiBaslangicSaati.length()))) + dersSuresi;
                                    String simdikiBitisString;

                                    if (simdikiBitisSaati >= 60) {
                                        simdikiBitisString = (Integer.parseInt(simdikiBaslangicSaati.substring(0, simdikiBaslangicSaati.indexOf(':'))) + 1) + ":" + (simdikiBitisSaati - 60);
                                        KayitEkle(dersler.getSelectedItem().toString(), sharedPreferences.getString("gun", ""), simdikiBaslangicSaati, simdikiBitisString, dersler.getSelectedItemPosition());
                                    } else {
                                        simdikiBitisString = simdikiBaslangicSaati.substring(0, simdikiBaslangicSaati.indexOf(':')) + ":" + simdikiBitisSaati;
                                        KayitEkle(dersler.getSelectedItem().toString(), sharedPreferences.getString("gun", ""), simdikiBaslangicSaati, simdikiBitisString, dersler.getSelectedItemPosition());
                                    }

                                    baslangicSaati.setText(simdikiBaslangicSaati);
                                    bitisSaati.setText(simdikiBitisString);
                                } else {
                                    if (Integer.parseInt((String.valueOf(Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':') + 1, liste.get(getAdapterPosition() - 1).getDersBitisSaati().length())) + tenefusSuresi))) >= 60)
                                        simdikiBaslangicSaati = String.valueOf(Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(0, liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':'))) + 1) + ":" + String.valueOf(Integer.parseInt(String.valueOf(Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':') + 1, liste.get(getAdapterPosition() - 1).getDersBitisSaati().length())) + tenefusSuresi)) - 60);
                                    else
                                        simdikiBaslangicSaati = liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(0, liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':')) + ":" + String.valueOf(Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':') + 1, liste.get(getAdapterPosition() - 1).getDersBitisSaati().length())) + tenefusSuresi);

                                    int simdikiBitisSaati = (Integer.parseInt(simdikiBaslangicSaati.substring(simdikiBaslangicSaati.indexOf(':') + 1, simdikiBaslangicSaati.length()))) + dersSuresi;
                                    String simdikiBitisString;

                                    if (simdikiBitisSaati >= 60) {
                                        simdikiBitisString = (Integer.parseInt(simdikiBaslangicSaati.substring(0, simdikiBaslangicSaati.indexOf(':'))) + 1) + ":" + (simdikiBitisSaati - 60);
                                        KayitEkle(dersler.getSelectedItem().toString(), sharedPreferences.getString("gun", ""), simdikiBaslangicSaati, simdikiBitisString, dersler.getSelectedItemPosition());
                                    } else {
                                        simdikiBitisString = simdikiBaslangicSaati.substring(0, simdikiBaslangicSaati.indexOf(':')) + ":" + simdikiBitisSaati;
                                        KayitEkle(dersler.getSelectedItem().toString(), sharedPreferences.getString("gun", ""), simdikiBaslangicSaati, simdikiBitisString, dersler.getSelectedItemPosition());
                                    }

                                    baslangicSaati.setText(simdikiBaslangicSaati);
                                    bitisSaati.setText(simdikiBitisString);

//                                    System.out.println("şimdiki başlangıç :"+(String.valueOf(Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':') + 1, liste.get(getAdapterPosition() - 1).getDersBitisSaati().length())) + tenefusSuresi)));
//                                    System.out.println("şimdiki bitis :"+(Integer.parseInt(simdikiBaslangicSaati.substring(simdikiBaslangicSaati.indexOf(':') + 1, simdikiBaslangicSaati.length())) + tenefusSuresi + dersSuresi));
                                }
                            }
                        }
                    } else
                        KayitGuncelle();
                }
            });
            dersNotuEkle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dersler.getSelectedItem() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                        builder.setMessage("Ders tanımlanmamış ders eklemek ister misiniz ?");
                        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(itemView.getContext(), Dersler.class);
                                itemView.getContext().startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("Hayır", null);
                        builder.show();
                    } else {
                        Intent intent = new Intent(itemView.getContext(), DersNotuEkle.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Ders", ders.getText().toString());
                        intent.putExtras(bundle);
                        itemView.getContext().startActivity(intent);
                    }
                }
            });

            dbHelper = new DbHelper(itemView.getContext());

            customList.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(itemView.getContext());
                    alertDialog.setTitle("Dersi Sil").setMessage("Dersi Silmek İstediğinize Emin Misiniz ?").setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            liste.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());

                            notifyDataSetChanged();

                            KayitSil(dersId.getText().toString());

                        }
                    }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                    return false;
                }
            });
            dersler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (dersler.getItemAtPosition(position).toString().equals("--Öğle Arası--")) {
                        dersler.setVisibility(View.INVISIBLE);
                        tenefusSuresiBaslik.setText("Öğle Arası Süresi");
                        tenefusSuresiNumberPicker.setMinValue(60);
                        tenefusSuresiNumberPicker.setMaxValue(60);

                        String ogleBaslangic = liste.get(getAdapterPosition() - 1).getDersBitisSaati();
                        baslangicSaati.setText(ogleBaslangic);
                        bitisSaati.setText(String.valueOf((Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(0, liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':'))) + 1) + ":" + liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':') + 1, liste.get(getAdapterPosition() - 1).getDersBitisSaati().length())));


                        sira.setText("Öğle Arası");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            KayitYukle();


        }
        @Override
        public void onClick(View view) {
            Log.d("position ", getAdapterPosition() + "");
        }

        public void updateList(List<Ders> list) {
            liste = (ArrayList<Ders>) list;
            notifyDataSetChanged();
        }

        public boolean KayitSil(String id) {
            DbHelper dbHelper = new DbHelper(itemView.getContext());
            int delete = dbHelper.dersSil(id);
            if (delete > 0) {
                Toast.makeText(itemView.getContext(), "Ders Silindi", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        }

        private void KayitEkle(String dersAdi, String gun, String baslangicSaat, String bitisSaat, int dersPozisyon) {
            try {
                DbHelper dbHelper = new DbHelper(itemView.getContext());

                if (dbHelper.dersEkle(dersAdi, gun, baslangicSaat, bitisSaat, dersPozisyon, String.valueOf(tenefusSuresiNumberPicker.getValue()))) {
                    if (dersler.getSelectedItem().toString().equals("--Öğle Arası--"))
                        Toast.makeText(itemView.getContext(), "Öğle Arası Eklendi", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(itemView.getContext(), "Ders Eklendi", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException s) {
                Toast.makeText(itemView.getContext(), "Ders Eklenemedi", Toast.LENGTH_SHORT).show();
                s.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void KayitYukle() {
            Cursor cursor;
            DbHelper dbHelper = new DbHelper(itemView.getContext());
            cursor = dbHelper.dersler();

            dersListesi.add("--Boş Ders--");
            dersListesi.add("--Öğle Arası--");


            while (cursor.moveToNext()) {
                dersListesi.add(cursor.getString(0));
            }
            ArrayAdapter<String> derslerAdapter = new ArrayAdapter<String>(itemView.getContext(), android.R.layout.simple_list_item_1, dersListesi);
            dersler.setAdapter(derslerAdapter);

            derslerAdapter.notifyDataSetChanged();
        }

        void KayitGuncelle() {
            DbHelper dbHelper = new DbHelper(itemView.getContext());
            if (dbHelper.dersGuncelle(Integer.parseInt(dersId.getText().toString()), dersler.getSelectedItem().toString(), dersler.getSelectedItemPosition(), String.valueOf(tenefusSuresiNumberPicker.getValue())))
                Toast.makeText(itemView.getContext(), "Ders Güncellendi", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(itemView.getContext(), "Ders Güncellenemedi", Toast.LENGTH_SHORT).show();
        }
    }
}