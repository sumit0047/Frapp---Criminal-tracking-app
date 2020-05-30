package com.vss.frapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private ActionProcessButton btnSignIn;
    private EditText EmailField;
    private EditText PassField;
    boolean doubleBackToExitPressedOnce = false;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);

        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);

        EmailField = (EditText) findViewById(R.id.email);
        PassField = (EditText) findViewById(R.id.pass);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        startActivity(new Intent(Login.this,MainActivity.class));
                }
            }


        };
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignin();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void startSignin()
    {
        String emailid = EmailField.getText().toString();
        String password = PassField.getText().toString();
        if(emailid.isEmpty())
        {
            EmailField.setError("Email is Empty");
            EmailField.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailid).matches())
        {
            EmailField.setError("Enter a valid EMail");
            EmailField.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            PassField.setError("Password is Empty");
            PassField.requestFocus();
            return;
        }
        else{
            btnSignIn.setProgress(1);
            mAuth.signInWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "Problem With Sign In",Toast.LENGTH_LONG).show();
                        btnSignIn.setProgress(0);
                    }
                    else {
                        checkIfEmailVerified();
                    }
                }
            });
        }
    }

    private void checkIfEmailVerified()
    {
        //no check for email required
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


            Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
            btnSignIn.setProgress(100);

        }
    }


