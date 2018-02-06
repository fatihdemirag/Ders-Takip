package fatihdemirag.net.dersprogram.DersNotlariP;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsibbold.zoomage.ZoomageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.Sınıflar.DersNotu;

public class DersNotuGor extends Activity {

    ZoomageView gelenResim;
    TextView gelenBaslik,gelenNot;

    ImageButton solaDon, sagaDon;

    Button paylas;

    ProgressDialog progressDialog;

    Bundle bundle;
    Bitmap bitmap;

    String gelenId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_notu_gor);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        gelenBaslik = findViewById(R.id.notGelenBaslik);
        gelenNot = findViewById(R.id.notGelen);
        gelenResim = findViewById(R.id.notGelenResim);
        paylas = findViewById(R.id.paylas);
        solaDon = findViewById(R.id.solaDon);
        sagaDon = findViewById(R.id.sagaDon);

        bundle = getIntent().getExtras();
        gelenBaslik.setText(bundle.getString("Seçilen Başlık"));
        gelenNot.setText(bundle.getString("Seçilen Not"));
        gelenId = bundle.getString("Seçilen Not Id", "");

        byte[] photo=bundle.getByteArray("Seçilen Not Resmi");
        final ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
        bitmap = BitmapFactory.decodeStream(imageStream);
        gelenResim.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1000, 600, false));

        paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsenkronPaylas asenkronPaylas = new AsenkronPaylas();
                asenkronPaylas.execute();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Not paylaşılıyor lütfen bekleyiniz");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        solaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gelenResim.setRotation(gelenResim.getRotation() - 90);
            }
        });
        sagaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gelenResim.setRotation(gelenResim.getRotation() + 90);
            }
        });

    }

    void Paylas(Bitmap bitmap) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        File file = new File(Environment.getExternalStorageDirectory() + "/Ders Programi");
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory() + "/Ders Programi/temp.jpeg"));
        intent.putExtra(Intent.EXTRA_TEXT, "Başlık : " + gelenBaslik.getText().toString() + "\n" + "Not : " + gelenNot.getText().toString());
        startActivity(Intent.createChooser(intent, "Not Paylaş"));
    }

    void ResimDosyasiOlustur(Bitmap bitmap) {
        File klasör = new File(Environment.getExternalStorageDirectory() + "/Ders Programi");
        if (!klasör.exists())
            klasör.mkdir();
        File path = new File(klasör.getAbsolutePath(), "temp.jpeg");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class AsenkronPaylas extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            if (gelenResim == null)
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
