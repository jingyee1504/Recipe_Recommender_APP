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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase DATABASE;
    DatabaseReference mDatabaseReference;

    Button btnLogin2;
    TextView tvSignUp,tvForgetPassword;
    EditText etEmail;
    TextInputLayout etPassword;
    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin2 = (Button) findViewById(R.id.btnLogin2);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (TextInputLayout) findViewById(R.id.etPassword);
        loadingBar =  (ProgressBar) findViewById(R.id.loadingBar);

        mAuth = FirebaseAuth.getInstance();

        DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = DATABASE.getReference("users");

        btnLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateLogin();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Login.this, Register.class);
                startActivity(intent);
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Login.this, ForgetPassword.class);
                startActivity(intent);
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

    private Boolean validationOfPassword(){
        String acc  = etPassword.getEditText().getText().toString();

        if(acc.isEmpty()){
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return false;
        }else if(acc.length()< 8 ){
            etPassword.setError("Minimum password length must be 8 characters");
            etPassword.requestFocus();
            return false;
        }else if(acc.length() > 15 ){
            etPassword.setError("Maximum password length must be 15 characters");
            etPassword.requestFocus();
            return false;
        }
        else {
            //null - help to remove error message which was generated for the first time and user entered
            // or left this field empty for the next time when the user entered the data
            //and the error will be gone
            etPassword.setError(null);
            return true;
        }
    }

    //Authenticate User
    private void authenticateLogin(){
        //Get all values - email, password
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getEditText().getText().toString().trim();

        //If the compiler found any one these returning false,
        //it will return false to registration function and execute the error
        if(!validationOfEmail() | !validationOfPassword()){
            return;
        }

        loadingBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    loadingBar.setVisibility(View.GONE);

                    update(password);

                    Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(Login.this, MainActivity.class);
                    startActivity(intent1);
                }
                else{
                    loadingBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this,
                            "Login Unsuccessful! Retry...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void update(String password){
        mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("password")
                .setValue(etPassword.getEditText().getText().toString());

        //if password different with database update, because this user forget password before
        if(!(password.equals(mDatabaseReference.child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("password")))){
            mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("password").setValue(etPassword.getEditText().getText().toString());
        }
    }

}
