package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.ArrayList;

public class RangkingRecyclerAdapter extends RecyclerView.Adapter<RangkingHolder> {

    ArrayList<String> itemlist;
    Context context;

    public RangkingRecyclerAdapter(ArrayList<String> itemlist, Context context){
        this.itemlist = itemlist;
        this.context = context;
    }

    @NonNull
    @Override
    public RangkingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_item,parent, false);
        return new RangkingHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RangkingHolder holder, int position) {
        holder.onbind();
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }
}

class RangkingHolder extends RecyclerView.ViewHolder{
    public RangkingHolder(@NonNull View v, Context context){
        super(v);
    }

    public void onbind(){

    }
}
