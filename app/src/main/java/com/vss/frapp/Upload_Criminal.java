package com.vss.frapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Upload_Criminal extends AppCompatActivity {

    private TextView btnUpload;
    private Button btnChoose;
    private TextView btnBack;

   // private ImageView image_prev;

    private EditText crim_name;
    private EditText crim_id;
    private EditText crim_age;
    private EditText crim_add;
    private EditText crim_desc;

    String criminal_Name;
    String criminal_id;
    String criminal_age;
    String criminal_add;
    String criminal_desc;
    String criminal_image;

    int flag =255;

    DatabaseReference mRef;

    private static final int PICK_IMAGE_REQUEST = 234;

    int SELECT_PICTURES = 1;

    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__criminal);

        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        btnBack = findViewById(R.id.back_button);

        //image_prev = findViewById(R.id.image_preview);

        crim_id = findViewById(R.id.crim_id);
        crim_name = findViewById(R.id.crim_name);
        crim_add = findViewById(R.id.crim_add);
        crim_age = findViewById(R.id.crim_age);
        crim_desc = findViewById(R.id.crim_desc);

        mRef = FirebaseDatabase.getInstance().getReference().child("criminals");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criminal_Name = crim_name.getText().toString();
                criminal_id = crim_id.getText().toString();
                criminal_add = crim_add.getText().toString();
                criminal_age = crim_age.getText().toString();
                criminal_desc = crim_desc.getText().toString();
                if(criminal_Name.isEmpty())
                {
                    crim_name.setError("Name is Empty");
                    crim_name.requestFocus();
                }
                else if(criminal_id.isEmpty())
                {
                    crim_id.setError("ID is Empty");
                    crim_id.requestFocus();
                }
                else if(criminal_desc.isEmpty())
                {
                    crim_desc.setError("Description is Empty");
                    crim_desc.requestFocus();
                }
                else if(criminal_age.isEmpty())
                {
                    crim_age.setError("Age is empty");
                    crim_age.requestFocus();
                }
                else if(criminal_add.isEmpty())
                {
                    crim_add.setError("Address is empty");
                    crim_add.requestFocus();
                }
                else {
                    uploadFile();
                    Toast.makeText(getApplicationContext(), "Data Added Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PICTURES) {
            if (resultCode == MainActivity.RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    Log.i("count", String.valueOf(count));
                    int currentItem = 0;
                    while (currentItem < count) {
                        imageUri = data.getClipData().getItemAt(currentItem).getUri();


                        Log.i("uri", imageUri.toString());
                        mArrayUri.add(imageUri);
                        currentItem = currentItem + 1;
                    }
                    Log.i("listsize", String.valueOf(mArrayUri.size()));
                } else if (data.getData() != null) {
                    String imagePath = data.getData().getPath();

                }
            }
        }
    }

    private void uploadFile()
    {
        int up = 0; int k =0;
        String uid = criminal_Name+criminal_id;
        StorageReference filepath = FirebaseStorage.getInstance().getReference().child(uid);
        Log.i("upload",String.valueOf(mArrayUri.size()));
        if(up == mArrayUri.size())
            Toast.makeText(getApplicationContext(),"Choose more Images",Toast.LENGTH_SHORT).show();
        while (up < mArrayUri.size()){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            filepath.child(mArrayUri.get(k).getLastPathSegment()).putFile(mArrayUri.get(k)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    Uri downloadURL = uri.getResult();
                    if(flag == 255) {
                        criminal_image = downloadURL.toString();
                        flag =1;
                    }
                    Toast.makeText(getApplicationContext(), downloadURL.toString(), Toast.LENGTH_SHORT).show();
                    if(flag ==1) {
                        mRef.child(criminal_id).setValue(new Model_Criminal_Details(criminal_Name, criminal_id, criminal_image, criminal_add, criminal_desc, criminal_age));
                        flag = 9;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            });
            up++;
            k++;
        }
    }

}
