package com.example.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText loginEmailEditText,loginPasswordEditText;
    private Button loginButton;
    private TextView signUpTextView;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
//------------------session management--------------------------//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginEmailEditText=findViewById(R.id.loginEmailEditText_Id);
        loginPasswordEditText=findViewById(R.id.loginPasswordeditText_Id);
        loginButton=findViewById(R.id.loginButton_Id);
        signUpTextView=findViewById(R.id.needAccountId);
        progressBar = findViewById(R.id.progressbarId);

        signUpTextView.setOnClickListener((View.OnClickListener) this);
        loginButton.setOnClickListener((View.OnClickListener) this);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.loginButton_Id:
                userLogin();
                break;
            case R.id.needAccountId:
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void userLogin() {
        String email= loginEmailEditText.getText().toString().trim();
        String password= loginPasswordEditText.getText().toString().trim();

        //checking the validity of the email
        if(email.isEmpty())
        {
            loginEmailEditText.setError("Enter an email address");
            loginEmailEditText.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            loginEmailEditText.setError("Enter a valid email address");
            loginEmailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            loginPasswordEditText.setError("Enter a password");
            loginPasswordEditText.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            loginPasswordEditText.setError("Minimum length of a password should be 6");
            loginPasswordEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                        //Toast.makeText(getApplicationContext(),"Login unsuccessful",Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(getApplicationContext(),"Login unsuccessful",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
    //--------------------session management---------------------------------//
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user =  mAuth.getCurrentUser();
        if(user != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

}