package fatihdemirag.net.dersprogram.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.db.DbHelper;
import fatihdemirag.net.dersprogram.helpers.classes.LessonClass;
import fatihdemirag.net.dersprogram.helpers.classes.NoteClass;
import fatihdemirag.net.dersprogram.ui.Fragment.Notes;


public class CardViewAdapterNoteLesson extends RecyclerView.Adapter<CardViewAdapterNoteLesson.ViewHolder> {
    private Context context;
    private ArrayList<LessonClass> liste;
    private View view;

    public CardViewAdapterNoteLesson(Context context, ArrayList<LessonClass> liste) {
        this.context = context;
        this.liste = liste;
    }


    @Override
    public CardViewAdapterNoteLesson.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.cardview_note_lessons, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardViewAdapterNoteLesson.ViewHolder holder, final int position) {
        holder.ders.setText(liste.get(position).getDersAdi());
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ders;

        LinearLayout dersContainer;

        public ViewHolder(final View itemView) {
            super(itemView);

            ders = itemView.findViewById(R.id.ders);
            dersContainer=itemView.findViewById(R.id.dersContainer);

            dersContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KayitYukle(ders.getText().toString());
                }
            });
        }
        @Override
        public void onClick(View view) {
        }

        public void updateList(List<LessonClass> list) {
            liste = (ArrayList<LessonClass>) list;
            notifyDataSetChanged();
        }

        public void KayitYukle(String ders)
        {
            try {
                DbHelper dbHelper=new DbHelper(itemView.getContext());
                ArrayList<NoteClass> dersNotuArrayList=new ArrayList<>();
                Cursor cursor;
                if (ders.equals("Tümü") || ders.equals("All"))
                   cursor = dbHelper.dersNotlariTumu();
                else
                    cursor = dbHelper.dersNotlari(ders);
                while(cursor.moveToNext())
                {
                    NoteClass dersNotu=new NoteClass();
                    dersNotu.setDersKonusu(cursor.getString(1));
                    dersNotu.setDersNotu(cursor.getString(2));
                    dersNotu.setNotResmi(cursor.getBlob(3));
                    dersNotu.setDers(cursor.getString(4));
                    dersNotu.setId(cursor.getInt(0));
                    dersNotu=new NoteClass(dersNotu.getDersKonusu(),dersNotu.getDersNotu(),dersNotu.getNotResmi(),dersNotu.getDers(),dersNotu.getId());
                    dersNotuArrayList.add(dersNotu);
                }
                CardViewAdapterNote cardviewAdapterNote = new CardViewAdapterNote(itemView.getContext(), dersNotuArrayList);
                cardviewAdapterNote.updateList(dersNotuArrayList);

                Notes.notListesi.setAdapter(cardviewAdapterNote);
                cardviewAdapterNote.notifyDataSetChanged();
            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }



    }
}