package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.project.Adapter.MyImageRcyclerAdapter;
import com.example.project.databinding.ActivityOtherUserBinding;
import com.example.project.model.DetailViewData;
import com.example.project.model.ProfileData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OtherUser extends AppCompatActivity {

    ActivityOtherUserBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String user;
    ProfileData userData;
    String userDataId;
    ProfileData userMe;
    String userMeId;
    ArrayList<DetailViewData> itemlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtherUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getIntent().getStringExtra("uid");
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        firestore.collection("profile").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot data : queryDocumentSnapshots){
                    ProfileData item = data.toObject(ProfileData.class);
                    if(item.uid.equals(user)){
                        userData = item;
                        userDataId = data.getId();
                    }
                    if(item.uid.equals(auth.getCurrentUser().getUid())){
                        userMe = item;
                        userMeId = data.getId();
                    }
                }
                for(String item : userData.follwers) {
                    if (item.equals(userMe.uid)) {
                        binding.followBtn.setVisibility(View.GONE);
                        binding.unfollowBtn.setVisibility(View.VISIBLE);
                        break;
                    }
                }
                firestore.collection("photo").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot data : queryDocumentSnapshots){
                            DetailViewData item = data.toObject(DetailViewData.class);
                            if(item.uid.equals(user)){
                                itemlist.add(item);
                            }
                        }
                        load_profile();
                    }
                });
            }
        });
        binding.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.unfollowBtn.setVisibility(View.VISIBLE);
                binding.followBtn.setVisibility(View.GONE);
                userData.follower += 1;
                userData.follwers.add(userMe.uid);
                userMe.follow += 1;
                userMe.follows.add(userData.uid);
                firestore.collection("profile").document(userDataId).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        change_follow();
                    }
                });
                firestore.collection("profile").document(userMeId).set(userMe);
            }
        });
        binding.unfollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.followBtn.setVisibility(View.VISIBLE);
                binding.unfollowBtn.setVisibility(View.GONE);
                userData.follower -= 1;
                userData.follwers.remove(userMe.uid);
                userMe.follow -= 1;
                userMe.follows.remove(userData.uid);
                firestore.collection("profile").document(userDataId).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        change_follow();
                    }
                });
                firestore.collection("profile").document(userMeId).set(userMe);
            }
        });

        binding.followerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewFollow.class);
                intent.putExtra("users", userData.follwers);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });

        binding.followingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewFollow.class);
                intent.putExtra("users", userData.follows);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });
    }
    private void load_profile(){
        binding.userNameTxt.setText(userData.userId);
        binding.followCount.setText(String.valueOf(userData.follow));
        binding.followerCount.setText(String.valueOf(userData.follower));
        binding.favoriteCount.setText(String.valueOf(userData.favorite));
        binding.showContent.setText(userData.content);
        Glide.with(getApplicationContext()).load(userData.userProfile).into(binding.userProfileImg);
        MyImageRcyclerAdapter adapter = new MyImageRcyclerAdapter(getApplicationContext(), userData.uid);
        binding.myImgRecyclerview.setAdapter(adapter);
        binding.myImgRecyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
    }

    private void change_follow(){
        binding.followerCount.setText(String.valueOf(userData.follower));
    }
}