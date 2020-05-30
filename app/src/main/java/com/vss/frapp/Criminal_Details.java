package com.vss.frapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Criminal_Details extends AppCompatActivity {

    TextView crim_name;
    TextView crim_age;
    TextView crim_id;
    TextView crim_desc;
    TextView crim_addr;
    String id;

    String img;
    String name;
    String key;

    ImageView crim_image;

    RecyclerView recyclerView;
    boolean AlreadyTracking = false;
    String email;

    FloatingActionButton fab;

    DatabaseReference mRef;
    DatabaseReference pRef;
    FirebaseRecyclerOptions<Model_Location> options;
    FirebaseRecyclerAdapter<Model_Location,Location_View_holder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criminal__details);

        crim_name = (TextView) findViewById(R.id.crim_details_name);
        crim_age = (TextView) findViewById(R.id.crim_details_age);
        crim_id = (TextView) findViewById(R.id.crim_details_id);
        crim_desc = (TextView) findViewById(R.id.crim_details_desc);
        crim_image = findViewById(R.id.crim_details_image);
        crim_addr = findViewById(R.id.crim_details_addr);

        recyclerView = findViewById(R.id.rv_crim_details);

        fab = findViewById(R.id.fab);



        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mRef = rootRef.child("locations").child(id);
        Query tQuery = rootRef.child("tracking").child(email.replace('.',',')).orderByChild("id").equalTo(id);
        tQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                }
                else
                {
                    fab.setImageResource(R.drawable.tick);
                    AlreadyTracking = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference nRef = FirebaseDatabase.getInstance().getReference("criminals").child(id);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(),"Invalid ID",Toast.LENGTH_SHORT).show();
                    finish();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        };
        nRef.addListenerForSingleValueEvent(eventListener);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!AlreadyTracking)
                {
                    DatabaseReference uRef = rootRef.child("tracking").child("ids").child(id);
                    uRef.child(email.replace('.',',')).setValue(true);
                    pRef = rootRef.child("tracking").child(email.replace('.',',')).push();
                    pRef.child("id").setValue(id);
                    pRef.child("photo").setValue(img);
                    pRef.child("full_name").setValue(name);
                    Snackbar.make(view, "Added To Tracking list", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageResource(R.drawable.tick);
                    AlreadyTracking = true;
                }
                else
                {
                    Query pQuery = rootRef.child("tracking").child(email.replace('.',',')).orderByChild("id").equalTo(id);
                    pQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                key = childSnapshot.getKey();
                                dataSnapshot.getRef().child(key).removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

                    Snackbar.make(view, "Removed from Tracking list", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageResource(R.drawable.add);

                }

            }
        });

        FirebaseDatabase.getInstance().getReference("criminals").orderByChild("id").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Model_Criminal_Details model = postSnapshot.getValue(Model_Criminal_Details.class);

                    Log.i("yolo",model.getfull_name());
                    String fullname = postSnapshot.child("full_name").getValue(String.class);
                    System.out.println(fullname);
                    crim_name.setText(model.getfull_name());
                    crim_addr.setText(model.getAddress());
                    crim_age.setText(model.getAge());
                    crim_desc.setText(model.getDesc());
                    crim_id.setText(model.getId());
                    img = model.getPhoto();
                    name = model.getfull_name();

                    Picasso.get().load(model.getPhoto()).into(crim_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        options = new FirebaseRecyclerOptions.Builder<Model_Location>()
                .setQuery(mRef,Model_Location.class).build();

        adapter = new FirebaseRecyclerAdapter<Model_Location, Location_View_holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Location_View_holder holder, int position, @NonNull Model_Location model) {
                holder.date.setText(model.getDate());
                holder.city.setText(model.getCity());
                holder.loc.setText(model.getLoc());
                holder.time.setText(model.getTime());
            }
            @Override
            public void onDataChanged() {
                super.onDataChanged();
            }
            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
            @NonNull
            @Override
            public Location_View_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_crim_loc,parent,false);
                return new Location_View_holder(view);
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager rvlayoutmanager = layoutManager;
        recyclerView.setLayoutManager(rvlayoutmanager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }

}
