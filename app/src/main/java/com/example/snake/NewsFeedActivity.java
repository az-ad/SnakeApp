package com.example.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsFeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;
    String profileimageUrlv,nameV,emailV;
    CircleImageView profileImageHeader;
    TextView nameHeader,emailHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar1);

//        /*---------------------Toolbar-------------------------*/
//
//        //setSupportActionBar(toolbar);
        //setSupportActionBar(toolbar);
//
//        /*----------------Navigation drawer menu------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        navigationView.setCheckedItem(R.id.nav_home);

        //-----------------user name & profile image setting-------------------------//
        View view= navigationView.inflateHeaderView(R.layout.header);
        profileImageHeader=view.findViewById(R.id.profileImageHeader_Id);
        nameHeader=view.findViewById(R.id.nameHeader_Id);
        emailHeader=view.findViewById(R.id.emailHeader_Id);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mUserRef= FirebaseDatabase.getInstance().getReference().child("Users");
    } // end oncreate

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
                    Toast.makeText(NewsFeedActivity.this, "Sorry!Something going wrong", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void sendUserToLoginActivity() {
        Intent intent=new Intent(NewsFeedActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //------------------------end of user proile header---------------//

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