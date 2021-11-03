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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity{

    FirebaseAuth mAuth;
    FirebaseDatabase DATABASE;
    DatabaseReference mDatabaseReference;

    Button btnRegister2;
    TextView tvLogin;
    EditText etName, etEmail;
    TextInputLayout etPassword, etConfirmPassword;
    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister2 =  (Button) findViewById(R.id.btnRegister2);
        tvLogin =  (TextView) findViewById(R.id.tvLogIn);
        etName =  (EditText) findViewById(R.id.etName);
        etEmail =  (EditText) findViewById(R.id.etEmail);
        etPassword = (TextInputLayout) findViewById(R.id.etPassword);
        etConfirmPassword = (TextInputLayout) findViewById(R.id.etConfirmPassword);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        mAuth = FirebaseAuth.getInstance();

        // How to save registration data? Click register button to save data into Firebase
        btnRegister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registration();
            }
        }); //store data on firebase end

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Register.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private Boolean validationOfName(){
        String acc  = etName.getText().toString();
        if(acc.isEmpty()){
            etName.requestFocus();
            etName.setError("Name is required!");
            return false;
        }
        else {
            //null - help to remove error message which was generated for the first time and user entered
            // or left this field empty for the next time when the user entered the data
            //and the error will be gone
            etName.setError(null);
            return true;
        }
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

    private Boolean validationOfConfirmPassword(){
        String acc  = etConfirmPassword.getEditText().getText().toString();
        String acc2 = etPassword.getEditText().getText().toString();
        if(acc.isEmpty()){
            etConfirmPassword.requestFocus();
            etConfirmPassword.setError("Confirm Password is required!");
            return false;
        }else if(!(acc.equals(acc2))){
            etConfirmPassword.requestFocus();
            etConfirmPassword.setError("Confirm password does not match!");
            return false;
        }
        else {
            //null - help to remove error message which was generated for the first time and user entered
            // or left this field empty for the next time when the user entered the data
            //and the error will be gone
            etName.setError(null);
            return true;
        }
    }

    //Store user registration data onto Firebase using button click
    private void registration(){
        //Get all values - name, email, pass, confirmPassword
        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getEditText().getText().toString().trim();
        final String confirmPassword = etConfirmPassword.getEditText().getText().toString().trim();

        //If the compiler found any one these returning false,
        //it will return false to registration function and execute the error
        if(!validationOfName() | !validationOfEmail() | !validationOfPassword() | !validationOfConfirmPassword()){
            return;
        }

        loadingBar.setVisibility(View.VISIBLE);

        //OnCompleteListener - handle both success and failure
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    UserAssistantClass assistantClass = new UserAssistantClass(name,email,password,confirmPassword);

                    //Add more users //user id as the unique id
                    DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
                    mDatabaseReference = DATABASE.getReference("users");
                    mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(assistantClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    loadingBar.setVisibility(View.GONE);
                                    Toast.makeText(Register.this,
                                            "Registered Successfully!", Toast.LENGTH_SHORT).show();

                                    //Navigate to Login Page
                                    Intent intent = new Intent (Register.this, Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                }
                                else{
                                    Toast.makeText(Register.this,
                                            "Registered Unsuccessful! Retry...", Toast.LENGTH_SHORT).show();
                                    loadingBar.setVisibility(View.GONE);
                                }
                            }
                        });
                } else{
                    Toast.makeText(Register.this,
                            "Registered Unsuccessful! Retry...", Toast.LENGTH_SHORT).show();
                    loadingBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
