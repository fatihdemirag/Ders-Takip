package fatihdemirag.net.dersprogram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by fxd on 25.08.2017.
 */

public class Custom_Adapter2 extends ArrayAdapter<DersNotu> {
    public Custom_Adapter2(Context context, ArrayList<DersNotu> dersNotuArrayList) {
        super(context,R.layout.activity_custom__list_view2,dersNotuArrayList);
        this.context=context;
        this.dersNotuArrayList=dersNotuArrayList;
    }
    LayoutInflater layoutInflater;
    Context context;
    ImageView notResmi;
    TextView konu,not,notId;
    ArrayList<DersNotu> dersNotuArrayList;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view=layoutInflater.inflate(R.layout.activity_custom__list_view2,parent,false);

        konu=(TextView)view.findViewById(R.id.konuText);
        not=(TextView)view.findViewById(R.id.notText);
        notResmi=(ImageView)view.findViewById(R.id.notImage);
        notId=(TextView)view.findViewById(R.id.notId);

        DersNotu dersNotu=dersNotuArrayList.get(position);
        konu.setText(dersNotu.getDersKonusu());
        not.setText(dersNotu.getDersNotu());
        notId.setText(dersNotu.getId()+"");

        byte[] photo=dersNotu.getNotResmi();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
        Bitmap bitmap= BitmapFactory.decodeStream(imageStream);
        notResmi.setImageBitmap(Bitmap.createScaledBitmap(bitmap,120,120,false));



        return view;
    }
}
