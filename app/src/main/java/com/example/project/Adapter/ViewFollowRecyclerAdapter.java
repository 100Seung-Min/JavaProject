package com.example.project.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.OtherUser;
import com.example.project.R;
import com.example.project.model.ProfileData;

import java.util.ArrayList;

public class ViewFollowRecyclerAdapter extends RecyclerView.Adapter<ViewFollowHolder> {
    ArrayList<ProfileData> itemlist;
    Context context;
    public ViewFollowRecyclerAdapter(ArrayList<ProfileData> itemlist, Context context){
        this.itemlist = itemlist;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewFollowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.user_view_item, parent, false);
        return new ViewFollowHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewFollowHolder holder, int position) {
        holder.bind(itemlist.get(position));
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }
}

class ViewFollowHolder extends RecyclerView.ViewHolder{
    ImageView userProfile;
    TextView userId;
    Context context;
    ConstraintLayout userlayout;
   public ViewFollowHolder(@NonNull View itemView, Context context){
       super(itemView);
       this.context = context;
       userId = itemView.findViewById(R.id.user_follow_name_txt);
       userProfile = itemView.findViewById(R.id.user_follow_profile_img);
       userlayout = itemView.findViewById(R.id.user_layout);
   }

   public void bind(ProfileData item){
       Glide.with(context).load(item.userProfile).into(userProfile);
       userId.setText(item.userId);
       userlayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, OtherUser.class);
               intent.putExtra("uid", item.uid);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);
           }
       });
   }
}
