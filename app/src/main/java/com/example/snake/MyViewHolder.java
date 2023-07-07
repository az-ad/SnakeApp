package com.example.snake;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profileImage;
    ImageView postImage,likeImage,commentsImage,commentSend;
    TextView nameUser,timeAgo,postDesc,likeCounter,commentsCounter;
    EditText inputComments;
    public static  RecyclerView recyclerView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage=itemView.findViewById(R.id.profileImagePost_Id);
        postImage=itemView.findViewById(R.id.postImageView_Id);
        nameUser=itemView.findViewById(R.id.nameUserPost_Id);
        timeAgo=itemView.findViewById(R.id.timeAgo_Id);
        postDesc=itemView.findViewById(R.id.postDesc_ID);
        likeImage=itemView.findViewById(R.id.likeImageView_Id);
        commentsImage=itemView.findViewById(R.id.commentsImageView_Id);
        likeCounter=itemView.findViewById(R.id.likeCount_Id);
        commentsCounter=itemView.findViewById(R.id.commentsCounter_Id);
        commentSend=itemView.findViewById(R.id.sendCommestImageView_id);
        inputComments=itemView.findViewById(R.id.inputComments_id);
        recyclerView=itemView.findViewById(R.id.recylerViewComments_id);
    }

    public void countLikes(String postKey, String uid, DatabaseReference likeRef) {
        likeRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    int totalLikes= (int) snapshot.getChildrenCount();
                    likeCounter.setText(totalLikes+"");
                }
                else {
                    likeCounter.setText("");
                }
                if(snapshot.child(uid).exists()){
                    likeImage.setColorFilter(Color.RED);
                }
                else {
                    likeImage.setColorFilter(Color.GRAY);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        likeRef.child(postKey).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.child(uid).exists()){
//                    likeImage.setColorFilter(Color.RED);
//                }
//                else {
//                    likeImage.setColorFilter(Color.GRAY);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void countComments(String postKey, String uid, DatabaseReference commentRef) {

        commentRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    int totalComments= (int) snapshot.getChildrenCount();
                    commentsCounter.setText(totalComments+"");
                }
                else {
                    commentsCounter.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
