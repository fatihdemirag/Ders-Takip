package fatihdemirag.net.dersprogram.DersNotlariP;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;

import fatihdemirag.net.dersprogram.R;

public class DersNotuGor extends Activity {

    ImageView gelenResim;
    TextView gelenBaslik,gelenNot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_notu_gor);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

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
}
