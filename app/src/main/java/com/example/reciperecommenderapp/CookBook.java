package com.example.reciperecommenderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.reciperecommenderapp.Adapter.CategoryRecipeAdapter;
import com.example.reciperecommenderapp.Adapter.CookbookAdapter;
import com.example.reciperecommenderapp.Adapter.FavouriteAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CookBook extends AppCompatActivity {

    //Initialize variable
    FirebaseDatabase DATABASE;
    DatabaseReference mDatabaseReference;

    MeowBottomNavigation btmNavigation;
    RecyclerView cookbookRecyclerView;
    List<RecipeAssistantClass> cookbookList;
    CookbookAdapter cookbookAdapter;
    Button btnDelete;
    ImageView btnCreateRecipe,btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_book);

        //Assign variable
        cookbookRecyclerView = (RecyclerView) findViewById(R.id.favouriteRecyclerView);
        btmNavigation =  (MeowBottomNavigation) findViewById(R.id.bottomNavigation);
        btnCreateRecipe = (ImageView) findViewById(R.id.btnCreateRecipe);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        cookbookList = new ArrayList<>();
        cookbookAdapter = new CookbookAdapter(getApplicationContext(),cookbookList);

        //SpanCount - Split into 2 columns
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);

        cookbookRecyclerView.setHasFixedSize(true);
        cookbookRecyclerView.setLayoutManager(mGridLayoutManager);
        cookbookRecyclerView.setAdapter(cookbookAdapter);

        DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = DATABASE.getReference("cookbook");
        Query checkCookbookUser;

        checkCookbookUser = mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        checkCookbookUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                recipeModel.setKey(dataSnapshot.getKey());
                cookbookList.add(recipeModel);
                cookbookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();

                //Remove the recipe with the given key
                for(RecipeAssistantClass ra : cookbookList){
                    if(ra.getKey().equals(key)){
                        cookbookList.remove(ra);
                        cookbookAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnCreateRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent (CookBook.this,CreateRecipe.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CookBook.this,Profile.class);
                startActivity(intent);
            }
        });

        showBottomNavigation();
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
                        Intent intent = new Intent (CookBook.this,SearchAndFilter.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        intent = new Intent(CookBook.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        intent = new Intent(CookBook.this,Profile.class);
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
