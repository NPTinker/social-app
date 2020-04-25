package com.nptinker.socialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private ImageView mIvProfile;
    private TextView mTvName, mTvUserStatus;
    private EditText mEtMessage;
    private ImageButton mBtnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //initialize
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mRecyclerView = findViewById(R.id.rv_chat);
        mIvProfile = findViewById(R.id.iv_profile);
        mTvName = findViewById(R.id.tv_name_chat);
        mTvUserStatus = findViewById(R.id.tv_user_status);
        mEtMessage = findViewById(R.id.et_message);
        mBtnSend = findViewById(R.id.btn_send);
    }
}
