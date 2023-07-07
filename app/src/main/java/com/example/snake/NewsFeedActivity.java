package com.example.snake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snake.Utills.Comment;
import com.example.snake.Utills.Posts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsFeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef,postRef,likeRef,commentRef;
    StorageReference  postImageRef;
    String profileimageUrlv,nameV,emailV;
    CircleImageView profileImageHeader;
    TextView nameHeader,emailHeader;
    ImageView addImagePost,sendImagePost;
    EditText inputPostDesc;
    Uri imageUri;
    ProgressDialog mLoadingBar;
    FirebaseRecyclerAdapter<Posts,MyViewHolder>adapter;
    FirebaseRecyclerOptions<Posts>options;
    RecyclerView recyclerView;
    private static final int REQUEST_CODE = 101;
    FirebaseRecyclerOptions<Comment>CommentOption;
    FirebaseRecyclerAdapter<Comment,CommentViewHolder>CommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar1);
        addImagePost=findViewById(R.id.addImagePost_Id);
        sendImagePost=findViewById(R.id.sendPostImageView_Id);
        inputPostDesc=findViewById(R.id.inputAddPost_Id);
        mLoadingBar=new ProgressDialog(this);
        recyclerView=findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        postRef= FirebaseDatabase.getInstance().getReference().child("Posts");
        likeRef=FirebaseDatabase.getInstance().getReference().child("Likes");
        commentRef=FirebaseDatabase.getInstance().getReference().child("Comments");
        postImageRef= FirebaseStorage.getInstance().getReference().child("PostImages");
        //-------------------send button--------------------------------------//
        sendImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPost();
            }
        });
        //------------open file chooser & select image-----------//
        addImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        loadPost();
    } // end oncreate

    private void loadPost() {
        options=new  FirebaseRecyclerOptions.Builder<Posts>().setQuery(postRef,Posts.class).build();
        adapter = new FirebaseRecyclerAdapter<Posts, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Posts model) {
               String postKey=getRef(position).getKey();
                holder.postDesc.setText(model.getPostDesc());
               // holder.timeAgo.setText(model.getDatePost());
                String timeAgo = calculateTimeAgo(model.getDatePost());
                holder.timeAgo.setText(timeAgo);
                holder.nameUser.setText(model.getName());
                Picasso.get().load(model.getPostImageUrl()).into(holder.postImage);
                Picasso.get().load(model.getUserProfileImageUrl()).into(holder.profileImage);
                holder.countLikes(postKey,mUser.getUid(),likeRef);
                holder.countComments(postKey,mUser.getUid(),commentRef);
                holder.likeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeRef.child(postKey).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    likeRef.child(postKey).child(mUser.getUid()).removeValue();
                                    holder.likeImage.setColorFilter(Color.GRAY);
                                    notifyDataSetChanged();
                                }
                                else {
                                    likeRef.child(postKey).child(mUser.getUid()).setValue("like");
                                    holder.likeImage.setColorFilter(Color.RED);
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(NewsFeedActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                holder.commentSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = holder.inputComments.getText().toString();
                        if (comment.isEmpty()){
                            Toast.makeText(NewsFeedActivity.this, "Please write something", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            AddComment(holder,postKey,commentRef,mUser.getUid(),comment);
                        }
                    }
                });

                LoadComment(postKey);
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_post,parent,false);
                return new MyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void LoadComment(String postKey) {
        MyViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(NewsFeedActivity.this));
        CommentOption=new  FirebaseRecyclerOptions.Builder<Comment>().setQuery(commentRef.child(postKey),Comment.class).build();
        CommentAdapter= new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(CommentOption) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment model) {
                Picasso.get().load(model.getProfileImageUrl()).into(holder.profileImage);
                holder.userName.setText(model.getUsername());
                holder.comment.setText(model.getComment());

            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_comment,parent,false);
                return new CommentViewHolder(view);
            }
        };
        CommentAdapter.startListening();
        MyViewHolder.recyclerView.setAdapter(CommentAdapter);
    }

    private void AddComment(MyViewHolder holder, String postKey, DatabaseReference commentRef, String uid, String comment) {
        HashMap hashMap = new HashMap();
        hashMap.put("username",nameV);
        hashMap.put("profileImageUrl",profileimageUrlv);
        hashMap.put("comment",comment);
        commentRef.child(postKey).child(uid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(NewsFeedActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    holder.inputComments.setText("");
                }
                else {
                    Toast.makeText(NewsFeedActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private String calculateTimeAgo(String datePost) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        try {
            long time = sdf.parse(datePost).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return  ago+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    //---------------for select image------------//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            imageUri =data.getData();
            addImagePost.setImageURI(imageUri);

        }
    }
    private void AddPost() {
        String postDesc=inputPostDesc.getText().toString();
        if(postDesc.isEmpty() || postDesc.length()<3)
        {
            inputPostDesc.setError("Please write something in Post Description");
        }
        else if(imageUri==null){
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
        else{
            mLoadingBar.setTitle("Adding Post");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            String strDate = formatter.format(date);
            //-------------store image to database----------------//
            postImageRef.child(mUser.getUid()+strDate).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        //---------store the data into database----//
                        postImageRef.child(mUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                HashMap hashMap=new HashMap();
                                hashMap.put("datePost",strDate);
                                hashMap.put("postImageUrl",uri.toString());
                                hashMap.put("postDesc",postDesc);
                                hashMap.put("userProfileImageUrl",profileimageUrlv);
                                hashMap.put("name",nameV);
                                postRef.child(mUser.getUid()+strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            mLoadingBar.dismiss();
                                            Toast.makeText(NewsFeedActivity.this, "Post Added", Toast.LENGTH_SHORT).show();
                                            addImagePost.setImageResource(R.drawable.ic_add_post_image);
                                            inputPostDesc.setText("");
                                        }
                                        else {
                                            mLoadingBar.dismiss();
                                            Toast.makeText(NewsFeedActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                    else {
                        mLoadingBar.dismiss();
                        Toast.makeText(NewsFeedActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

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