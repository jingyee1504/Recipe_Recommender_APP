package com.example.reciperecommenderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgetPassword extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText etEmail;
    Button btnReset;
    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        etEmail = (EditText) findViewById(R.id.etEmail);
        btnReset = (Button) findViewById(R.id.btnReset);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        mAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPasswordViaEmail();
            }
        });

    }

    private Boolean validationOfEmail(){
        String acc  = etEmail.getText().toString();
        if(acc.isEmpty()){
            etEmail.requestFocus();
            etEmail.setError("Email is required!");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(acc).matches()){
            etEmail.requestFocus();
            etEmail.setError("Invalid Email Address!");
            return false;
        }
        else {
            //null - help to remove error message which was generated for the first time and user entered
            // or left this field empty for the next time when the user entered the data
            //and the error will be gone
            etEmail.setError(null);
            return true;
        }
    }

    private void resetPasswordViaEmail(){
        final String email = etEmail.getText().toString().trim();

        if(!validationOfEmail()){
            return;
        }

        loadingBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    loadingBar.setVisibility(View.GONE);
                    Toast.makeText(ForgetPassword.this, "Check your email to reset your password",
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent (ForgetPassword.this, Login.class);
                    startActivity(intent);
                }
                else{
                    loadingBar.setVisibility(View.GONE);
                    Toast.makeText(ForgetPassword.this, "Failed. Retry...", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}