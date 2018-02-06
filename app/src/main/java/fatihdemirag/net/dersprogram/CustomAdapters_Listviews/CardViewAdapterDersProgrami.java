package fatihdemirag.net.dersprogram.CustomAdapters_Listviews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;
import fatihdemirag.net.dersprogram.DersNotlariP.DersNotuEkle;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.Sınıflar.Ders;

/**
 * Created by fxd on 01.02.2018.
 */

public class CardViewAdapterDersProgrami extends RecyclerView.Adapter<CardViewAdapterDersProgrami.ViewHolder> {
    Context context;
    ArrayList<Ders> liste;
    View view;
    ViewHolder viewHolder;

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
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView baslangicSaati, bitisSaati, dersId, ders;
        ImageView dersDuzenle, dersNotuEkle, onayButton;

        Spinner dersler;

        Dialog dialog;

        TextView dialogId;
        EditText dialogDersAd, dialogGirisSaati, dialogCikisSaati;

        DbHelper dbHelper;

        RelativeLayout customList;

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
            dersDuzenle = itemView.findViewById(R.id.dersDuzenle);
            dersNotuEkle = itemView.findViewById(R.id.dersNotuEkle);
            customList = itemView.findViewById(R.id.customList);
            dersler = itemView.findViewById(R.id.dersler);
            onayButton = itemView.findViewById(R.id.onayButton);

            dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.activity_custom__alert_dialog);
            dialog.setTitle(R.string.duzenle);

            dialogId = dialog.findViewById(R.id.idAlert);
            dialogDersAd = dialog.findViewById(R.id.dersAdi);
            dialogGirisSaati = dialog.findViewById(R.id.dersGiris);
            dialogCikisSaati = dialog.findViewById(R.id.dersCikis);

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
            dersDuzenle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            onayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tenefusSuresi = Integer.parseInt(sharedPreferences.getString("tenefusSuresi", ""));
                    dersSuresi = Integer.parseInt(sharedPreferences.getString("dersSuresi", ""));
                    dersBaslangicSaatiString = sharedPreferences.getString("dersBaslangicSaati", "") + ":" + sharedPreferences.getString("dersBaslangicDakikasi", "");

                    baslangicSaatiInt = Integer.parseInt(dersBaslangicSaatiString.substring(0, dersBaslangicSaatiString.indexOf(':')));
                    bitisSaatiInt = Integer.parseInt(dersBaslangicSaatiString.substring(dersBaslangicSaatiString.indexOf(':') + 1, dersBaslangicSaatiString.length()));

                    if (getAdapterPosition() == 0) {

                        KayitEkle(dersler.getSelectedItem().toString(), sharedPreferences.getString("gun", ""), baslangicSaatiInt + ":" + bitisSaatiInt, baslangicSaatiInt + ":" + dersBitisSaati, dersler.getSelectedItemPosition());

                        baslangicSaati.setText(baslangicSaatiInt + ":" + bitisSaatiInt);
                        bitisSaati.setText(baslangicSaatiInt + ":" + dersBitisSaati);
                    } else {
                        String simdikiBaslangicSaati = liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(0, liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':')) + ":" + String.valueOf(Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':') + 1, liste.get(getAdapterPosition() - 1).getDersBitisSaati().length())) + tenefusSuresi);
                        int simdikiBitisSaati = Integer.parseInt(simdikiBaslangicSaati.substring(simdikiBaslangicSaati.indexOf(':') + 1, simdikiBaslangicSaati.length())) + tenefusSuresi + dersSuresi;
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
                    }
                }
            });
            dersNotuEkle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), DersNotuEkle.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Ders", ders.getText().toString());
                    intent.putExtras(bundle);
                    itemView.getContext().startActivity(intent);
                }
            });

            dbHelper = new DbHelper(itemView.getContext());

            dialog.findViewById(R.id.kaydetButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean guncel = dbHelper.updateData(Integer.parseInt(dialogId.getText().toString()), dialogDersAd.getText().toString()
                            , dialogGirisSaati.getText().toString(), dialogCikisSaati.getText().toString());
                    if (guncel)
                        Toast.makeText(itemView.getContext(), "Ders Güncellendi", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(itemView.getContext(), "Ders Güncellenemedi", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.findViewById(R.id.cikButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            customList.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(itemView.getContext());
                    alertDialog.setTitle("Dersi Sil").setMessage("Dersi Silmek İstediğinize Emin Misiniz ?").setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            liste.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());

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
            int delete = dbHelper.deleteData(id);
            if (delete > 0) {
                Toast.makeText(itemView.getContext(), "Kayıt Silindi", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        }

        private void KayitEkle(String dersAdi, String gun, String baslangicSaat, String bitisSaat, int dersPozisyon) {
            try {
                DbHelper dbHelper = new DbHelper(itemView.getContext());

                if (dbHelper.insertData(dersAdi, gun, baslangicSaat, bitisSaat, dersPozisyon))
                    Toast.makeText(itemView.getContext(), "Ders Eklendi", Toast.LENGTH_SHORT).show();
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
            cursor = dbHelper.getAllData3();

            ArrayList<String> dersListesi = new ArrayList<>();

            while (cursor.moveToNext()) {
                dersListesi.add(cursor.getString(0));
            }
            ArrayAdapter<String> derslerAdapter = new ArrayAdapter<String>(itemView.getContext(), android.R.layout.simple_list_item_1, dersListesi);
            dersler.setAdapter(derslerAdapter);

            derslerAdapter.notifyDataSetChanged();
        }

    }
}
