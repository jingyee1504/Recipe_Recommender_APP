package com.example.reciperecommenderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditRecipe extends AppCompatActivity {

    //Declare variable
    FirebaseDatabase DATABASE;
    DatabaseReference mDatabaseReference,mUserDatabaseReference;

    Uri uri;
    TextView tvCategory,tvCountry;
    TextInputLayout etRecipeName, etIngredients,etInstructions;
    TextInputEditText tvRecipeName,tvIngredients,tvInstructions;
    ImageView imgCameraIcon,imgRecipePhoto,imgBackEditRecipe;
    Button btnCreate;
    String[] category = {"Beef","Chicken","Fish","Sides","Pasta","Seafood","Dessert","Sushi","Vegan","Noodles","Western","Broth"};
    String[] country = {"USA","UK","China","Malaysia","India","French","New Zealand","Japan"};
    String recipeImageURL,key;
    AutoCompleteTextView autoCompleteCategory;
    AutoCompleteTextView autoCompleteCountry;
    ArrayAdapter<String> adapterCategory;
    ArrayAdapter<String> adapterCountry;
    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        imgCameraIcon =  findViewById(R.id.imgCameraIcon);
        imgRecipePhoto =  findViewById(R.id.imgCreateRecipe);
        imgBackEditRecipe =  findViewById(R.id.imgBackEditRecipe);
        autoCompleteCategory =  findViewById(R.id.autoCompleteCategory);
        autoCompleteCountry =  findViewById(R.id.autoCompleteCountry);
        tvCategory =  findViewById(R.id.tvCategory);
        tvCountry = findViewById(R.id.tvCountry);
        tvRecipeName = findViewById(R.id.tvRecipeName);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvInstructions = findViewById(R.id.tvInstructions);
        etRecipeName = findViewById(R.id.etRecipeName);
        etIngredients = findViewById(R.id.etIngredients);
        etInstructions = findViewById(R.id.etInstructions);
        btnCreate =findViewById(R.id.btnCreate);
        loadingBar =  (ProgressBar) findViewById(R.id.loadingBar);

        getRecipeData();

        adapterCategory = new ArrayAdapter<String>(this,R.layout.list_category,category);
        adapterCountry = new ArrayAdapter<String>(this,R.layout.list_category,country);

        autoCompleteCategory.setAdapter(adapterCategory);
        autoCompleteCountry.setAdapter(adapterCountry);

        imgCameraIcon.setVisibility(View.GONE);

        autoCompleteCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String category = parent.getItemAtPosition(i).toString();
                tvCategory.setText(category);
            }
        });

        autoCompleteCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String country = parent.getItemAtPosition(i).toString();
                tvCountry.setText(country);
            }
        });

        imgBackEditRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (EditRecipe.this,CookBook.class);
                startActivity(intent);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRecipe();
            }
        });
    }

    private void getRecipeData() {

        // Getting the value from dataSnapshot of the database
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Setting the value to textview once getting the value
            String postkey = bundle.getString("key");
            String imageUri = null;
            imageUri = bundle.getString("Image");
            Picasso.get().load(imageUri).into(imgRecipePhoto);
            tvRecipeName.setText(bundle.getString("Title"));
            autoCompleteCategory.setText(bundle.getString("Category"));
            autoCompleteCountry.setText(bundle.getString("Country"));
            tvInstructions.setText(bundle.getString("Instructions"));
            tvIngredients.setText(bundle.getString("Ingredients"));
        }
    }

    private Boolean validationOfRecipeName(){
        String acc  = etRecipeName.getEditText().getText().toString();
        if(acc.isEmpty()){
            etRecipeName.requestFocus();
            etRecipeName.setError("Recipe Name is required!");
            return false;
        }
        else {
            //null - help to remove error message which was generated for the first time and user entered
            // or left this field empty for the next time when the user entered the data
            //and the error will be gone
            etRecipeName.setError(null);
            return true;
        }
    }

    private Boolean validationOfIngredients(){
        String acc  = etIngredients.getEditText().getText().toString();
        if(acc.isEmpty()){
            etIngredients.requestFocus();
            etIngredients.setError("Ingredients is required!");
            return false;
        }
        else {
            //null - help to remove error message which was generated for the first time and user entered
            // or left this field empty for the next time when the user entered the data
            //and the error will be gone
            etIngredients.setError(null);
            return true;
        }
    }

    private Boolean validationOfInstructions(){
        String acc  = etInstructions.getEditText().getText().toString();
        if(acc.isEmpty()){
            etInstructions.setError("Instructions is required!");
            etInstructions.requestFocus();
            return false;
        }
        else {
            //null - help to remove error message which was generated for the first time and user entered
            // or left this field empty for the next time when the user entered the data
            //and the error will be gone
            etInstructions.setError(null);
            return true;
        }
    }

    private Boolean validationOfCategory(){
        String acc  = autoCompleteCategory.getText().toString();
        if(acc.isEmpty()){
            autoCompleteCategory.requestFocus();
            autoCompleteCategory.setError("Category is required!");
            return false;
        }
        else {
            //null - help to remove error message which was generated for the first time and user entered
            // or left this field empty for the next time when the user entered the data
            //and the error will be gone
            autoCompleteCategory.setError(null);
            return true;
        }
    }

    private Boolean validationOfCountry(){
        String acc  = autoCompleteCountry.getText().toString();
        if(acc.isEmpty()){
            autoCompleteCountry.requestFocus();
            autoCompleteCountry.setError("Confirm Password is required!");
            return false;
        }
        else {
            //null - help to remove error message which was generated for the first time and user entered
            // or left this field empty for the next time when the user entered the data
            //and the error will be gone
            autoCompleteCountry.setError(null);
            return true;
        }
    }

    private void updateRecipe(){

        //Get all values - recipe name, ingredients, instructions, country, categories from edit text
        final String recipeName = etRecipeName.getEditText().getText().toString().trim();
        final String ingredients = etIngredients.getEditText().getText().toString().trim();
        final String instructions = etInstructions.getEditText().getText().toString().trim();
        final String category = autoCompleteCategory.getText().toString().trim();
        final String country = autoCompleteCountry.getText().toString().trim();

        //If the compiler found any one these returning false,
        //it will return false to registration function and execute the error
        if(!validationOfRecipeName() | !validationOfIngredients() | !validationOfInstructions() | !validationOfCategory() | !validationOfCountry()){
            return;
        }

        loadingBar.setVisibility(View.VISIBLE);

        //Get all values from bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Setting the value to textview once getting the value
            String postkey = bundle.getString("key");

            DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");

            //UPDATE Recipes database
            mDatabaseReference = DATABASE.getReference("recipes");

            if(!(recipeName.equals(mDatabaseReference.child(postkey).child("recipeName")))){
                mDatabaseReference.child(postkey).child("recipeName").setValue(recipeName);
            }

            if(!(ingredients.equals(mDatabaseReference.child(postkey).child("ingredients")))){
                mDatabaseReference.child(postkey).child("ingredients").setValue(ingredients);
            }
            if(!(instructions.equals(mDatabaseReference.child(postkey).child("instructions")))){
                mDatabaseReference.child(postkey).child("instructions").setValue(instructions);
            }
            if(!(category.equals(mDatabaseReference.child(postkey).child("category")))){
                mDatabaseReference.child(postkey).child("category").setValue(category);
            }
            if(!(country.equals(mDatabaseReference.child(postkey).child("country")))){
                mDatabaseReference.child(postkey).child("country").setValue(country);
            }

            //UPDATE Cookbook Database
            mUserDatabaseReference = DATABASE.getReference("cookbook");

            if(!(recipeName.equals(mUserDatabaseReference
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postkey).child("recipeName")))){
                mUserDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postkey).child("recipeName")
                        .setValue(recipeName);
            }
            if(!(ingredients.equals(mUserDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(postkey).child("ingredients")))){
                mUserDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postkey).child("ingredients")
                        .setValue(ingredients);
            }
            if(!(instructions.equals(mUserDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(postkey).child("instructions")))){
                mUserDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postkey).child("instructions")
                        .setValue(instructions);
            }
            if(!(category.equals(mUserDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(postkey).child("category")))){
                mUserDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postkey).child("category")
                        .setValue(category);
            }
            if(!(country.equals(mUserDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(postkey).child("country")))){
                mUserDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postkey).child("country")
                        .setValue(country);
            }

            loadingBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"Recipe Updated!",Toast.LENGTH_SHORT ).show();

            //Navigate to Cookbook Page
            Intent intent = new Intent (EditRecipe.this, CookBook.class);
            startActivity(intent);
        }
    }
}