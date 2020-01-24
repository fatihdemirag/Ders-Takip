package fatihdemirag.net.dersprogram.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.helpers.classes.NoteClass;

public class CardViewAdapterNote extends ArrayAdapter<NoteClass> {
    public CardViewAdapterNote(Context context, ArrayList<NoteClass> dersNotuArrayList) {
        super(context, R.layout.cardview_note, dersNotuArrayList);
        this.context=context;
        this.dersNotuArrayList=dersNotuArrayList;
    }

    private Context context;
    private ArrayList<NoteClass> dersNotuArrayList;

    private ArrayList<NoteClass> tmp;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view= layoutInflater.inflate(R.layout.cardview_note,parent,false);

        TextView konu = view.findViewById(R.id.konuText);
        TextView not = view.findViewById(R.id.notText);
        ImageView notResmi = view.findViewById(R.id.notImage);
        TextView notId = view.findViewById(R.id.notId);

        NoteClass dersNotu=dersNotuArrayList.get(position);
        konu.setText(dersNotu.getDersKonusu());
        not.setText(dersNotu.getDers());
        notId.setText(dersNotu.getId()+"");

        byte[] photo=dersNotu.getNotResmi();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
        Bitmap bitmap= BitmapFactory.decodeStream(imageStream);
        notResmi.setImageBitmap(Bitmap.createScaledBitmap(bitmap,120,120,false));

        return view;
    }
    public void updateList(List<NoteClass> list) {
        dersNotuArrayList = (ArrayList<NoteClass>) list;
        notifyDataSetChanged();
    }

}
