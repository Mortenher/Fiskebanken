package com.example.morten.fiskebanken;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FishListAdapter extends ArrayAdapter<Fisk> {

    public FishListAdapter(Context context, int resource){
        super(context, resource);
    }

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
        }

        return view;
    }
}
