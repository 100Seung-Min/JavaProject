package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.model.DetailViewData;
import com.example.project.model.ProfileData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyImageRcyclerAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    FirebaseFirestore firestore;
    Context context;
    ArrayList<DetailViewData> itemlist = new ArrayList<>();

    public MyImageRcyclerAdapter(Context context, String uid){
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("photo").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                itemlist.clear();
                for(DocumentSnapshot data : queryDocumentSnapshots){
                    DetailViewData item = data.toObject(DetailViewData.class);
                    if(item.uid.equals(uid)){
                        itemlist.add(item);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.my_image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(itemlist.get(position), context);
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }
}

class ImageViewHolder extends RecyclerView.ViewHolder {
    ImageView myImage;
    public ImageViewHolder(@NonNull View itemview){
        super(itemview);
        myImage = itemview.findViewById(R.id.my_img);
    }

    public void bind(DetailViewData item, Context context){
        Glide.with(context).load(item.imageUrl).into(myImage);
    }
}
