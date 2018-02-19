package fatihdemirag.net.dersprogram.DersNotlariP;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import fatihdemirag.net.dersprogram.MainPage;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.Sınıflar.DersNotu;

public class DersNotuGor extends Activity {

    ZoomageView gelenResim;
    TextView gelenBaslik,gelenNot;

    EditText yeniNot;

    ImageButton solaDon, sagaDon;

    ImageView notGuncelle;

    Button paylas;

    ProgressDialog progressDialog;

    Bundle bundle;
    Bitmap bitmap;

    String gelenId;

    boolean notGuncelleTiklandi = false;

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
        notGuncelle = findViewById(R.id.notGuncelle);
        yeniNot = findViewById(R.id.yeniNot);

        bundle = getIntent().getExtras();
        gelenBaslik.setText(bundle.getString("Seçilen Başlık"));
        gelenNot.setText(bundle.getString("Seçilen Not"));
        gelenId = bundle.getString("Seçilen Not Id", "");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Not paylaşılıyor lütfen bekleyiniz");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        byte[] photo=bundle.getByteArray("Seçilen Not Resmi");
        final ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
        bitmap = BitmapFactory.decodeStream(imageStream);
        gelenResim.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1000, 600, false));

        paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int PERMISSION_ALL = 1;
                final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                if (!hasPermissions(DersNotuGor.this, PERMISSIONS)) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DersNotuGor.this);
                    alertDialog.setTitle("Paylaşım özelliğini kullanmak için izin gereklidir.").setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(DersNotuGor.this, PERMISSIONS, PERMISSION_ALL);
                        }
                    });
                    alertDialog.show();
                } else {
                    AsenkronPaylas asenkronPaylas = new AsenkronPaylas();
                    asenkronPaylas.execute();
                }

            }
        });



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

        notGuncelle.setImageResource(R.drawable.edit);
        notGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notGuncelleTiklandi) {
                    notGuncelle.setImageResource(R.drawable.edit);

                    gelenNot.setVisibility(View.VISIBLE);
                    yeniNot.setVisibility(View.INVISIBLE);

                    DbHelper dbHelper = new DbHelper(DersNotuGor.this);
                    dbHelper.dersNotuGuncelle(Integer.parseInt(gelenId), yeniNot.getText().toString());
                    Toast.makeText(DersNotuGor.this, "Not Güncellendi", Toast.LENGTH_SHORT).show();
                    notGuncelleTiklandi = !notGuncelleTiklandi;
                } else {
                    yeniNot.setText(gelenNot.getText().toString());

                    notGuncelle.setImageResource(R.drawable.onay);

                    gelenNot.setVisibility(View.INVISIBLE);
                    yeniNot.setVisibility(View.VISIBLE);
                    notGuncelleTiklandi = !notGuncelleTiklandi;
                }
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
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
