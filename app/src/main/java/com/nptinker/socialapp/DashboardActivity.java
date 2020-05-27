package com.nptinker.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class DashboardActivity extends AppCompatActivity {

    private String mUID;

    private FirebaseAuth mAuth;
    private BottomNavigationView mBottomNavigationView;
    private ActionBar actionBar;
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //handle item click
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            actionBar.setTitle("Home");
                            HomeFragment fragment1 = new HomeFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content,fragment1,"");
                            ft1.commit();
                            return true;
                        case R.id.nav_users:
                            actionBar.setTitle("User list");
                            UsersFragment fragment2 = new UsersFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content,fragment2,"");
                            ft2.commit();
                            return true;
                        case R.id.nav_profile:
                            actionBar.setTitle("Profile");
                            ProfileFragment fragment3 = new ProfileFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content,fragment3,"");
                            ft3.commit();
                            return true;
                        case R.id.nav_chat:
                            actionBar.setTitle("Chat");
                            ChatListFragment fragment4 = new ChatListFragment();
                            FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                            ft4.replace(R.id.content,fragment4,"");
                            ft4.commit();
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Set actionbar and the title
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Profile");

        //initialize
        mAuth = FirebaseAuth.getInstance();
        mBottomNavigationView = findViewById(R.id.navigation);

        //click event for nav
        mBottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);

        //homeFragment default
        actionBar.setTitle("Home");
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment1,"");
        ft1.commit();

        checkUserStatus();
    }

   @Override
    public void onStart() {
        checkUserStatus();
        super.onStart();

   }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    public void updateToken (String token){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
//        Token token = new Token(token);
//        reference.child(mUID).setValue(mToken);
//    }

    private void checkUserStatus(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user !=null){
            //mTvProfile.setText(user.getEmail());

            mUID = user.getUid();

//            SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("Current_USERID", mUID);
//            editor.apply();
//
//            updateToken(FirebaseInstanceId.getInstance().getToken());
        } else {
            //move to MainActivity if not logged in
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();
        }
    }
}
