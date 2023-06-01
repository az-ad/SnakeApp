package com.example.snake;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profileImage;
    ImageView postImage,likeImage,commentsImage;
    TextView nameUser,timeAgo,postDesc,likeCounter,commentsCounter;
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
    }
}
