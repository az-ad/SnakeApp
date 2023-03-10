package com.example.snake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RatingActivity extends AppCompatActivity {


    private RatingBar rateRatingBar;
    private EditText commentEditText;
    private ImageView buttonImageView;
    private ListView listView;
    private List<UserComment>userCommentList;
    private CommentCustomAdapter commentCustomAdapter;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        databaseReference = FirebaseDatabase.getInstance().getReference("RatingUser");
        rateRatingBar= findViewById(R.id.rateRatingBar_Id);
        commentEditText= findViewById(R.id.commentEditText_Id);
        buttonImageView= findViewById(R.id.buttonImageView_Id);
        listView = findViewById(R.id.listView_Id);

        userCommentList = new ArrayList<>();
        commentCustomAdapter = new CommentCustomAdapter(RatingActivity.this,userCommentList);
        //loadData();
        buttonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }


        });

    }

//    public void loadData() {
//
//
//    }

    public void saveData() {
        String comment = commentEditText.getText().toString().trim();
        String rating = String.valueOf(rateRatingBar.getRating());

        if (TextUtils.isEmpty(comment)) {
            // if the text fields are empty
            // then show the below message.
            Toast.makeText(RatingActivity.this, "Please add some data.", Toast.LENGTH_SHORT).show();
        }
        else {
            String key = databaseReference.push().getKey();
            UserComment userComment = new UserComment(comment,rating);
            databaseReference.child(key).setValue(userComment);
            Toast.makeText(RatingActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
            commentEditText.setText("");
            float v = 0;
            rateRatingBar.setRating(v);
        }

    }

    @Override
    protected void onStart() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCommentList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren() )
                {
                    UserComment userComment = dataSnapshot1.getValue(UserComment.class);
                    userCommentList.add(userComment);

                }
                listView.setAdapter(commentCustomAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onStart();
    }
}