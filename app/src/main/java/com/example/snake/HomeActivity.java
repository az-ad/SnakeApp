package com.example.snake;

//import static com.example.snake.LoginActivity.SHARED_PREFS;

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
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    //Toolbar toolbar;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;
    String profileimageUrlv,nameV,emailV;
    CircleImageView profileImageHeader;
    TextView nameHeader,emailHeader;
//    public static final String SHARED_PREFS ="sharedPrefs";
    WebView webView;
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar1);

//        webView = findViewById(R.id.aboutus);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("file:///android_asset/index.html");
        /*---------------------Toolbar-------------------------*/

        //setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
    //-------------new action bar--------------//
//        getSupportActionBar().setTitle("Snake App");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);

        /*----------------Navigation drawer menu------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

        //--------------for youTube video play------------------------------------------//
        youTubePlayerView = findViewById(R.id.activity_main_youtubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "bZYPI4mYwhw";
                youTubePlayer.loadVideo(videoId, 0);
                //youTubePlayer.cueVideo(videoId, 0);
            }
        });



        //-----------------user name & profile image setting-------------------------//
        View view= navigationView.inflateHeaderView(R.layout.header);
        profileImageHeader=view.findViewById(R.id.profileImageHeader_Id);
        nameHeader=view.findViewById(R.id.nameHeader_Id);
        emailHeader=view.findViewById(R.id.emailHeader_Id);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mUserRef= FirebaseDatabase.getInstance().getReference().child("Users");

    }//end oncreate


    @Override
    protected void onStart() {
        super.onStart();
        if(mUser==null){
            sendUserToLoginActivity();
        }
        else{
            mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        profileimageUrlv=dataSnapshot.child("profileImage").getValue().toString();
                        nameV=dataSnapshot.child("name").getValue().toString();
                        emailV=dataSnapshot.child("email").getValue().toString();
                        Picasso.get().load(profileimageUrlv).into(profileImageHeader);
                        nameHeader.setText(nameV);
                        emailHeader.setText(emailV);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomeActivity.this, "Sorry!Something going wrong", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void sendUserToLoginActivity() {
        Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

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
            case R.id.nav_verifyEmail:
                intent = new Intent(getApplicationContext(),EmailVerificationActivity.class);
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
            case R.id.nav_newsFeed:
                intent = new Intent(getApplicationContext(),NewsFeedActivity.class);
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
                SharedPreferences sharedPreferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("remember","false");
                editor.apply();
                finish();
//                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("name","");
//                editor.apply();
//                FirebaseAuth.getInstance().signOut();
//                finish();
                 intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId()== android.R.id.home){
//            drawerLayout.openDrawer(GravityCompat.START);
//            return true;
//        }
//        return true;
//    }
}