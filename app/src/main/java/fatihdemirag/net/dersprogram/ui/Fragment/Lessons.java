package fatihdemirag.net.dersprogram.ui.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

import fatihdemirag.net.dersprogram.MainActivity;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.db.DbHelper;

public class Lessons extends Fragment {


    public Lessons() {
    }

    private Button dersEkleButton;


    private EditText dialogDersAdi;


    private Animation fabAcilis, fabKapanis;

    private Dialog dialog;

    private ArrayList<String> dersArrayList = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    private Cursor cursor;
    private DbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_lessons, container, false);
        MainActivity.page.setText(getString(R.string.dersler));

        final ListView derslerListesi = view.findViewById(R.id.derslerListesi);
        dersEkleButton = view.findViewById(R.id.dersEkleButton);

        fabAcilis = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fabKapanis = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);

        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, dersArrayList);
        derslerListesi.setAdapter(arrayAdapter);

        dbHelper = new DbHelper(getActivity());
        cursor = dbHelper.dersler();

        KayitYukle();

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_lesson_add);
        dialog.setCancelable(true);

        dialogDersAdi = dialog.findViewById(R.id.dersAdi);


        dersEkleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                dersEkleButton.startAnimation(fabAcilis);

                fabAcilis.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dersEkleButton.startAnimation(fabKapanis);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                dialog.findViewById(R.id.dersEkle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (!dialogDersAdi.getText().toString().equals("")) {
                                if (dbHelper.dersEkle(dialogDersAdi.getText().toString().trim())) {
                                    dersEkleButton.startAnimation(fabKapanis);

                                    dersArrayList.add(dialogDersAdi.getText().toString());
                                    dialog.dismiss();
                                    KayitYukle();

                                    dialogDersAdi.setText("");
                                    Toast.makeText(getActivity(), getString(R.string.derseklendi), Toast.LENGTH_SHORT).show();

                                } else
                                    Toast.makeText(getActivity(), getString(R.string.derseklenemedi), Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getActivity(), getString(R.string.dersbos), Toast.LENGTH_SHORT).show();

                        } catch (SQLException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), getString(R.string.derseklenemedi), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        derslerListesi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage(getString(R.string.derssiluyari)).setPositiveButton(getString(R.string.evet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.dersSilTekli(dersArrayList.get(position).toString());
                        Toast.makeText(getActivity(), dersArrayList.get(position).toString() + " " + getString(R.string.dersisilindi), Toast.LENGTH_SHORT).show();
                        dersArrayList.remove(dersArrayList.get(position).toString());
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton(getString(R.string.hayir), null);
                alertDialog.show();
                return false;
            }
        });
        derslerListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), derslerListesi.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                LessonNotes lessonNotes=new LessonNotes();
                Bundle bundle=new Bundle();
                bundle.putString("ders",derslerListesi.getItemAtPosition(i).toString());
                lessonNotes.setArguments(bundle);
                transaction.replace(R.id.nav_host_fragment,lessonNotes,"noteAdd");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
    void KayitYukle() {
        while (cursor.moveToNext()) {
            dersArrayList.add(cursor.getString(0));
        }
        arrayAdapter.notifyDataSetChanged();
    }

}
