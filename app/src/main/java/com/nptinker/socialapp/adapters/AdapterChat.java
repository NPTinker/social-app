package com.nptinker.socialapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nptinker.socialapp.R;
import com.nptinker.socialapp.models.ModelChat;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<ModelChat> chatList;
    private String imageUrl;

    FirebaseUser firebaseUser;

    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false );
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false );
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        //get data
        String message = chatList.get(position).getMessage();
        String timeStamp = chatList.get(position).getTimestamp();
        String type = chatList.get(position).getType();
        Log.d("Time", timeStamp);

        //convert time
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        if(type.equals("text")){
            holder.mTvMessage.setVisibility(View.VISIBLE);
            holder.ivMessage.setVisibility(View.GONE);
            holder.mTvMessage.setText(message);
        } else {
            holder.mTvMessage.setVisibility(View.GONE);
            holder.ivMessage.setVisibility(View.VISIBLE);

            Picasso.get().load(message).placeholder(R.drawable.ic_image_black).into(holder.ivMessage);
        }

        //set data
        holder.mTvMessage.setText(message);
        holder.mTvTime.setText(dateTime);
        try {
            Picasso.get().load(imageUrl).into(holder.mIvProfile);
        } catch (Exception e) {

        }
        //click to show delete dialog
        holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Warning: Delete!");
                builder.setMessage("Are you sure to delete this message?");
                //delete button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(position);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //create and show dialog
                builder.create().show();
            }
        });

        //set seen status
        if (position == chatList.size()-1) {
            if (chatList.get(position).isSeen()){
                holder.mTvIsSeen.setText("Seen");
            } else {
                holder.mTvIsSeen.setText("Sent");
            }
        } else {
            holder.mTvIsSeen.setVisibility(View.GONE);
        }

    }

    private void deleteMessage(int position) {
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String msgTimestamp = chatList.get(position).getTimestamp();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = databaseReference.orderByChild("timestamp").equalTo(msgTimestamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    // compare uid
                    if (ds.child("sender").getValue().equals(myUid)){
                        //ds.getRef().removeValue();
                        //set value to "deleted"
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("message", "This message was wiped");
                        ds.getRef().updateChildren(hashMap);
                        Toast.makeText(context, "message deleted...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "You can delete you own messages only",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get currently signed in user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{
        //view
        private ImageView mIvProfile, ivMessage;
        private TextView mTvMessage, mTvTime, mTvIsSeen;
        private LinearLayout messageLayout; //click listener for deleting

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mIvProfile = itemView.findViewById(R.id.iv_profile);
            ivMessage = itemView.findViewById(R.id.iv_message);
            mTvMessage = itemView.findViewById(R.id.tv_message);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvIsSeen = itemView.findViewById(R.id.tv_is_seen);
            messageLayout = itemView.findViewById(R.id.layout_message);
        }
    }
}
