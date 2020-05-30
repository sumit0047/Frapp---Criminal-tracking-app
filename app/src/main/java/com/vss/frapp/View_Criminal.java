package com.vss.frapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class View_Criminal extends AppCompatActivity {

    EditText crim_id;
    TextView submit;
    TextView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__criminal);

        crim_id = findViewById(R.id.crim_view_id);
        submit = findViewById(R.id.btnLookup);

        btnBack = findViewById(R.id.backBtn);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String crim_ID = crim_id.getText().toString();
                if(crim_ID.isEmpty())
                {
                    crim_id.setError("ID is empty");
                    crim_id.requestFocus();
                }
                else
                {
                        Intent intent = new Intent(getApplicationContext(), Criminal_Details.class);
                        intent.putExtra("id", crim_ID);
                        startActivity(intent);
                }
            }
        });
    }
}
