package com.example.project.Adapter;

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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RangkingRecyclerAdapter extends RecyclerView.Adapter<RangkingHolder> {

    ArrayList<ProfileData> itemlist;
    Context context;

    public RangkingRecyclerAdapter(ArrayList<ProfileData> itemlist, Context context){
        this.itemlist = itemlist;
        this.context = context;
    }

    @NonNull
    @Override
    public RangkingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ranking_item,parent,false);
        return new RangkingHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RangkingHolder holder, int position) {
        holder.onbind(itemlist.get(position), position);
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }
}

class RangkingHolder extends RecyclerView.ViewHolder{
    Context context;

    LinearLayout first_layout;
    TextView first_userId;
    TextView first_favorite;
    ImageView first_profile;

    LinearLayout second_layout;
    TextView second_userId;
    TextView second_favorite;
    ImageView second_profile;

    LinearLayout third_layout;
    TextView third_userId;
    TextView third_favorite;
    ImageView third_profile;

    LinearLayout top_layout;
    TextView top_userId;
    TextView top_favorite;
    ImageView top_profile;
    TextView top_rank;

    ConstraintLayout ranking_layout;


    public RangkingHolder(@NonNull View v, Context context){
        super(v);
        this.context = context;
        first_layout = v.findViewById(R.id.rangking_first);
        first_userId = v.findViewById(R.id.userId_first);
        first_favorite = v.findViewById(R.id.favorite_count_first);
        first_profile = v.findViewById(R.id.user_profile_first);

        second_layout = v.findViewById(R.id.rangking_second);
        second_userId = v.findViewById(R.id.userId_second);
        second_favorite = v.findViewById(R.id.favorite_count_second);
        second_profile = v.findViewById(R.id.user_profile_second);

        third_layout = v.findViewById(R.id.rangking_third);
        third_userId = v.findViewById(R.id.userId_third);
        third_favorite = v.findViewById(R.id.favorite_count_third);
        third_profile = v.findViewById(R.id.user_profile_third);

        top_layout = v.findViewById(R.id.rangking);
        top_userId = v.findViewById(R.id.userId);
        top_favorite = v.findViewById(R.id.favorite_count);
        top_profile = v.findViewById(R.id.user_profile);
        top_rank = v.findViewById(R.id.ranking);
        ranking_layout = v.findViewById(R.id.ranking_layout);
    }

    public void onbind(ProfileData item, int ranking){
        if(ranking == 0){
            first_layout.setVisibility(View.VISIBLE);
            first_userId.setText(item.userId);
            first_favorite.setText(String.valueOf(item.favorite));
            Glide.with(context).load(item.userProfile).into(first_profile);
        }
        else if(ranking == 1){
            second_layout.setVisibility(View.VISIBLE);
            second_userId.setText(item.userId);
            second_favorite.setText(String.valueOf(item.favorite));
            Glide.with(context).load(item.userProfile).into(second_profile);
        }
        else if(ranking == 2){
            third_layout.setVisibility(View.VISIBLE);
            third_userId.setText(item.userId);
            third_favorite.setText(String.valueOf(item.favorite));
            Glide.with(context).load(item.userProfile).into(third_profile);
        }
        else{
            top_rank.setText(String.valueOf(ranking + 1));
            top_layout.setVisibility(View.VISIBLE);
            top_userId.setText(item.userId);
            top_favorite.setText(String.valueOf(item.favorite));
            Glide.with(context).load(item.userProfile).into(top_profile);
        }
        ranking_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if(!item.uid.equals(auth.getCurrentUser().getUid())){
                    Intent intent = new Intent(context, OtherUser.class);
                    intent.putExtra("uid", item.uid);
                    context.startActivity(intent);
                }
            }
        });
    }
}
