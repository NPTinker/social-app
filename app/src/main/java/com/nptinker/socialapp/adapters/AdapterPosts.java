package com.nptinker.socialapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nptinker.socialapp.R;
import com.nptinker.socialapp.TheirProfileActivity;
import com.nptinker.socialapp.models.ModelPost;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {

    Context context;
    List<ModelPost> postList;
    private String uid;

    public AdapterPosts(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate row_posts.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        uid = postList.get(position).getUid();
        String uEmail = postList.get(position).getuEmail();
        String uName = postList.get(position).getuName();
        String uDp = postList.get(position).getuDp();
        String pId = postList.get(position).getpId();
        String pTitle = postList.get(position).getpTitle();
        String pDescription = postList.get(position).getpDescription();
        String pImage = postList.get(position).getpImage();
        String pTimeStamp = postList.get(position).getpTime();

        //convert time
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        //set data
        holder.tvUName.setText(uName);
        holder.tvPTime.setText(pTime);
        holder.tvPTitle.setText(pTitle);
        holder.tvPDescription.setText(pDescription);
        //set user dp
        try {
            Picasso.get().load(uDp).placeholder(R.drawable.ic_default_img).into(holder.ivUPicture);
        } catch (Exception e) {

        }

        //set post image
        if (pImage.equals("noImage")){
            holder.ivPImage.setVisibility(View.GONE);
        } else {
            try {
                Picasso.get().load(pImage).into(holder.ivPImage);
            } catch (Exception e) {

            }
        }


        //handle btn click
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        holder.layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TheirProfileActivity.class);
                intent.putExtra("uid",uid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView ivUPicture, ivPImage;
        TextView tvUName, tvPTime, tvPTitle, tvPDescription, tvPLikes;
        ImageButton btnMore;
        Button btnLike, btnComment, btnShare;
        LinearLayout layoutProfile;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            ivUPicture = itemView.findViewById(R.id.iv_u_picture);
            ivPImage = itemView.findViewById(R.id.iv_p_image);
            tvUName = itemView.findViewById(R.id.tv_u_name);
            tvPTime = itemView.findViewById(R.id.tv_p_time);
            tvPTitle = itemView.findViewById(R.id.tv_p_title);
            tvPDescription = itemView.findViewById(R.id.tv_p_description);
            tvPLikes = itemView.findViewById(R.id.tv_p_likes);
            btnMore = itemView.findViewById(R.id.btn_more);
            btnLike = itemView.findViewById(R.id.btn_like);
            btnComment = itemView.findViewById(R.id.btn_comment);
            btnShare = itemView.findViewById(R.id.btn_share);
            layoutProfile = itemView.findViewById(R.id.layout_profile);
        }
    }
}
