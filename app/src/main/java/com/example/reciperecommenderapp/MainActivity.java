package com.example.reciperecommenderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.reciperecommenderapp.Adapter.PreferencesAdapter;
import com.example.reciperecommenderapp.Adapter.SearchAdapter;
import com.example.reciperecommenderapp.Adapter.TrendingAdapter;
import com.example.reciperecommenderapp.Category.FragmentBeef;
import com.example.reciperecommenderapp.Category.RecipeCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Initialize variable
    FirebaseDatabase DATABASE;
    DatabaseReference mDatabaseReference, mCountReference;

    MeowBottomNavigation btmNavigation;
    CardView beefGroup,chickenGroup,dessertGroup,fishGroup,pastaGroup,seafoodGroup,sideGroup,sushiGroup,veganGroup,noodleGroup,westernGroup,brothGroup;
    TextView tvSearch;
    ProgressBar loadingBar;
    RecyclerView mRecyclerView;
    TrendingAdapter mTrendingAdapter;
    List<RecipeAssistantClass> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        btmNavigation = (MeowBottomNavigation) findViewById(R.id.bottomNavigation);
        beefGroup =  (CardView) findViewById(R.id.beefGroup);
        chickenGroup =  (CardView) findViewById(R.id.chickenGroup);
        dessertGroup =  (CardView) findViewById(R.id.dessertGroup);
        fishGroup =  (CardView) findViewById(R.id.fishGroup);
        pastaGroup =  (CardView) findViewById(R.id.pastaGroup);
        seafoodGroup =  (CardView) findViewById(R.id.seafoodGroup);
        sideGroup =  (CardView) findViewById(R.id.sidesGroup);
        sushiGroup =  (CardView) findViewById(R.id.sushiGroup);
        veganGroup =  (CardView) findViewById(R.id.veganGroup);
        noodleGroup =  (CardView) findViewById(R.id.noodlesGroup);
        westernGroup =  (CardView) findViewById(R.id.westernGroup);
        brothGroup =  (CardView) findViewById(R.id.brothGroup);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        recipeList = new ArrayList<>();

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchAndFilter.class);
                startActivity(intent);
            }
        });
        showTrendingRecipe();
        categoryOnclick();
        showBottomNavigation();
    }

    public void showTrendingRecipe(){

        DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = DATABASE.getReference("recipes");
        mCountReference = DATABASE.getReference("trendingCount");

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mTrendingAdapter = new TrendingAdapter(MainActivity.this, recipeList);
        mRecyclerView.setAdapter(mTrendingAdapter);

        Query mDatabaseHighestCount = mCountReference.orderByChild("count").limitToLast(10);
        mDatabaseHighestCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dss:dataSnapshot.getChildren()){
                    String key = dss.getKey();
                    mDatabaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                            recipeModel.setKey(dataSnapshot.getKey());

                            //Add the recipe to the beginning of the list
                            recipeList.add(0,recipeModel);
                            mTrendingAdapter.notifyDataSetChanged();
                            loadingBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void categoryOnclick(){
        //Navigate to Meal Categories
        beefGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RecipeCategory.class);
                intent.putExtra("groupIndex",0);
                startActivity(intent);
            }
        });

        chickenGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RecipeCategory.class);
                intent.putExtra("groupIndex",1);
                startActivity(intent);
            }
        });

        dessertGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RecipeCategory.class);
                intent.putExtra("groupIndex",2);
                startActivity(intent);
            }
        });

        fishGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RecipeCategory.class);
                intent.putExtra("groupIndex",3);
                startActivity(intent);
            }
        });

        pastaGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RecipeCategory.class);
                intent.putExtra("groupIndex",4);
                startActivity(intent);
            }
        });

        seafoodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RecipeCategory.class);
                intent.putExtra("groupIndex",5);
                startActivity(intent);
            }
        });

        sideGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RecipeCategory.class);
                intent.putExtra("groupIndex",6);
                startActivity(intent);
            }
        });

        sushiGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RecipeCategory.class);
                intent.putExtra("groupIndex",7);
                startActivity(intent);
            }
        });

        veganGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RecipeCategory.class);
                intent.putExtra("groupIndex",8);
                startActivity(intent);
            }
        });

        noodleGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RecipeCategory.class);
                intent.putExtra("groupIndex",9);
                startActivity(intent);
            }
        });

        westernGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RecipeCategory.class);
                intent.putExtra("groupIndex",10);
                startActivity(intent);
            }
        });

        brothGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RecipeCategory.class);
                intent.putExtra("groupIndex",11);
                startActivity(intent);
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

        //Set Home Page default selected
        btmNavigation.show(2,true);

        btmNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 1:
                        Intent intent = new Intent (MainActivity.this,SearchAndFilter.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        intent = new Intent (MainActivity.this,Profile.class);
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
