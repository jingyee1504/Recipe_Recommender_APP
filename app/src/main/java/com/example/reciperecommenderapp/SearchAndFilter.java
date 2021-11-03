package com.example.reciperecommenderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchAndFilter extends AppCompatActivity implements Serializable {

    //Initialize variable

    MeowBottomNavigation btmNavigation;
    ImageButton imgIngredient, imgIngredient1,imgIngredient2,imgIngredient3,imgIngredient4,imgIngredient5,
            imgIngredient6, imgIngredient7,imgIngredient8,imgIngredient9,imgIngredient10,imgIngredient11;
    Button btnSearch;
    TextView tvSearch;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_filter);

        //Assign variable
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btmNavigation =  (MeowBottomNavigation) findViewById(R.id.bottomNavigation);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        imgIngredient = (ImageButton) findViewById(R.id.imgIngredient);
        imgIngredient1 = (ImageButton) findViewById(R.id.imgIngredient2);
        imgIngredient2 = (ImageButton) findViewById(R.id.imgIngredient3);
        imgIngredient3 = (ImageButton) findViewById(R.id.imgIngredient4);
        imgIngredient4 = (ImageButton) findViewById(R.id.imgIngredient5);
        imgIngredient5 = (ImageButton) findViewById(R.id.imgIngredient6);
        imgIngredient6 = (ImageButton) findViewById(R.id.imgIngredient7);
        imgIngredient7 = (ImageButton) findViewById(R.id.imgIngredient8);
        imgIngredient8 = (ImageButton) findViewById(R.id.imgIngredient9);
        imgIngredient9 = (ImageButton) findViewById(R.id.imgIngredient10);
        imgIngredient10 = (ImageButton) findViewById(R.id.imgIngredient11);
        imgIngredient11 = (ImageButton) findViewById(R.id.imgIngredient12);

        List <String> ingredientsList = new ArrayList<String>();


        imgIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgIngredient.getBackground().getConstantState() == getResources().getDrawable(R.color.colorPrimaryDark).getConstantState()){
                    imgIngredient.setBackgroundResource(R.drawable.ic_background_circle);
                    ingredientsList.remove("broccoli");
                }
                else{
                    imgIngredient.setBackgroundResource(R.color.colorPrimaryDark);
                    ingredientsList.add("broccoli");
                }
            }
        });

        imgIngredient1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgIngredient1.getBackground().getConstantState() == getResources().getDrawable(R.color.colorPrimaryDark).getConstantState()){
                    imgIngredient1.setBackgroundResource(R.drawable.ic_background_circle);
                    ingredientsList.remove("butter");
                }
                else{
                    imgIngredient1.setBackgroundResource(R.color.colorPrimaryDark);
                    ingredientsList.add("butter");
                }
            }
        });

        imgIngredient2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgIngredient2.getBackground().getConstantState() == getResources().getDrawable(R.color.colorPrimaryDark).getConstantState()){
                    imgIngredient2.setBackgroundResource(R.drawable.ic_background_circle);
                    ingredientsList.remove("carrot");
                }
                else{
                    imgIngredient2.setBackgroundResource(R.color.colorPrimaryDark);
                    ingredientsList.add("carrot");
                }
            }
        });

        imgIngredient3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgIngredient3.getBackground().getConstantState() == getResources().getDrawable(R.color.colorPrimaryDark).getConstantState()){
                    imgIngredient3.setBackgroundResource(R.drawable.ic_background_circle);
                    ingredientsList.remove("cheese");
                }
                else{
                    imgIngredient3.setBackgroundResource(R.color.colorPrimaryDark);
                    ingredientsList.add("cheese");
                }
            }
        });

        imgIngredient4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgIngredient4.getBackground().getConstantState() == getResources().getDrawable(R.color.colorPrimaryDark).getConstantState()){
                    imgIngredient4.setBackgroundResource(R.drawable.ic_background_circle);
                    ingredientsList.remove("lemon");
                }
                else{
                    imgIngredient4.setBackgroundResource(R.color.colorPrimaryDark);
                    ingredientsList.add("lemon");
                }
            }
        });

        imgIngredient5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgIngredient5.getBackground().getConstantState() == getResources().getDrawable(R.color.colorPrimaryDark).getConstantState()){
                    imgIngredient5.setBackgroundResource(R.drawable.ic_background_circle);
                    ingredientsList.remove("egg");
                }
                else{
                    imgIngredient5.setBackgroundResource(R.color.colorPrimaryDark);
                    ingredientsList.add("egg");
                }
            }
        });

        imgIngredient6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgIngredient6.getBackground().getConstantState() == getResources().getDrawable(R.color.colorPrimaryDark).getConstantState()){
                    imgIngredient6.setBackgroundResource(R.drawable.ic_background_circle);
                    ingredientsList.remove("milk");
                }
                else{
                    imgIngredient6.setBackgroundResource(R.color.colorPrimaryDark);
                    ingredientsList.add("milk");
                }
            }
        });

        imgIngredient7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgIngredient7.getBackground().getConstantState() == getResources().getDrawable(R.color.colorPrimaryDark).getConstantState()){
                    imgIngredient7.setBackgroundResource(R.drawable.ic_background_circle);
                    ingredientsList.remove("mushroom");
                }
                else{
                    imgIngredient7.setBackgroundResource(R.color.colorPrimaryDark);
                    ingredientsList.add("mushroom");
                }
            }
        });

        imgIngredient8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgIngredient8.getBackground().getConstantState() == getResources().getDrawable(R.color.colorPrimaryDark).getConstantState()){
                    imgIngredient8.setBackgroundResource(R.drawable.ic_background_circle);
                    ingredientsList.remove("rice");
                }
                else{
                    imgIngredient8.setBackgroundResource(R.color.colorPrimaryDark);
                    ingredientsList.add("rice");
                }
            }
        });

        imgIngredient9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgIngredient9.getBackground().getConstantState() == getResources().getDrawable(R.color.colorPrimaryDark).getConstantState()){
                    imgIngredient9.setBackgroundResource(R.drawable.ic_background_circle);
                    ingredientsList.remove("onion");
                }
                else{
                    imgIngredient9.setBackgroundResource(R.color.colorPrimaryDark);
                    ingredientsList.add("onion");
                }
            }
        });

        imgIngredient10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgIngredient10.getBackground().getConstantState() == getResources().getDrawable(R.color.colorPrimaryDark).getConstantState()){
                    imgIngredient10.setBackgroundResource(R.drawable.ic_background_circle);
                    ingredientsList.remove("tomato");
                }
                else{
                    imgIngredient10.setBackgroundResource(R.color.colorPrimaryDark);
                    ingredientsList.add("tomato");
                }
            }
        });

        imgIngredient11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgIngredient11.getBackground().getConstantState() == getResources().getDrawable(R.color.colorPrimaryDark).getConstantState()){
                    imgIngredient11.setBackgroundResource(R.drawable.ic_background_circle);
                    ingredientsList.remove("parsley");
                }
                else{
                    imgIngredient11.setBackgroundResource(R.color.colorPrimaryDark);
                    ingredientsList.add("parsley");
                }
            }
        });

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (SearchAndFilter.this, SearchResults.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ingredientsList.size() == 0){
                    Toast.makeText(SearchAndFilter.this, "Please select a ingredient or enter recipe name",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent (SearchAndFilter.this, SearchResults.class);
                    intent.putExtra("Ingredients",(Serializable) ingredientsList);
                    startActivity(intent);
                }
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

        //Set Home Page default selected
        btmNavigation.show(1,true);

        btmNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 1:
                        Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Intent intent = new Intent (SearchAndFilter.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        intent = new Intent(SearchAndFilter.this,Profile.class);
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
                        Toast.makeText(getApplicationContext(), "You Reselected - Profile", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
