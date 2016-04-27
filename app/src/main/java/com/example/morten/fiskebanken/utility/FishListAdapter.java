package com.example.morten.fiskebanken.utility;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.morten.fiskebanken.R;

public class FishListAdapter extends ArrayAdapter<Fisk> {

    public FishListAdapter(Context context, int resource){
        super(context, resource);
    }

    //Hjelpeklasse for å sette inn informasjon om fisk i liste
    @Override
    public View getView(int position,View convertView, ViewGroup parent){
        View view = convertView;

        if(view == null){
            LayoutInflater viewInflater = LayoutInflater.from(getContext());
            view = viewInflater.inflate(R.layout.activity_fish_list_adapter, null);
        }

        Fisk fisk = getItem(position);
        if(fisk != null) {
            TextView type = (TextView)view.findViewById(R.id.type);
            TextView vekt = (TextView)view.findViewById(R.id.vekt);
            TextView lengde = (TextView)view.findViewById(R.id.lengde);
            ImageView bilde = (ImageView)view.findViewById(R.id.fisk_thumbnail);

            if(type != null){
                type.setText(fisk.getType());
            }
            if(vekt != null){
                String vektDouble = Double.toString(fisk.getVekt());
                vekt.setText(vektDouble);
            }
            if(lengde != null){
                String lengdeDouble = Double.toString(fisk.getLengde());
                lengde.setText(lengdeDouble);
            }
            if(bilde != null){
                final int THUMBSIZE = 256;
                String bildePath = fisk.getBilde();
                bilde.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(bildePath), THUMBSIZE, THUMBSIZE));
            }
        }

        return view;
    }
}
