package fatihdemirag.net.dersprogram.DersNotlariP;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;
import fatihdemirag.net.dersprogram.R;

public class DersNotuEkle extends Activity {

    Button resimEkle,ekleButton,fotografCek;

    Intent intent;
    int secilenResim=1;
    ImageView resim;
    Uri getResim;
    InputStream inputStream;
    EditText konu,not;
    DbHelper dbHelper;

    String ders;
    String dosyaIsmi,konum,tarih;

    boolean foto=false;

    AdView adView;

    File resimDosyasi,resimKonumu;
    File resimOlustur=null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null)
        {
            if (requestCode!=0 && !foto)
            {
                try {
                    getResim = data.getData();
                    inputStream = getContentResolver().openInputStream(getResim);
                    Bitmap r = BitmapFactory.decodeStream(inputStream);
                    resim.setImageBitmap(r);
                    resim.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException f) {
                    Toast.makeText(getApplicationContext(), getString(R.string.resimyok), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), getString(R.string.resimyok), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_notu_ekle);
        Bundle bundle = getIntent().getExtras();
        ders = bundle.getString("Ders");

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.ekle) + " " + ders + " " + getString(R.string.not));

        resim=(ImageView)findViewById(R.id.galeriResim);
        resimEkle=(Button)findViewById(R.id.resimEkle);
        resimEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,secilenResim);
            }
        });
        konu=(EditText)findViewById(R.id.konu);
        not=(EditText)findViewById(R.id.not);
        ekleButton=(Button)findViewById(R.id.ekleButton);
        fotografCek=findViewById(R.id.fotografCek);
        dbHelper=new DbHelper(getApplicationContext());

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("47F268874164B56F4CA084A336DE0B42").build();
        adView.loadAd(adRequest);

        ekleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (resim.getDrawable() == null) {
                        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.blank)).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] i = stream.toByteArray();
                        dbHelper = new DbHelper(getApplicationContext());

                        if (konu.getText().length() == 0)
                            Toast.makeText(getApplicationContext(), getString(R.string.konuuyari), Toast.LENGTH_SHORT).show();
                        else if (not.getText().length() == 0)
                            Toast.makeText(getApplicationContext(), getString(R.string.notuyari), Toast.LENGTH_SHORT).show();
                        else {
                            if (dbHelper.dersNotuEkle(konu.getText().toString(), ders, i, not.getText().toString())) {
                                onBackPressed();
                                Toast.makeText(getApplicationContext(), getString(R.string.noteklendi), Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getApplicationContext(), getString(R.string.noteklenemedi), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Bitmap bitmap = ((BitmapDrawable) resim.getDrawable()).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] i = stream.toByteArray();
                        dbHelper = new DbHelper(getApplicationContext());

                        if (konu.getText().length() == 0)
                            Toast.makeText(getApplicationContext(), getString(R.string.konuuyari), Toast.LENGTH_SHORT).show();
                        else if (not.getText().length() == 0)
                            Toast.makeText(getApplicationContext(), getString(R.string.notuyari), Toast.LENGTH_SHORT).show();
                        else {
                            if (dbHelper.dersNotuEkle(konu.getText().toString(), ders, i, not.getText().toString())) {
                                onBackPressed();
                                Toast.makeText(getApplicationContext(), getString(R.string.noteklendi), Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getApplicationContext(), getString(R.string.noteklenemedi), Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getString(R.string.noteklenemedi), Toast.LENGTH_SHORT).show();
                }
            }
        });
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss");
        tarih=simpleDateFormat.format(Calendar.getInstance().getTime());

        fotografCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dosyaIsmi=ders+"-"+tarih;
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
}
