package com.nptinker.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nptinker.socialapp.adapters.AdapterPosts;
import com.nptinker.socialapp.models.ModelPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TheirProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    RecyclerView rvPost;

    private ImageView mIvAvatar, mIvCover;
    private TextView mTvName, mTvEmail, mTvPhone;

    List<ModelPost> postList;
    AdapterPosts adapterPosts;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_their_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mIvAvatar = findViewById(R.id.iv_avatar);
        mIvCover = findViewById(R.id.iv_cover);
        mTvName = findViewById(R.id.tv_name);
        mTvEmail = findViewById(R.id.tv_email);
        mTvPhone = findViewById(R.id.tv_phone);

        rvPost = findViewById(R.id.rv_post);
        firebaseAuth = FirebaseAuth.getInstance();

        //get uid clicked user
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        //get info of current user
        Query query = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get data
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue(); // gonna change
                    String cover = "" + ds.child("cover").getValue(); // gonna change

                    //Set data
                    mTvName.setText(name);
                    mTvEmail.setText(email);
                    mTvPhone.setText(phone);
                    try {
                        //if image is received
                        Picasso.get().load(image).into(mIvAvatar);
                    } catch (Exception e){
                        //any exception = set default
                        Picasso.get().load(R.drawable.ic_add_image).into(mIvAvatar);
                    }
                    try {
                        //if image is received
                        Picasso.get().load(cover).into(mIvCover);
                    } catch (Exception e){
                        //any exception = set default
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postList = new ArrayList<>();

        checkUserStatus();
        loadTheirPosts();
    }

    private void loadTheirPosts() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //show newest posts first
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recycler view
        rvPost.setLayoutManager(layoutManager);

        //init posts list
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = reference.orderByChild("uid").equalTo(uid);

        //get all data from reference
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost myPost = ds.getValue(ModelPost.class);

                    //push to the list
                    postList.add(myPost);

                    adapterPosts = new AdapterPosts(TheirProfileActivity.this, postList);
                    rvPost.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TheirProfileActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchTheirPosts(final String searchQuery) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(TheirProfileActivity.this);
        //show newest posts first
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recycler view
        rvPost.setLayoutManager(layoutManager);

        //init posts list
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = reference.orderByChild("uid").equalTo(uid);

        //get all data from reference
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost myPost = ds.getValue(ModelPost.class);

                    if (myPost.getpTitle().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            myPost.getpDescription().toLowerCase().contains(searchQuery.toLowerCase())) {
                        //push to the list
                        postList.add(myPost);

                    }
                    adapterPosts = new AdapterPosts(TheirProfileActivity.this, postList);
                    rvPost.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TheirProfileActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user !=null){
            //mTvProfile.setText(user.getEmail());
            //uid = user.getUid();
        } else {
            //move to MainActivity if not logged in
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_add_post).setVisible(false);

        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView  = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)){
                    searchTheirPosts(query);
                } else {
                    loadTheirPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    searchTheirPosts(newText);
                } else {
                    loadTheirPosts();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
