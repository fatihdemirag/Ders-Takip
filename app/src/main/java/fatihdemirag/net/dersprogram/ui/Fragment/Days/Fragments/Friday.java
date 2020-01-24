package fatihdemirag.net.dersprogram.ui.Fragment.Days.Fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.adapters.CardViewAdapterLesson;
import fatihdemirag.net.dersprogram.db.DbHelper;
import fatihdemirag.net.dersprogram.helpers.classes.LessonClass;


public class Friday extends Fragment {


    private ArrayList<LessonClass> list=new ArrayList<>();
    private DbHelper dbHelper;
    private LessonClass lesson;


    private RecyclerView recylerLesson;
    private RecyclerView.LayoutManager layoutManager;
    private CardViewAdapterLesson cardViewAdapterLesson;

    private Button lessonAdd;


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_days_monday,container,false);
        recylerLesson = view.findViewById(R.id.mondayList);
        lessonAdd=view.findViewById(R.id.lessonAddDay);

        layoutManager = new LinearLayoutManager(getActivity());
        cardViewAdapterLesson = new CardViewAdapterLesson(getActivity(), list);

        recylerLesson.setLayoutManager(layoutManager);
        recylerLesson.setAdapter(cardViewAdapterLesson);

        dbHelper=new DbHelper(getActivity());

        Load("Cuma", dbHelper);

        lessonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                list.clear();
                Load("Cuma", dbHelper);
                lesson = new LessonClass();
                lesson.setButonYazisi(getString(R.string.kaydet));
                lesson.setTenefusAktifMi(true);
                lesson.setTenefusSuresiBaslik(getString(R.string.teneffüs));
                list.add(lesson);
                cardViewAdapterLesson.notifyDataSetChanged();

                editor.putString("gun", "Cuma");
                editor.apply();

            }
        });

        return view;
    }
    private void Load(String gun, DbHelper dbHelper) {
        list.clear();
        Cursor cursor = dbHelper.tumDersler();

        while (cursor.moveToNext()) {
            if (cursor.getString(2).equals(gun)) {
                lesson = new LessonClass();
                lesson.setDersAdi(cursor.getString(1));
                lesson.setDersBaslangicSaati(cursor.getString(3));
                lesson.setDersBitisSaati(cursor.getString(4));
                lesson.setDersId((cursor.getInt(0)));
                lesson.setDersPozisyon(cursor.getInt(5));
                lesson.setDersTenefusSuresi(cursor.getString(6));
                lesson.setTenefusAktifMi(true);


                lesson.setTenefusGizle(View.GONE);

                lesson.setButonYazisi(getString(R.string.guncelle));
                if (lesson.getDersAdi().equals(getString(R.string.oglearasi))) {
                    lesson.setOnayAktifMi(View.INVISIBLE);
                    lesson.setNotEkleAktifMi(View.INVISIBLE);
                    lesson.setTenefusSuresiBaslik(getString(R.string.oglearasisuresi));
                } else {
                    lesson.setTenefusSuresiBaslik(getString(R.string.teneffüs));
                }
                if (lesson.getDersAdi().equals(getString(R.string.bosders))) {
                    lesson.setNotEkleAktifMi(View.INVISIBLE);
                }
                lesson = new LessonClass(lesson.getDersAdi(), gun, lesson.getDersBaslangicSaati(), lesson.getDersBitisSaati(), lesson.getDersId(),
                        lesson.getDersPozisyon(), lesson.getDersTenefusSuresi(), lesson.getButonYazisi(),
                        lesson.getSira(), lesson.getOnayAktifMi(), lesson.getNotEkleAktifMi(), lesson.getTenefusSuresiBaslik(),lesson.getTenefusGizle());
                list.add(lesson);
            }
        }
        cardViewAdapterLesson.notifyDataSetChanged();
        dbHelper.close();
    }

}