package com.example.snake;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profileImage;
    TextView userName,comment;
    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage=itemView.findViewById(R.id.profileImage_comment_id);
        userName=itemView.findViewById(R.id.userNameComment_id);
        comment=itemView.findViewById(R.id.commentsTextView_id);
    }
}
