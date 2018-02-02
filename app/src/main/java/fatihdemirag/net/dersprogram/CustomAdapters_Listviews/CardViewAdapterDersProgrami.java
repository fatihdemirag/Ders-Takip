package fatihdemirag.net.dersprogram.CustomAdapters_Listviews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView baslangicSaati, bitisSaati, dersId, ders;
        ImageView dersDuzenle, dersNotuEkle;

        Dialog dialog;

        TextView dialogId;
        EditText dialogDersAd, dialogGirisSaati, dialogCikisSaati;

        DbHelper dbHelper;

        RelativeLayout customList;

        public ViewHolder(final View itemView) {
            super(itemView);

            baslangicSaati = itemView.findViewById(R.id.baslangicSaat);
            bitisSaati = itemView.findViewById(R.id.bitisSaat);
            dersId = itemView.findViewById(R.id.dersId);
            ders = itemView.findViewById(R.id.ders);
            dersDuzenle = itemView.findViewById(R.id.dersDuzenle);
            dersNotuEkle = itemView.findViewById(R.id.dersNotuEkle);
            customList = itemView.findViewById(R.id.customList);

            dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.activity_custom__alert_dialog);
            dialog.setTitle(R.string.duzenle);

            dialogId = dialog.findViewById(R.id.idAlert);
            dialogDersAd = dialog.findViewById(R.id.dersAdi);
            dialogGirisSaati = dialog.findViewById(R.id.dersGiris);
            dialogCikisSaati = dialog.findViewById(R.id.dersCikis);

            dersDuzenle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogId.setText(dersId.getText().toString() + "");
                    dialogDersAd.setText(ders.getText().toString());
                    dialogGirisSaati.setText(baslangicSaati.getText().toString());
                    dialogCikisSaati.setText(bitisSaati.getText().toString());
                    dialog.show();
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
                    alertDialog.setTitle("Dersi Sil").setMessage("Dersi Silmek İstediğinize Emin Misiniz").setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //getActivity().recreate();

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
    }
}
