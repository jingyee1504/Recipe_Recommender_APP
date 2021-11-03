package com.example.reciperecommenderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4500;

    //Variables
    Animation topAnim, bottomAnim;
    ImageView img_chefIcon;
    TextView title1, title2, subtitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        img_chefIcon =  findViewById(R.id.icon);
        subtitle =  findViewById(R.id.subtitle);
        title1 = findViewById(R.id.title1);
        title2 =  findViewById(R.id.title2);

        img_chefIcon.setAnimation(topAnim);
        subtitle.setAnimation(topAnim);
        title1.setAnimation(bottomAnim);
        title2.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (SplashScreen.this,WelcomePage.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}
