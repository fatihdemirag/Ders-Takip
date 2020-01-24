package fatihdemirag.net.dersprogram.ui.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.adapters.CardViewAdapterNote;
import fatihdemirag.net.dersprogram.db.DbHelper;
import fatihdemirag.net.dersprogram.helpers.classes.NoteClass;

public class Home extends Fragment {
    public Home() {
    }

    private ListView notListesi;

    private ArrayList<NoteClass> dersNotuArrayList=new ArrayList<>();

    private Cursor cursor;
    private DbHelper dbHelper;

    private NoteClass dersNotu;

    private CardViewAdapterNote cardviewAdapterNote;

    private AdView adView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        notListesi = view.findViewById(R.id.notListesi);
        adView = view.findViewById(R.id.adViewHome);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("97B662D4BD302B562AEF9FF593DD78C8").build();
        adView.loadAd(adRequest);

        cardviewAdapterNote = new CardViewAdapterNote(getActivity(), dersNotuArrayList);
        notListesi.setAdapter(cardviewAdapterNote);

        dbHelper=new DbHelper(getActivity());
        KayitYukle();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());

        if (Objects.equals(sharedPreferences.getString("dersBaslangicSaati", ""), ""))
        {
            Toast.makeText(getActivity(), R.string.dersbaslangictanimsiz, Toast.LENGTH_SHORT).show();
            FragmentManager transaction = getFragmentManager();

            transaction.beginTransaction()
                    .addToBackStack("setting")
                    .replace(R.id.nav_host_fragment, new Settings(),"setting").commit();
        }

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

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    {
                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.cikisyap);
                        builder.setPositiveButton(R.string.evet, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        });
                        builder.setNegativeButton(R.string.hayir, null);
                        builder.show();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void KayitYukle()
    {
        try {
            dersNotuArrayList.clear();
            cursor = dbHelper.dersNotlariSon();
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

}
