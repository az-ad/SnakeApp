package com.example.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    //Toolbar toolbar;
    Toolbar toolbar;
    private YouTubePlayerView youTubePlayerView;
    // creating constant keys for shared preferences.
//    public static final String SHARED_PREFS = "shared_prefs";
//
//    // key for storing email.
//    public static final String EMAIL_KEY = "email_key";
//
//    // key for storing password.
//    public static final String PASSWORD_KEY = "password_key";
//
//    // variable for shared preferences.
//    SharedPreferences sharedpreferences;
//    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar1);

        /*---------------------Toolbar-------------------------*/

        //setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        /*----------------Navigation drawer menu------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);



    }//end oncreate

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_login:
                Intent intent;
                intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_signUp:
                intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(getApplicationContext(),ProfileUserActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_classifySnake:
                intent = new Intent(getApplicationContext(),SnakeClassificationActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_rate:
                intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                intent = new Intent(getApplicationContext(),AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_location:
                intent = new Intent(getApplicationContext(),GoogleMapActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:

                // logout
                FirebaseAuth.getInstance().signOut();
                finish();
                 intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}