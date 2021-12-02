package com.example.project.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.Adapter.DetailViewRecyclerAdapter;
import com.example.project.AddPhoto;
import com.example.project.R;
import com.example.project.databinding.FragmentShowSnsBinding;
import com.example.project.model.DetailViewData;
import com.example.project.model.ProfileData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class ShowSns extends Fragment {

    FragmentShowSnsBinding binding;
    ArrayList<DetailViewData> data = new ArrayList<>();
    ArrayList<String> contentId = new ArrayList<>();
    SnapHelper snapHelper;
    FirebaseFirestore firestore;
    DetailViewRecyclerAdapter adapter;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowSnsBinding.inflate(inflater, container, false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //리사이클러뷰 리스트 설정
        init_list();

        //리사이클러뷰 기초 설정
        adapter = new DetailViewRecyclerAdapter(getContext(), data, auth.getCurrentUser().getUid(), contentId);
        binding.snsRecyclerview.setAdapter(adapter);
        binding.snsRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.snsRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    binding.addPhoto.setVisibility(View.GONE);
                }
                else{
                    binding.addPhoto.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //스냅헬퍼 기초 설정
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.snsRecyclerview);

        //업로드 버튼 클릭시
        binding.addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddPhoto.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    private void init_list(){
        firestore.collection("photo").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                data.clear();
                contentId.clear();
                for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                    DetailViewData item = document.toObject(DetailViewData.class);
                    data.add(item);
                    contentId.add(document.getId());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}