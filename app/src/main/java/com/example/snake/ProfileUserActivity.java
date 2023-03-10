package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class ProfileUserActivity extends AppCompatActivity {
    private EditText userNameEditText,nameEditText,emailEditText,divisionEditText,phoneNumberEditText;
    private Button editProfileButton;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser=mAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        userNameEditText=findViewById(R.id.userNameEditText_Id);
        nameEditText=findViewById(R.id.nameEditText_Id);
        emailEditText=findViewById(R.id.emailEditText_Id);
        divisionEditText=findViewById(R.id.divisionEditText_Id);
        phoneNumberEditText=findViewById(R.id.userNameEditText_Id);
        editProfileButton=findViewById(R.id.editProfileButton_Id);

    }//end onCreate


}