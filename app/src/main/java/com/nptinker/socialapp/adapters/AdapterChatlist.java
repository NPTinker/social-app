package com.nptinker.socialapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nptinker.socialapp.ChatActivity;
import com.nptinker.socialapp.R;
import com.nptinker.socialapp.models.ModelUser;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterChatlist extends RecyclerView.Adapter<AdapterChatlist.MyHolder>{

    Context context;
    List<ModelUser> userList;
    private HashMap<String,String> lastMessageMap;

    public AdapterChatlist(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
        lastMessageMap = new HashMap<>();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        final String hisUid = userList.get(position).getUid();
        String userImage = userList.get(position).getImage();
        String userName = userList.get(position).getName();
        String lastMessage = lastMessageMap.get(hisUid);

        //set data
        holder.tvName.setText(userName);
        if (lastMessage == null || lastMessage.equals("default")) {
            holder.tvLastMessage.setVisibility(View.GONE);
        } else {
            holder.tvLastMessage.setVisibility(View.VISIBLE);
            holder.tvLastMessage.setText(lastMessage);
        }
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.ic_default_img).into(holder.ivProfile);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.ic_default_img).into(holder.ivProfile);
        }
        //set status of other users in chatlist
        if (userList.get(position).getOnlineStatus().equals("online")){
            holder.ivOnlineStatus.setImageResource(R.drawable.circle_online);
        } else {
            holder.ivOnlineStatus.setImageResource(R.drawable.circle_offline);
        }

        //click on user
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userUid", hisUid);
                context.startActivity(intent);
            }
        });
    }

    public void setLastMessageMap(String userId, String lastMessage){
        lastMessageMap.put(userId,lastMessage);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        //row_chatlist.xml
        ImageView ivProfile, ivOnlineStatus;
        TextView tvName, tvLastMessage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            ivProfile = itemView.findViewById(R.id.iv_profile);
            ivOnlineStatus = itemView.findViewById(R.id.iv_online_status);
            tvName = itemView.findViewById(R.id.tv_name);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
        }
    }
}
