package com.vss.frapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Crim_Card_Viewholder extends RecyclerView.ViewHolder {

public TextView full_name;
public  TextView id;
public ImageView photo;

    public Crim_Card_Viewholder(View itemView) {
        super(itemView);
        full_name = itemView.findViewById(R.id.rv_crim_title);
        id = itemView.findViewById(R.id.rv_crim_subtitle);
        photo = itemView.findViewById(R.id.rv_crim_img);
    }
}
