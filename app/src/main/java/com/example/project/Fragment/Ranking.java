package com.example.project.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.Adapter.RangkingRecyclerAdapter;
import com.example.project.R;
import com.example.project.databinding.FragmentRankingBinding;
import com.example.project.model.ProfileData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Ranking extends Fragment {
    FragmentRankingBinding binding;
    FirebaseFirestore firestore;
    ArrayList<ProfileData> rangkingData = new ArrayList<>();
    RangkingRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRankingBinding.inflate(inflater, container, false);
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("profile").orderBy("favorite", Query.Direction.DESCENDING).limit(20).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                rangkingData.clear();
                for(DocumentSnapshot data : queryDocumentSnapshots.getDocuments()){
                    rangkingData.add(data.toObject(ProfileData.class));
                }
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new RangkingRecyclerAdapter(rangkingData, getContext());
        binding.rangkingRecyclerview.setAdapter(adapter);
        binding.rangkingRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }
}