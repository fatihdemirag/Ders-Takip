package fatihdemirag.net.dersprogram;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

import fatihdemirag.net.dersprogram.R;

public class DersNotuGor extends Activity {

    ImageView gelenResim;
    TextView gelenBaslik,gelenNot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_notu_gor);

        gelenBaslik=(TextView)findViewById(R.id.notGelenBaslik);
        gelenNot=(TextView)findViewById(R.id.notGelen);
        gelenResim=(ImageView)findViewById(R.id.notGelenResim);

        Bundle bundle=getIntent().getExtras();
        gelenBaslik.setText(bundle.getString("Seçilen Başlık"));
        gelenNot.setText(bundle.getString("Seçilen Not"));



        byte[] photo=bundle.getByteArray("Seçilen Not Resmi");
        ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
        Bitmap bitmap= BitmapFactory.decodeStream(imageStream);
        gelenResim.setImageBitmap(Bitmap.createScaledBitmap(bitmap,1000,800,false));


    }
}
