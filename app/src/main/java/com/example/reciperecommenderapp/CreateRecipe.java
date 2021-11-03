package com.example.reciperecommenderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CreateRecipe extends AppCompatActivity{

    //Declare variable
    FirebaseDatabase DATABASE;
    DatabaseReference mDatabaseReference,mUserDatabaseReference, mCountReference;

    Uri uri;
    TextView tvCategory,tvCountry;
    TextInputLayout etRecipeName, etIngredients,etInstructions;
    ImageView imgCameraIcon,imgRecipePhoto,imgBackCreateRecipe;
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
        setContentView(R.layout.activity_create_recipe);

        imgCameraIcon =  findViewById(R.id.imgCameraIcon);
        imgRecipePhoto =  findViewById(R.id.imgCreateRecipe);
        imgBackCreateRecipe =  findViewById(R.id.imgBackCreateRecipe);
        autoCompleteCategory =  findViewById(R.id.autoCompleteCategory);
        autoCompleteCountry =  findViewById(R.id.autoCompleteCountry);
        tvCategory =  findViewById(R.id.tvCategory);
        tvCountry = findViewById(R.id.tvCountry);
        etRecipeName = findViewById(R.id.etRecipeName);
        etIngredients = findViewById(R.id.etIngredients);
        etInstructions = findViewById(R.id.etInstructions);
        btnCreate =findViewById(R.id.btnCreate);
        loadingBar =  (ProgressBar) findViewById(R.id.loadingBar);

        autoCompleteCategory.setInputType(InputType.TYPE_NULL);
        autoCompleteCountry.setInputType(InputType.TYPE_NULL);

        adapterCategory = new ArrayAdapter<String>(this,R.layout.list_category,category);
        adapterCountry = new ArrayAdapter<String>(this,R.layout.list_category,country);

        autoCompleteCategory.setAdapter(adapterCategory);
        autoCompleteCountry.setAdapter(adapterCountry);

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

        imgCameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(CreateRecipe.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        imgBackCreateRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (CreateRecipe.this,CookBook.class);
                startActivity(intent);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadRecipeWithImage(); // Upload Recipe Wif Image  //addRecipe() inside here
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uri = data.getData();
        imgRecipePhoto.setImageURI(uri);
        imgCameraIcon.setVisibility(View.INVISIBLE);

        imgRecipePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(CreateRecipe.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
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

    private Boolean validationOfRecipeImage(){
        if(imgRecipePhoto.getDrawable() == null){
            Toast.makeText(CreateRecipe.this, "Recipe Image is required!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    private void addRecipe(){
        //Get all values - recipe name, ingredients, instructions, country, categories
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

        RecipeAssistantClass assistantClass = new RecipeAssistantClass(recipeName,ingredients,instructions,category,country,recipeImageURL,key);
        DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = DATABASE.getReference("recipes").push();

        //push()- generate a unique id for every recipe
        mDatabaseReference.setValue(assistantClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    loadingBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Recipe Created!",Toast.LENGTH_SHORT ).show();

                    //Navigate to Cookbook Page
                    Intent intent = new Intent (CreateRecipe.this, CookBook.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CreateRecipe.this,
                            "Create Recipe Failed! Retry...", Toast.LENGTH_SHORT).show();
                    loadingBar.setVisibility(View.GONE);
                }
            }
        });

        String key = mDatabaseReference.getKey();

        mUserDatabaseReference = DATABASE.getReference("cookbook");
        mUserDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key)
                .setValue(assistantClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    loadingBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Cookbook Created!",Toast.LENGTH_SHORT ).show();

                    //Navigate to Cookbook Page
                    Intent intent = new Intent (CreateRecipe.this, CookBook.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CreateRecipe.this,
                            "Create Recipe Failed! Retry...", Toast.LENGTH_SHORT).show();
                    loadingBar.setVisibility(View.GONE);
                }
            }
        });

        int num = 0;

        mCountReference = DATABASE.getReference("trendingCount");
        mCountReference.child(key).child("count").setValue(num);
    }

    public void uploadRecipeWithImage(){
        if(!validationOfRecipeImage()){
            return;
        }

        StorageReference mStorageReference = FirebaseStorage.getInstance("gs://recipe-recommender-app-77e1b.appspot.com")
                .getReference().child("recipeImages").child(uri.getLastPathSegment());

        mStorageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> mUriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!mUriTask.isComplete());
                Uri recipeImageUri = mUriTask.getResult();
                recipeImageURL = recipeImageUri.toString();

                addRecipe();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateRecipe.this, "Recipe Image is required!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
