package fatihdemirag.net.dersprogram.ui.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.fragment.app.Fragment;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.db.DbHelper;

public class NoteDetail extends Fragment {


    public NoteDetail() {
    }


    private ImageView gelenResim;
    private TextView gelenBaslik,gelenNot;

    private EditText yeniNot;

    private ImageButton solaDon, sagaDon;

    private Button notGuncelle;

    private Button paylas;

    private ProgressDialog progressDialog;

    private Bundle bundle;
    private Bitmap bitmap;

    private String gelenId;

    private boolean notGuncelleTiklandi = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_note_detail, container, false);


        gelenBaslik = view.findViewById(R.id.notGelenBaslik);
        gelenNot = view.findViewById(R.id.notGelen);
        gelenResim = view.findViewById(R.id.notGelenResim);
        paylas = view.findViewById(R.id.paylas);

        notGuncelle = view.findViewById(R.id.notGuncelle);
        yeniNot = view.findViewById(R.id.yeniNot);

        bundle = getArguments();
        gelenBaslik.setText(bundle.getString("Seçilen Başlık"));
        gelenNot.setText(bundle.getString("Seçilen Not"));
        gelenId = bundle.getString("Seçilen Not Id", "");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.notpaylasim));
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
//        int height = display.getHeight();

        byte[] photo=bundle.getByteArray("Seçilen Not Resmi");
        final ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
        bitmap = BitmapFactory.decodeStream(imageStream);
        gelenResim.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1000, width, false));

        paylas.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_share), null, null, null);
        paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsenkronPaylas asenkronPaylas = new AsenkronPaylas();
                asenkronPaylas.execute();
            }
        });

        notGuncelle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit), null, null, null);
        notGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notGuncelleTiklandi) {
                    gelenNot.setText(yeniNot.getText().toString());
                    notGuncelle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit), null, null, null);
                    notGuncelle.setText(getString(R.string.notduzenle));

                    gelenNot.setVisibility(View.VISIBLE);
                    yeniNot.setVisibility(View.INVISIBLE);

                    DbHelper dbHelper = new DbHelper(getActivity());
                    dbHelper.dersNotuGuncelle(Integer.parseInt(gelenId), yeniNot.getText().toString());
                    Toast.makeText(getActivity(), getString(R.string.notguncellendi), Toast.LENGTH_SHORT).show();
                    notGuncelleTiklandi = !notGuncelleTiklandi;
                } else {
                    yeniNot.setText(gelenNot.getText().toString());

                    notGuncelle.setText(getString(R.string.kaydet));
                    notGuncelle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_close), null, null, null);

                    gelenNot.setVisibility(View.INVISIBLE);
                    yeniNot.setVisibility(View.VISIBLE);
                    notGuncelleTiklandi = !notGuncelleTiklandi;
                }
            }
        });
        gelenResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder resimBuyukUyari = new AlertDialog.Builder(getActivity(),android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

                View customUyari = getLayoutInflater().inflate(R.layout.alert_note_image, null);
                final PhotoView photoView = customUyari.findViewById(R.id.gelenResimBuyuk);
                ImageView resimKapat=customUyari.findViewById(R.id.resimKapat);

                solaDon = customUyari.findViewById(R.id.solDon);
                sagaDon = customUyari.findViewById(R.id.sagDon);

                photoView.setImageBitmap(bitmap);

                resimBuyukUyari.setView(customUyari);

                final AlertDialog buyukResimDialog = resimBuyukUyari.create();
                buyukResimDialog.show();

                resimKapat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buyukResimDialog.cancel();
                    }
                });

                solaDon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        photoView.setRotation(photoView.getRotation() - 90);
                    }
                });
                sagaDon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        photoView.setRotation(photoView.getRotation() + 90);
                    }
                });

            }
        });

        return view;
    }
    void Paylas(Bitmap bitmap) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        File file = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/temp.jpeg"));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.baslik) + " : " + gelenBaslik.getText().toString() + "\n" + getString(R.string.not) + " : " + gelenNot.getText().toString());
        startActivity(Intent.createChooser(intent, getString(R.string.notpaylas)));
    }

    void ResimDosyasiOlustur(Bitmap bitmap) {
        File klasor = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
        if (!klasor.exists())
            klasor.mkdir();
        File path = new File(klasor.getAbsolutePath(), "temp.jpeg");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class AsenkronPaylas extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            if (gelenResim.getDrawable() == null)
                this.cancel(true);
            else {
                progressDialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            ResimDosyasiOlustur(bitmap);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Paylas(bitmap);
            return null;
        }
    }

}
