package com.kelpie.cartogweper;

import  android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<CustomElement> {

    public CustomAdapter(Context context, ArrayList<CustomElement> networks){
        super(context, R.layout.custom_listview_row, networks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.custom_listview_row, parent, false );

        CustomElement item = getItem(position);

        ImageView img = (ImageView) v.findViewById(R.id.imageview);
        TextView text1 = (TextView)v.findViewById(R.id.textview1);
        TextView text2 = (TextView)v.findViewById(R.id.textview2);

        img.setImageResource(R.drawable.ic_wifi_black_18dp);
        text1.setText(item.getSSID());
        text2.setText(item.getProtocol());

        return v;
    }

    public void updateList(ArrayList<CustomElement> networks){
        networks.clear();
        networks.addAll(networks);
        this.notifyDataSetChanged();
    }
}
