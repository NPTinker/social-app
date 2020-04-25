package com.nptinker.socialapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nptinker.socialapp.ChatActivity;
import com.nptinker.socialapp.R;
import com.nptinker.socialapp.models.ModelUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyHolder> {

    Context context;
    List<ModelUser> userList;

    public AdapterUser(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout (row_user.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String userUid = userList.get(position).getUid();
        String userImage = userList.get(position).getImage();
        String userName = userList.get(position).getName();
        final String userEmail = userList.get(position).getEmail();

        //set data
        holder.mTvName.setText(userName);
        holder.mTvEmail.setText(userEmail);
        try {
            Picasso.get().load(userImage).placeholder(
                    R.drawable.ic_default_img).into(holder.mIvAvatar);
        } catch (Exception e) {

        }
        //handle item being clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userUid", userUid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mIvAvatar;
        TextView mTvName, mTvEmail;

        public MyHolder (View itemView) {
            super(itemView);

            mIvAvatar = itemView.findViewById(R.id.iv_avatar2);
            mTvName = itemView.findViewById(R.id.tv_name2);
            mTvEmail = itemView.findViewById(R.id.tv_email2);
        }
    }
}
