package fatihdemirag.net.dersprogram.ui.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fatihdemirag.net.dersprogram.MainActivity;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.db.DbHelper;

public class NoteAdd extends Fragment {


    public NoteAdd() {
    }


    private Button resimEkle,ekleButton,fotografCek;

    private Spinner dersler;

    private ArrayList<String> dersListesi = new ArrayList<>();

    private Intent intent;
    private int secilenResim=1;
    private ImageView resim;
    private Uri getResim;
    private InputStream inputStream;
    private EditText konu,not;
    private DbHelper dbHelper;

    private String dosyaIsmi,konum,tarih;

    private boolean foto=false;


    private File resimDosyasi,resimKonumu;
    private File resimOlustur=null;

    SimpleDateFormat simpleDateFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_note_add, container, false);
        MainActivity.page.setText(getString(R.string.notekle));

        resim=view.findViewById(R.id.galeriResim);
        resimEkle=view.findViewById(R.id.fotografEkle);
        resimEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,secilenResim);
            }
        });
        konu=view.findViewById(R.id.konu);
        not=view.findViewById(R.id.not);
        ekleButton=view.findViewById(R.id.ekleButton);
        fotografCek=view.findViewById(R.id.fotografCek);
        dersler=view.findViewById(R.id.derslerSpinner);

        dbHelper=new DbHelper(getActivity());


        DersleriYukle();

        simpleDateFormat=new SimpleDateFormat("dd.MM.yyyy");

        ekleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (resim.getDrawable() == null) {
                        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.blank)).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] i = stream.toByteArray();
                        dbHelper = new DbHelper(getActivity());

                        if (konu.getText().length() == 0)
                            Toast.makeText(getActivity(), getString(R.string.konuuyari), Toast.LENGTH_SHORT).show();
                        else if (not.getText().length() == 0)
                            Toast.makeText(getActivity(), getString(R.string.notuyari), Toast.LENGTH_SHORT).show();
                        else {
                            if (dbHelper.dersNotuEkle(konu.getText().toString(), dersler.getSelectedItem().toString(), i, not.getText().toString(),simpleDateFormat.format(Calendar.getInstance().getTime()))) {
                                Toast.makeText(getActivity(), getString(R.string.noteklendi), Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getActivity(), getString(R.string.noteklenemedi), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Bitmap bitmap = ((BitmapDrawable) resim.getDrawable()).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] i = stream.toByteArray();
                        dbHelper = new DbHelper(getActivity());

                        if (konu.getText().length() == 0)
                            Toast.makeText(getActivity(), getString(R.string.konuuyari), Toast.LENGTH_SHORT).show();
                        else if (not.getText().length() == 0)
                            Toast.makeText(getActivity(), getString(R.string.notuyari), Toast.LENGTH_SHORT).show();
                        else {
                            if (dbHelper.dersNotuEkle(konu.getText().toString(), dersler.getSelectedItem().toString(), i, not.getText().toString(),simpleDateFormat.format(Calendar.getInstance().getTime()))) {
                                {
                                    konu.setText("");
                                    not.setText("");
                                    Toast.makeText(getActivity(), getString(R.string.noteklendi), Toast.LENGTH_SHORT).show();
                                }
                            } else
                                Toast.makeText(getActivity(), getString(R.string.noteklenemedi), Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.noteklenemedi), Toast.LENGTH_SHORT).show();
                }
            }
        });
        simpleDateFormat=new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss");
        tarih=simpleDateFormat.format(Calendar.getInstance().getTime());

        fotografCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dosyaIsmi=dersler.getSelectedItem().toString()+"-"+tarih;
                try {
                    resimOlustur=resimDosyasiOlustur();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                intent=new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(resimOlustur));
                startActivityForResult(intent,0);
            }
        });


        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null)
        {
            if (requestCode!=0 && !foto)
            {
                try {
                    getResim = data.getData();
                    inputStream = getActivity().getContentResolver().openInputStream(getResim);
                    Bitmap r = BitmapFactory.decodeStream(inputStream);
                    resim.setImageBitmap(r);
                    resim.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException f) {
                    Toast.makeText(getActivity(), getString(R.string.resimyok), Toast.LENGTH_SHORT).show();
                    f.printStackTrace();
                }
            }
            else
            {
                try {
                    Bitmap r = BitmapFactory.decodeFile(konum);
                    resim.setImageBitmap(r);
                    resim.setVisibility(View.VISIBLE);
                }catch (Exception e)
                {
                    Toast.makeText(getActivity(), getString(R.string.resimyok), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    File resimDosyasiOlustur() throws IOException
    {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        File dosya= Environment.getExternalStoragePublicDirectory(getString(R.string.app_name));
        if (!dosya.exists())
            dosya.mkdir();

        resimKonumu=Environment.getExternalStoragePublicDirectory(getString(R.string.app_name));
        resimDosyasi=File.createTempFile(dosyaIsmi,".jpg",resimKonumu);
        konum= resimDosyasi.getAbsolutePath();


        return resimDosyasi;
    }
    private void DersleriYukle() {
        Cursor cursor;
        DbHelper dbHelper = new DbHelper(getActivity());
        cursor = dbHelper.dersler();

        while (cursor.moveToNext()) {
            dersListesi.add(cursor.getString(0));
        }
        ArrayAdapter<String> derslerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dersListesi);
        dersler.setAdapter(derslerAdapter);

        derslerAdapter.notifyDataSetChanged();
    }

}
