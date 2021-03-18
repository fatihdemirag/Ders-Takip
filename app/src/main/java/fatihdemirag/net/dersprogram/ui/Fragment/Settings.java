package fatihdemirag.net.dersprogram.ui.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import fatihdemirag.net.dersprogram.MainActivity;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.db.DbHelper;

public class Settings extends Fragment {


    public Settings() {
    }

    private TimePicker dersBaslangicSaati;
    private NumberPicker dersSuresi;
    private Button ayarKaydet;
    private TextView site;

    private LinearLayout dersProgramiSil;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_settings, container, false);
        MainActivity.page.setText(getString(R.string.ayarlar));

        dersBaslangicSaati = view.findViewById(R.id.dersBaslangicSaati);
        dersSuresi = view.findViewById(R.id.dersSuresi);
        ayarKaydet = view.findViewById(R.id.ayarKaydet);
        dersProgramiSil=view.findViewById(R.id.dersProgramiSil);
        site=view.findViewById(R.id.site);

        dersBaslangicSaati.setIs24HourView(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();

        if (!sharedPreferences.getString("dersBaslangicSaati", "").equals("") && !sharedPreferences.getString("dersBaslangicDakikasi", "").equals("")) {
            dersBaslangicSaati.setCurrentHour(Integer.parseInt(sharedPreferences.getString("dersBaslangicSaati", "")));
            dersBaslangicSaati.setCurrentMinute(Integer.parseInt(sharedPreferences.getString("dersBaslangicDakikasi", "")));
        } else {
            dersBaslangicSaati.setCurrentHour(8);
            dersBaslangicSaati.setCurrentMinute(0);
        }
        dersSuresi.setMinValue(30);
        dersSuresi.setMaxValue(50);
        if (!sharedPreferences.getString("dersSuresi", "").equals(""))
            dersSuresi.setValue(Integer.parseInt(sharedPreferences.getString("dersSuresi", "")));


        ayarKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dersBaslangicSaati.getCurrentHour() < 10)
                    editor.putString("dersBaslangicSaati", "0" + String.valueOf(dersBaslangicSaati.getCurrentHour()));
                else
                    editor.putString("dersBaslangicSaati", String.valueOf(dersBaslangicSaati.getCurrentHour()));
                if (dersBaslangicSaati.getCurrentMinute() < 10)
                    editor.putString("dersBaslangicDakikasi", "0" + String.valueOf(dersBaslangicSaati.getCurrentMinute()));
                else
                    editor.putString("dersBaslangicDakikasi", String.valueOf(dersBaslangicSaati.getCurrentMinute()));
                editor.putString("dersSuresi", String.valueOf(dersSuresi.getValue()));
                editor.apply();
                Toast.makeText(getActivity(), getResources().getString(R.string.ayarlarkaydet), Toast.LENGTH_SHORT).show();
            }
        });

        dersProgramiSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.dersprogramisilonay));
                builder.setPositiveButton(getResources().getString(R.string.evet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            DbHelper dbHelper=new DbHelper(getActivity());
                            dbHelper.dersProgramiSil();

                            Toast.makeText(getActivity(), getResources().getString(R.string.dersprogramisilindi), Toast.LENGTH_SHORT).show();
                        }catch (Exception e)
                        {
                            Toast.makeText(getActivity(), getResources().getString(R.string.hata), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.hayir), null);
                builder.show();
            }
        });

        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.fatihdemirag.net"));
                startActivity(intent);
            }
        });


//        String str="7:0";
//        System.out.println("İki nokta : "+str.indexOf(':'));
//        System.out.println("İki nokta sol :"+str.substring(0,str.indexOf(':')));
//        System.out.println("İki nokta sağ :"+str.substring(str.indexOf(':')+1,str.length()));
        return view;
    }

}
