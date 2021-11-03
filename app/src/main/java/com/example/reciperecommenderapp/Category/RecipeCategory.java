package com.example.reciperecommenderapp.Category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.reciperecommenderapp.Adapter.FragmentAdapter;
import com.example.reciperecommenderapp.FavouriteRecipeFolder;
import com.example.reciperecommenderapp.MainActivity;
import com.example.reciperecommenderapp.Profile;
import com.example.reciperecommenderapp.R;
import com.example.reciperecommenderapp.SearchAndFilter;
import com.example.reciperecommenderapp.SplashScreen;
import com.google.android.material.tabs.TabLayout;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager2.widget.ViewPager2;

public class RecipeCategory extends AppCompatActivity {
    //Initialize variable
    TabLayout categoryTab;
    ViewPager2 mPager;
    FragmentAdapter mAdapter;
    MeowBottomNavigation btmNavigation;
    String[] categoryArray;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_category);

        //Assign variable
        categoryArray = getResources().getStringArray(R.array.category);
        categoryTab = findViewById(R.id.tabLayout);
        mPager = findViewById(R.id.viewPager);
        btmNavigation = findViewById(R.id.bottomNavigation);

        FragmentManager fm = getSupportFragmentManager();
        mAdapter = new FragmentAdapter(fm,getLifecycle());
        mPager.setAdapter(mAdapter);

        //setOffscreenPageLimit - set the limit for the no.of tabs in one page
        mPager.setOffscreenPageLimit(4);

        //Add new category from Category Array in String.xml
        for (int i = 0; i < categoryArray.length; i++) {
            categoryTab.addTab(categoryTab.newTab().setText(categoryArray[i]));
        }

        int fragmentID = getIntent().getExtras().getInt("groupIndex");

        //set the default selected from home page
        mPager.setCurrentItem(fragmentID,true);
        categoryTab.getTabAt(fragmentID).select();

        //click on tab
        categoryTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        //When swiping, make the particular tab selected using viewpager2
        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                categoryTab.selectTab(categoryTab.getTabAt(position));
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
                        Intent intent = new Intent (RecipeCategory.this, SearchAndFilter.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        intent = new Intent (RecipeCategory.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        intent = new Intent (RecipeCategory.this, Profile.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        btmNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //Display message
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
