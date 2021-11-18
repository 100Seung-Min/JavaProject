package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.DetailViewData;

import java.util.ArrayList;

public class DetailViewRecyclerAdapter extends RecyclerView.Adapter<Holder> {

    ArrayList<DetailViewData> data;
    Context context;

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.userName.setText("안녕");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public DetailViewRecyclerAdapter(Context context,ArrayList<DetailViewData> data){
        this.data = data;
        this.context = context;
    }
}

class Holder extends RecyclerView.ViewHolder{

    TextView userName;
    ImageView userProfile;
    ImageView detailImg;
    ImageView setting;
    ImageView favorite;
    public Holder(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.user_name_txt);
        userProfile = itemView.findViewById(R.id.user_profile_img);
        detailImg = itemView.findViewById(R.id.detail_img);
        setting = itemView.findViewById(R.id.detail_setting_img);
        favorite = itemView.findViewById(R.id.favorite_img);
    }
}
