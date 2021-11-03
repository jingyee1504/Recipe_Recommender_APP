package com.example.reciperecommenderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.reciperecommenderapp.Adapter.SearchAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchResults extends AppCompatActivity {

    //Initialize variable
    FirebaseDatabase DATABASE;
    DatabaseReference mDatabaseReference;

    MeowBottomNavigation btmNavigation;
    EditText etSearch;
    List<RecipeAssistantClass> recipeList;
    SearchAdapter searchAdapter;
    RecyclerView recipeRecyclerView;
    TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        //Assign variable
        btmNavigation =  (MeowBottomNavigation) findViewById(R.id.bottomNavigation);
        etSearch = (EditText) findViewById(R.id.etSearch);
        recipeRecyclerView = (RecyclerView) findViewById(R.id.list_ex);
        tvMessage = (TextView) findViewById(R.id.tvMessage);

        recipeList = new ArrayList<>();
        DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = DATABASE.getReference("recipes");

        List<String> ingredientList = new ArrayList<String>();

        ingredientList = (List<String>) getIntent().getSerializableExtra("Ingredients");

        if(ingredientList != null){
            getIngredientData(ingredientList);
        }

        else{
            performSearch("");
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                    performSearch(editable.toString());
                }
                else{
                    performSearch("");
                }
            }
        });

        showBottomNavigation();
    }

    private void getIngredientData(List<String> ingredientList){

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            boolean found;
            int numFound, totalFound = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String ingredients = ds.child("ingredients").getValue(String.class);

                    numFound = 0;

                    //display all recipe that is containing ingredient 1, or ingredient 2, ....
                    for(int i = 0; i < ingredientList.size(); i++) {
                        found = ingredients.contains(ingredientList.get(i));

                        if(found){
                            numFound++;
                        }
                    }

                    //if wan find all recipe that is containing ingredient 1, or ingredient 2, ....// use this
                    if (numFound == ingredientList.size()) {
                        RecipeAssistantClass recipeModel = ds.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(ds.getKey());
                        recipeList.add(recipeModel);
                        totalFound++;
                    }
                }

                //If no matching, then show "Cannot found" message
                if(totalFound == 0){
                    tvMessage.setVisibility(View.VISIBLE);
                }

                searchAdapter = new SearchAdapter(getApplicationContext(),recipeList);

                //SpanCount - Split into 2 columns
                GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);

                recipeRecyclerView.setHasFixedSize(true);
                recipeRecyclerView.setLayoutManager(mGridLayoutManager);
                recipeRecyclerView.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void performSearch(String editable){
        Query query = mDatabaseReference.orderByChild("recipeName").startAt(editable).endAt(editable + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    tvMessage.setVisibility(View.INVISIBLE);
                    recipeList.clear();

                    for(DataSnapshot dss: dataSnapshot.getChildren()){
                        RecipeAssistantClass recipeModel = dss.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dss.getKey());
                        recipeList.add(recipeModel);
                    }

                    searchAdapter = new SearchAdapter(getApplicationContext(),recipeList);

                    //SpanCount - Split into 2 columns
                    GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);

                    recipeRecyclerView.setHasFixedSize(true);
                    recipeRecyclerView.setLayoutManager(mGridLayoutManager);
                    recipeRecyclerView.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();
                }
                else{

                    //if cannot found
                    tvMessage.setVisibility(View.VISIBLE);
                    recipeList.clear();

                    searchAdapter = new SearchAdapter(getApplicationContext(),recipeList);

                    //SpanCount - Split into 2 columns
                    GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);

                    recipeRecyclerView.setHasFixedSize(true);
                    recipeRecyclerView.setLayoutManager(mGridLayoutManager);
                    recipeRecyclerView.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                        Intent intent = new Intent (SearchResults.this,SearchAndFilter.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent (SearchResults.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent (SearchResults.this,Profile.class);
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
                        Toast.makeText(getApplicationContext(), "You Reselected - Favourite Folder", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
