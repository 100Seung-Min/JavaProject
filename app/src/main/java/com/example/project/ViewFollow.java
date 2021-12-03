package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.project.Adapter.ViewFollowRecyclerAdapter;
import com.example.project.databinding.ActivityViewFollowBinding;
import com.example.project.model.ProfileData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewFollow extends AppCompatActivity {

    ActivityViewFollowBinding binding;
    ArrayList<String> itemlist;
    ArrayList<ProfileData> userlist = new ArrayList<>();
    FirebaseFirestore firestore;
    ViewFollowRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewFollowBinding.inflate(getLayoutInflater());
        firestore = FirebaseFirestore.getInstance();
        setContentView(binding.getRoot());
        itemlist = getIntent().getStringArrayListExtra("users");
        firestore.collection("profile").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userlist.clear();
                for(DocumentSnapshot data : queryDocumentSnapshots){
                    ProfileData item = data.toObject(ProfileData.class);
                    for(String uid : itemlist){
                        if(uid.equals(item.uid)){
                            userlist.add(item);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new ViewFollowRecyclerAdapter(userlist, getApplicationContext());
        binding.viewFollowRecyclerview.setAdapter(adapter);
        binding.viewFollowRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}