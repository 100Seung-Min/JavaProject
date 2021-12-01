package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.OtherUser;
import com.example.project.R;
import com.example.project.model.DetailViewData;
import com.example.project.model.ProfileData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DetailViewRecyclerAdapter extends RecyclerView.Adapter<DetailHolder> {

    ArrayList<DetailViewData> data;
    Context context;
    String name;
    ArrayList<String> contentId;

    @NonNull
    @Override
    public DetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_item, parent, false);
        return new DetailHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHolder holder, int position) {
        holder.onBind(data.get(position), name,  contentId.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public DetailViewRecyclerAdapter(Context context,ArrayList<DetailViewData> data, String name,  ArrayList<String> contentId){
        this.data = data;
        this.name = name;
        this.context = context;
        this.contentId = contentId;
    }
}

class DetailHolder extends RecyclerView.ViewHolder{

    FirebaseFirestore firestore;

    TextView userName;
    TextView content;
    ImageView userProfile;
    ImageView detailImg;
    ImageView setting;
    ImageView favorite;
    Context context;
    String contentid;
    ProfileData user = new ProfileData();

    public DetailHolder(@NonNull View itemView, Context context) {
        super(itemView);
        userName = itemView.findViewById(R.id.user_name_txt);
        userProfile = itemView.findViewById(R.id.user_profile_img);
        detailImg = itemView.findViewById(R.id.detail_img);
        setting = itemView.findViewById(R.id.detail_setting_img);
        favorite = itemView.findViewById(R.id.favorite_img);
        content = itemView.findViewById(R.id.content);
        this.context = context;
    }

    public void onBind(DetailViewData item, String name,  String contentId){
        firestore = FirebaseFirestore.getInstance();
        load_profile(name);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherUser.class);
                context.startActivity(intent);
            }
        });
        userName.setText(item.userId);
        Glide.with(context).load(item.imageUrl).into(detailImg);
        content.setText(item.content);
        boolean mode = check_name(item, name);
        if(mode == true){
            favorite.setImageResource(R.drawable.ic_baseline_star_rate_24);
        }
        else{
            favorite.setImageResource(R.drawable.ic_baseline_star_outline_24);
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == true){
                    favorite.setImageResource(R.drawable.ic_baseline_star_outline_24);
                    item.favorites.remove(name);
                }
                else{
                    favorite.setImageResource(R.drawable.ic_baseline_star_rate_24);
                    item.favorites.add(name);
                }
                firestore.collection("photo").document(contentId).set(item);
            }
        });
    }

    private boolean check_name(DetailViewData item, String name){
        for(String id : item.favorites){
            if(id.equals(name)){
                return true;
            }
        }
        return false;
    }

    private void load_profile(String name){
        firestore.collection("profile").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot data : value.getDocuments()){
                    ProfileData item = data.toObject(ProfileData.class);
                    if(item.userId.equals(name)){
                        user = item;
                        contentid = data.getId();
                    }
                }
                Glide.with(context).load(user.userProfile).into(userProfile);
            }
        });
    }
}
