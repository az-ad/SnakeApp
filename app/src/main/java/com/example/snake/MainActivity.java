package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;
    Animation topAnim;
    Animation bottomAnim;
    ImageView image;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        //setting animation time
        topAnim.setDuration(2000);
        bottomAnim.setDuration(2000);

        //find the widget of splash screen by id
        image=(ImageView) findViewById(R.id.imageView);
        textView =(TextView)findViewById(R.id.textView);

        //setting animation element
        image.setAnimation(topAnim);
        textView.setAnimation(bottomAnim);

        //after animation end
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                //intent = new Intent(getApplicationContext(),HomeActivity.class);
                intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}

