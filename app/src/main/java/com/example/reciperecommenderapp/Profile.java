package com.example.reciperecommenderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase DATABASE;
    DatabaseReference mDatabaseReference;

    MeowBottomNavigation btmNavigation;
    CardView cvFavourite, cvCookbook;
    ImageView imgCameraIcon2,imgProfilePhoto;
    TextView tvName, tvEmail;
    FloatingActionButton btnFloating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        cvFavourite = findViewById(R.id. cvFavourite);
        cvCookbook =  findViewById(R.id. cvCookbook);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        btmNavigation =  findViewById(R.id.bottomNavigation);
        btnFloating = findViewById(R.id.btnFloating);

        mAuth = FirebaseAuth.getInstance();

        DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = DATABASE.getReference("users");

        showUserData();

        cvFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Profile.this,FavouriteRecipeFolder.class);
                startActivity(intent);
            }
        });

        cvCookbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Profile.this,CookBook.class);
                startActivity(intent);
            }
        });

        btnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(Profile.this,Login.class);
                startActivity(intent);
            }
        });

        showBottomNavigation();
    }

    private void showUserData(){

        Query checkUser = mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String NameDB = dataSnapshot.child("name").getValue(String.class);
                    String EmailDB = dataSnapshot.child("email").getValue(String.class);

                    tvName.setText(NameDB);
                    tvEmail.setText(EmailDB);
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

        //Set Profile default selected
        btmNavigation.show(3,true);

        btmNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 1:
                        Intent intent = new Intent (Profile.this,SearchAndFilter.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        intent = new Intent (Profile.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(),"Profile",Toast.LENGTH_SHORT).show();
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
