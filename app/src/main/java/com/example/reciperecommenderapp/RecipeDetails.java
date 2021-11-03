package com.example.reciperecommenderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.reciperecommenderapp.Adapter.PreferencesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetails extends AppCompatActivity {

    //Initialize variable
    FirebaseDatabase DATABASE;
    DatabaseReference mDatabaseReference,mFavouriteReference;

    MeowBottomNavigation btmNavigation;
    ImageView imgMeal;
    TextView tvRecipe, tvCategory,tvCountry,tvInstructions,tvIngredients;
    Button btnFav,btnShare;
    RecyclerView mRecyclerView;
    PreferencesAdapter mPreferencesAdapter;
    List<RecipeAssistantClass> recipeList;
    Query checkCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        //Assign variable
        imgMeal = (ImageView) findViewById(R.id.imgMeal);
        tvRecipe = (TextView) findViewById(R.id.tvRecipe);
        tvCategory = (TextView) findViewById(R.id.category);
        tvCountry = (TextView) findViewById(R.id.country);
        tvInstructions = (TextView) findViewById(R.id.instructions);
        tvIngredients = (TextView) findViewById(R.id.ingredient);
        btnFav = (Button) findViewById(R.id.btnFav);
        btnShare = (Button) findViewById(R.id.btnShare);
        btmNavigation = (MeowBottomNavigation) findViewById(R.id.bottomNavigation);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recipeList = new ArrayList<>();

        //calling function - to get the recipe details data
        getRecipeData();

        //calling function - to get the related recipe
        showPreferencesRecipe();

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAction();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipe = sendRecipeData();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,recipe);
                intent.setType("text/plain");

                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });

        //calling function - to show bottom navigation
        showBottomNavigation();
    }
    
    private void getRecipeData() {

        DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = DATABASE.getReference("favouriteList");

        // Getting the value from dataSnapshot of the database
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Setting the value to textview once getting the value
            String postkey = bundle.getString("key");
            String imageUri = null;
            imageUri = bundle.getString("Image");
            Picasso.get().load(imageUri).into(imgMeal);
            tvRecipe.setText(bundle.getString("Title"));
            tvCategory.setText(bundle.getString("Category"));
            tvCountry.setText(bundle.getString("Country"));
            tvInstructions.setText(bundle.getString("Instructions"));
            tvIngredients.setText(bundle.getString("Ingredients"));

            //Check the current recipe is matching any key from the database
            //If exist, set the Fav btn to "Saved"
            mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("key")
                    .equalTo(postkey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // onDataChange method - is called when the data is changed in my Firebase console
                    // get the realtime updates in the data
                    // dataSnapshot - contains data from database,
                    // everytime read from database, will receive the data as Datasnapshot.
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                        String key = childSnapshot.getKey();
                        if (postkey.equals(key)){
                            btnFav.setBackgroundResource(R.drawable.ic_favorite_red);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void performAction(){

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Setting the value to textview once getting the value
            String postkey = bundle.getString("key");

            DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
            mDatabaseReference = DATABASE.getReference("favouriteList").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                int check = 0;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dss : dataSnapshot.getChildren()){
                        //will get current user's all recipe in favourite list
                        String key = dss.getKey();
                        if (key.equals(postkey)){
                            check++;
                        }
                    }
                    if(check > 0){
                        deleteRecipeFavourite();
                    }
                    else{
                        saveRecipeFavourite();
                        
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void saveRecipeFavourite(){
        mDatabaseReference = DATABASE.getReference("favouriteList");
        btnFav.setBackgroundResource(R.drawable.ic_favorite_red);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String imgURL = bundle.getString("Image");
            String title = bundle.getString("Title");
            String category = bundle.getString("Category");
            String country = bundle.getString("Country");
            String Instructions = bundle.getString("Instructions");
            String Ingredients = bundle.getString("Ingredients");
            String key = bundle.getString("key");

            RecipeAssistantClass assistantClass = new RecipeAssistantClass(title,Ingredients,Instructions,category,country,imgURL,key);

            mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key)
                    .setValue(assistantClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RecipeDetails.this,"Recipe Added to Favourite Folder!",Toast.LENGTH_SHORT ).show();
                    }
                    else{
                        Toast.makeText(RecipeDetails.this,
                                "Added Recipe Failed! Retry...", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    private void deleteRecipeFavourite(){
        mDatabaseReference = DATABASE.getReference("favouriteList");
        btnFav.setBackgroundResource(R.drawable.ic_favourite_red_border);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String key = bundle.getString("key");
            mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).removeValue();
        }
    }

    private void showPreferencesRecipe(){

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String category = bundle.getString("Category");

            LinearLayoutManager layoutManager = new LinearLayoutManager(RecipeDetails.this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setHasFixedSize(true);

            mPreferencesAdapter = new PreferencesAdapter(RecipeDetails.this, recipeList);
            mRecyclerView.setAdapter(mPreferencesAdapter);

            DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
            mDatabaseReference = DATABASE.getReference("recipes");

            checkCategory = mDatabaseReference.orderByChild("category").equalTo(category);
            checkCategory.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                    recipeModel.setKey(dataSnapshot.getKey());
                    recipeList.add(recipeModel);
                    mPreferencesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public String sendRecipeData() {
        String recipe = "";
        // Getting the value from dataSnapshot of the database
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String Title = bundle.getString("Title");
            String Category = bundle.getString("Category");
            String Country = bundle.getString("Country");
            String Instructions = bundle.getString("Instructions");
            String Ingredients = bundle.getString("Ingredients");

            recipe = "Title : " + Title + "\n" + "Category : " + Category + "\n"
                    + "Country : " + Country + "\n" + "Instructions : " + Instructions
                    + "\n" + "Ingredients : " + Ingredients;
        }
        return recipe;
    }

    public void showBottomNavigation(){
        //Add menu item
        btmNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_search_black));
        btmNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_home_black));
        btmNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_favorite_black));

        btmNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 1:
                        break;
                }
            }
        });

        btmNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 1:
                        Intent intent = new Intent (RecipeDetails.this,SearchAndFilter.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        intent = new Intent (RecipeDetails.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        intent = new Intent (RecipeDetails.this,Profile.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        btmNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //Display message
                //Toast.makeText(getApplicationContext(), "You Reselected" + item.getId(), Toast.LENGTH_SHORT).show();
                switch (item.getId()){
                    case 1:
                        Toast.makeText(getApplicationContext(), "You Reselected - Search", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "You Reselected - Home", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "You Reselected - Profile", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
