package com.example.snake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUserActivity extends AppCompatActivity {
    private EditText nameEditText,emailEditText,professionEditText,divisionEditText,phoneNumberEditText;
    private Button saveProfileButton;
    private CircleImageView profileImageView;
    Uri imageUri;
    //DatabaseReference databaseReference;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser=mAuth.getInstance().getCurrentUser();
    StorageReference StorageRef;

    private static final int REQUEST_CODE = 101;
    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);


        nameEditText=findViewById(R.id.nameEditText_Id);
        emailEditText=findViewById(R.id.emailEditText_Id);
        professionEditText=findViewById(R.id.professionEditText_Id);
        divisionEditText=findViewById(R.id.divisionEditText_Id);
        phoneNumberEditText=findViewById(R.id.phoneNumberEditText_Id);
        saveProfileButton=findViewById(R.id.saveProfileButton_Id);
        profileImageView=findViewById(R.id.profileImageView_Id);
        mLoadingBar=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mRef= FirebaseDatabase.getInstance().getReference().child("Users");
        StorageRef= FirebaseStorage.getInstance().getReference().child("ProfileImages");

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });
    }//end onCreate

    private void SaveData() {
        String name=nameEditText.getText().toString();
        String email=emailEditText.getText().toString();
        String profession=professionEditText.getText().toString();
        String division=divisionEditText.getText().toString();
        String phoneNumber=phoneNumberEditText.getText().toString();

        if(name.isEmpty() || name.length()<3){
            showError(nameEditText,"Minimum length of the name should be at least 3");
        }
        else if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showError(emailEditText,"Email is not valid");
        }
        else if(profession.isEmpty()){
            showError(professionEditText,"Enter profession");
        }
        else if(division.isEmpty()){
            showError(divisionEditText,"Enter division");
        }
        else if(phoneNumber.isEmpty() || phoneNumber.length()!=11){
            showError(phoneNumberEditText,"phone number is not valid");
        }
        else if(imageUri==null){
            Toast.makeText(this,"Please select an image",Toast.LENGTH_SHORT).show();
        }
        else{
            mLoadingBar.setTitle("adding Setup Profile");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            StorageRef.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        StorageRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap=new HashMap();
                                hashMap.put("name",name);
                                hashMap.put("email",email);
                                hashMap.put("profession",profession);
                                hashMap.put("division",division);
                                hashMap.put("phoneNumber",phoneNumber);
                                hashMap.put("profileImage",uri.toString());
                                hashMap.put("status","offline");

                                mRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Intent intent=new Intent(ProfileUserActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                        mLoadingBar.dismiss();
                                        Toast.makeText(ProfileUserActivity.this, "Setup profile complete", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mLoadingBar.dismiss();
                                        Toast.makeText(ProfileUserActivity.this, "", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            imageUri =data.getData();
            profileImageView.setImageURI(imageUri);

        }
    }
}