package fatihdemirag.net.dersprogram.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.db.DbHelper;
import fatihdemirag.net.dersprogram.helpers.classes.LessonClass;
import fatihdemirag.net.dersprogram.ui.Fragment.Lessons;
import fatihdemirag.net.dersprogram.ui.Fragment.NoteAdd;


public class CardViewAdapterLesson extends RecyclerView.Adapter<CardViewAdapterLesson.ViewHolder> {
    private Context context;
    private ArrayList<LessonClass> liste;
    private View view;

    public CardViewAdapterLesson(Context context, ArrayList<LessonClass> liste) {
        this.context = context;
        this.liste = liste;
    }


    @Override
    public CardViewAdapterLesson.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.cardview_lessons, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardViewAdapterLesson.ViewHolder holder, final int position) {
        holder.baslangicSaati.setText(liste.get(position).getDersBaslangicSaati());
        holder.bitisSaati.setText(liste.get(position).getDersBitisSaati());
        holder.dersId.setText(liste.get(position).getDersId() + "");
        holder.ders.setText(liste.get(position).getDersAdi());
        holder.dersler.setSelection(liste.get(position).getDersPozisyon());

        if (liste.get(position).getDersTenefusSuresi() != null)
            holder.tenefusSuresiNumberPicker.setValue(Integer.parseInt(liste.get(position).getDersTenefusSuresi()));
        holder.tenefusSuresiNumberPicker.setEnabled(liste.get(position).isTenefusAktifMi());

        if (liste.get(position).getDersTenefusSuresi() != null)
            holder.tenefusSuresiBaslik.setText(view.getResources().getString(R.string.teneffüs)+" : "+liste.get(position).getDersTenefusSuresi()+" "+view.getResources().getString(R.string.dakika));
        else
            holder.tenefusSuresiBaslik.setText(view.getResources().getString(R.string.teneffüs));

        holder.tenefusSuresiNumberPicker.setVisibility(liste.get(position).getTenefusGizle());

        holder.onayButton.setText(liste.get(position).getButonYazisi());
        holder.onayButton.setVisibility(liste.get(position).getOnayAktifMi());
        holder.dersNotuEkle.setVisibility(liste.get(position).getNotEkleAktifMi());
        YoYo.with(Techniques.FadeIn).playOn(holder.customList);
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView baslangicSaati, bitisSaati, dersId, ders, tenefusSuresiBaslik;
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
            tenefusSuresiNumberPicker = itemView.findViewById(R.id.tenefusSuresiNumberPicker);
            cardView = itemView.findViewById(R.id.cardview);
            tenefusSuresiBaslik = itemView.findViewById(R.id.tenefusSuresiYazi);


            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());

            tenefusSuresiNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            tenefusSuresiNumberPicker.setMinValue(5);
            tenefusSuresiNumberPicker.setMaxValue(30);

            onayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
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
                                    builder.setMessage(itemView.getResources().getString(R.string.dersyok));
                                    builder.setPositiveButton(itemView.getResources().getString(R.string.evet), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                                            FragmentTransaction transaction = manager.beginTransaction();
                                            transaction.replace(R.id.nav_host_fragment,new Lessons(),"lessons");
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                        }
                                    });
                                    builder.setNegativeButton(itemView.getResources().getString(R.string.hayir), null);
                                    builder.show();
                                }
                            } else {
                                if (dersler.getSelectedItem().toString().equals(itemView.getResources().getString(R.string.oglearasisuresi))) {
                                    String ogleBaslangic = liste.get(getAdapterPosition() - 1).getDersBitisSaati();
                                    baslangicSaati.setText(ogleBaslangic);
                                    bitisSaati.setText(String.valueOf((Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(0, liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':'))) + 1) + ":" + liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':') + 1, liste.get(getAdapterPosition() - 1).getDersBitisSaati().length())));
                                    KayitEkle(dersler.getSelectedItem().toString(), sharedPreferences.getString("gun", ""), baslangicSaati.getText().toString(), bitisSaati.getText().toString(), dersler.getSelectedItemPosition());
                                } else {
                                    String simdikiBaslangicSaati = "";
                                    if (liste.get(getAdapterPosition() - 1).getDersAdi().equals(itemView.getResources().getString(R.string.oglearasi))) {
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
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.hata), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dersNotuEkle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                        builder.setMessage(itemView.getResources().getString(R.string.dersyok));
                        builder.setPositiveButton(itemView.getResources().getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.nav_host_fragment,new NoteAdd(),"noteAdd");
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                        builder.setNegativeButton(itemView.getResources().getString(R.string.hayir), null);
                        builder.show();
                    }catch (Exception e)
                    {
                        Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.hata), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            dbHelper = new DbHelper(itemView.getContext());
            customList.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(itemView.getContext());
                        alertDialog.setTitle(itemView.getResources().getString(R.string.derssil)).setMessage(itemView.getResources().getString(R.string.derssiluyari)).setPositiveButton(itemView.getResources().getString(R.string.evet), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                liste.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());

                                notifyDataSetChanged();

                                KayitSil(dersId.getText().toString());

                            }
                        }).setNegativeButton(itemView.getResources().getString(R.string.hayir), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.hata), Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
            dersler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (dersler.getItemAtPosition(position).toString().equals(itemView.getResources().getString(R.string.oglearasi))) {
                            dersler.setVisibility(View.INVISIBLE);
                            tenefusSuresiBaslik.setText(itemView.getResources().getString(R.string.oglearasisuresi));
                            tenefusSuresiNumberPicker.setMinValue(60);
                            tenefusSuresiNumberPicker.setMaxValue(60);

                            String ogleBaslangic = liste.get(getAdapterPosition() - 1).getDersBitisSaati();
                            baslangicSaati.setText(ogleBaslangic);
                            bitisSaati.setText(String.valueOf((Integer.parseInt(liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(0, liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':'))) + 1) + ":" + liste.get(getAdapterPosition() - 1).getDersBitisSaati().substring(liste.get(getAdapterPosition() - 1).getDersBitisSaati().indexOf(':') + 1, liste.get(getAdapterPosition() - 1).getDersBitisSaati().length())));

                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.hata), Toast.LENGTH_SHORT).show();
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

        public void updateList(List<LessonClass> list) {
            liste = (ArrayList<LessonClass>) list;
            notifyDataSetChanged();
        }

        public boolean KayitSil(String id) {
            DbHelper dbHelper = new DbHelper(itemView.getContext());
            int delete = dbHelper.dersSil(id);
            if (delete > 0) {
                Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.dersilindi), Toast.LENGTH_SHORT).show();
                updateList(liste);
                return true;
            } else {
                return false;
            }
        }

        private void KayitEkle(String dersAdi, String gun, String baslangicSaat, String bitisSaat, int dersPozisyon) {
            try {
                DbHelper dbHelper = new DbHelper(itemView.getContext());

                if (dbHelper.dersEkle(dersAdi, gun, baslangicSaat, bitisSaat, dersPozisyon, String.valueOf(tenefusSuresiNumberPicker.getValue()))) {
                    if (dersler.getSelectedItem().toString().equals(itemView.getResources().getString(R.string.oglearasi)))
                        Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.oglearasieklendi), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.derseklendi), Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException s) {
                Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.derseklenemedi), Toast.LENGTH_SHORT).show();
                s.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.derseklenemedi), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        private void KayitYukle() {
            Cursor cursor;
            DbHelper dbHelper = new DbHelper(itemView.getContext());
            cursor = dbHelper.dersler();

            dersListesi.add(itemView.getResources().getString(R.string.bosders));
            dersListesi.add(itemView.getResources().getString(R.string.oglearasi));


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
                Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.dersguncellendi), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.dersguncellenemedi), Toast.LENGTH_SHORT).show();
        }
    }
}