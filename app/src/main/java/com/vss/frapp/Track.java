package com.vss.frapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Track extends AppCompatActivity {

    RecyclerView recyclerView;

    DatabaseReference mRef;
    DatabaseReference cRef;
    FirebaseRecyclerOptions<Model_Crim_Card> options;
    FirebaseRecyclerAdapter<Model_Crim_Card,Crim_Card_Viewholder> adapter;

    private ProgressView pv_circular;
    String img;
    String name;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        recyclerView = findViewById(R.id.crim_card);
        pv_circular=(ProgressView)findViewById(R.id.club_progress);

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mRef = FirebaseDatabase.getInstance().getReference().child("tracking").child(email.replace('.',','));
        cRef = FirebaseDatabase.getInstance().getReference().child("criminals");
        options = new FirebaseRecyclerOptions.Builder<Model_Crim_Card>()
                .setQuery(mRef,Model_Crim_Card.class).build();

        adapter = new FirebaseRecyclerAdapter<Model_Crim_Card, Crim_Card_Viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Crim_Card_Viewholder holder, int position, @NonNull Model_Crim_Card model) {


                Picasso.get().load(model.getPhoto()).into(holder.photo, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

                holder.full_name.setText(model.getFull_name());

               // Toast.makeText(getApplicationContext(),name+img+model.getId(),Toast.LENGTH_SHORT).show();
                holder.id.setText(model.getId());

            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                pv_circular.setVisibility(View.GONE);
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @NonNull
            @Override
            public Crim_Card_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_card_crim,parent,false);
                return new Crim_Card_Viewholder(view);
            }
        };

        recyclerView.addOnItemTouchListener(new RvItemClickListener(getApplicationContext(), recyclerView, new RvItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                View view2 = recyclerView.getLayoutManager().findViewByPosition(position);
                TextView tv = (TextView)view2.findViewById(R.id.rv_crim_subtitle);
                Intent intent = new Intent(getApplicationContext(), Criminal_Details.class);
                intent.putExtra("id",tv.getText().toString());
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "long Clicked", Toast.LENGTH_SHORT).show();
            }
        }));




        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager rvlayoutmanager = layoutManager;

        recyclerView.setLayoutManager(rvlayoutmanager);

        adapter.startListening();

        recyclerView.setAdapter(adapter);
    }
}
