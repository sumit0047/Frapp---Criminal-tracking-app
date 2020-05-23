package com.vss.frapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Location_View_holder extends RecyclerView.ViewHolder {

public TextView time;
public TextView date;
public TextView city;
public TextView loc;

public Location_View_holder(View itemView)
{
    super(itemView);
time = itemView.findViewById(R.id.time_post);
date = itemView.findViewById(R.id.date_post);
loc = itemView.findViewById(R.id.desc_post);
city = itemView.findViewById(R.id.club_post);
}


}
