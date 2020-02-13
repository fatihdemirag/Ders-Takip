package fatihdemirag.net.dersprogram.ui.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.adapters.CardViewAdapterNote;
import fatihdemirag.net.dersprogram.adapters.CardViewAdapterNoteLesson;
import fatihdemirag.net.dersprogram.db.DbHelper;
import fatihdemirag.net.dersprogram.helpers.classes.LessonClass;
import fatihdemirag.net.dersprogram.helpers.classes.NoteClass;

public class Notes extends Fragment {


    public Notes() {
    }

    private Bundle bundle;

    public static ListView notListesi;

    private RecyclerView dersListesi;

    private Button fabButton;

    private EditText notAra;

    private Animation fabAcilis, fabKapanis;

    public ArrayList<NoteClass> dersNotuArrayList=new ArrayList<>();
    private ArrayList<LessonClass> derslerArrayList=new ArrayList<>();

    private Cursor cursor;
    private DbHelper dbHelper;

    public CardViewAdapterNote cardviewAdapterNote;
    private CardViewAdapterNoteLesson cardviewAdapterNoteLesson;

    private NoteClass dersNotu;
    private LessonClass ders;

    private AdView adView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notes, container, false);



        fabButton = view.findViewById(R.id.fabButton);
        fabAcilis = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fabKapanis = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        notAra = view.findViewById(R.id.notAra);

        dbHelper=new DbHelper(getActivity());
        bundle=getArguments();

        notListesi = view.findViewById(R.id.notListesi);
        dersListesi = view.findViewById(R.id.dersListesi);
        adView = view.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("97B662D4BD302B562AEF9FF593DD78C8").build();
        adView.loadAd(adRequest);

        cardviewAdapterNote = new CardViewAdapterNote(getActivity(), dersNotuArrayList);
        notListesi.setAdapter(cardviewAdapterNote);

        cardviewAdapterNoteLesson = new CardViewAdapterNoteLesson(getActivity(), derslerArrayList);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        dersListesi.setLayoutManager(layoutManager);

        dersListesi.setAdapter(cardviewAdapterNoteLesson);


        KayitYukle();
        DersleriYukle();


        notListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();

                TextView secilenNotBaslik=(view.findViewById(R.id.konuText));
                TextView secilenNot=(view.findViewById(R.id.notText));
                ImageView secilenNotResmi=view.findViewById(R.id.notImage);
                TextView secilenNotId=(view.findViewById(R.id.notId));

                bundle.putString("Seçilen Not Id",secilenNotId.getText().toString());

                bundle.putString("Seçilen Başlık",secilenNotBaslik.getText().toString());
                bundle.putString("Seçilen Not",secilenNot.getText().toString());

                DbHelper dbHelper=new DbHelper(getActivity());
                Cursor cursor=dbHelper.ResimBul(secilenNotId.getText().toString());
                while (cursor.moveToNext())
                    dersNotu.setNotResmi(cursor.getBlob(0));
                //Resmi byteArraya dönüştürme kodu
                //
                //Bitmap bitmap=((BitmapDrawable)secilenNotResmi.getDrawable()).getBitmap();
                //ByteArrayOutputStream stream=new ByteArrayOutputStream();
                //bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                //byte[] i=stream.toByteArray();

                bundle.putByteArray("Seçilen Not Resmi",dersNotu.getNotResmi());

                NoteDetail noteDetail=new NoteDetail();
                noteDetail.setArguments(bundle);

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment,noteDetail,"noteDetail");
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });
        if (dersNotuArrayList.size() == 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setMessage(getString(R.string.notbulunamadi));
            alertDialog.setPositiveButton(getString(R.string.evet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.nav_host_fragment,new NoteAdd(),"noteAdd");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            alertDialog.setNegativeButton(getString(R.string.hayir), null);
            alertDialog.show();
        }
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment,new NoteAdd(),"noteAdd");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        notListesi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle(getString(R.string.dersnotsilonay)).setPositiveButton(getString(R.string.evet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            dbHelper.NotSilTekli(dersNotuArrayList.get(position).getId());
                            Toast.makeText(getActivity(), getString(R.string.notsilindi), Toast.LENGTH_SHORT).show();

                            dersNotuArrayList.clear();
                            KayitYukle();
                            cardviewAdapterNote.notifyDataSetChanged();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), getString(R.string.notsilinemedi), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton(getString(R.string.hayir), null).show();
                return false;
            }
        });

        notAra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Notes.this.onTextChanged(s,start,before,count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return view;
    }

    public void KayitYukle()
    {
        try {
            dersNotuArrayList.clear();
            cursor = dbHelper.dersNotlariTumu();
            while(cursor.moveToNext())
            {
                dersNotu=new NoteClass();
                dersNotu.setDersKonusu(cursor.getString(1));
                dersNotu.setDersNotu(cursor.getString(2));
                dersNotu.setNotResmi(cursor.getBlob(3));
                dersNotu.setDers(cursor.getString(4));
                dersNotu.setId(cursor.getInt(0));
                dersNotu=new NoteClass(dersNotu.getDersKonusu(),dersNotu.getDersNotu(),dersNotu.getNotResmi(),dersNotu.getDers(),dersNotu.getId());
                dersNotuArrayList.add(dersNotu);
            }
            cardviewAdapterNote.notifyDataSetChanged();

        }catch (SQLException e)
        {
            Toast.makeText(getActivity(), getString(R.string.notyok), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void DersleriYukle()
    {
        try {
            derslerArrayList.clear();
            cursor = dbHelper.dersler();

            ders=new LessonClass(getActivity().getString(R.string.tum),"","","",0,0,"","","",0,0,"",0);
            derslerArrayList.add(ders);
            while(cursor.moveToNext())
            {
                ders=new LessonClass();
                ders.setDersAdi(cursor.getString(0));

                ders=new LessonClass(ders.getDersAdi(),"","","",0,0,"","","",0,0,"",0);
                derslerArrayList.add(ders);
            }
            cardviewAdapterNoteLesson.notifyDataSetChanged();

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
        int textlength = cs.length();
        ArrayList<NoteClass> tempArrayList = new ArrayList<NoteClass>();
        for(NoteClass c: dersNotuArrayList){
            if (textlength <= c.getDers().length()) {
                if (c.getDers().toLowerCase().contains(cs.toString().toLowerCase()) || c.getDersKonusu().toLowerCase().contains(cs.toString().toLowerCase())) {
                    tempArrayList.add(c);
                }
            }
        }
        cardviewAdapterNote = new CardViewAdapterNote(getActivity(), tempArrayList);
        notListesi.setAdapter(cardviewAdapterNote);
    }

}
