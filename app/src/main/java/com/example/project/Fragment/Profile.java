package com.example.project.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.project.Adapter.MyImageRcyclerAdapter;
import com.example.project.Login;
import com.example.project.R;
import com.example.project.Splash;
import com.example.project.ViewFollow;
import com.example.project.databinding.FragmentProfileBinding;
import com.example.project.model.DetailViewData;
import com.example.project.model.ProfileData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Profile extends Fragment {

    FragmentProfileBinding binding;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    ProfileData user = new ProfileData();
    Uri uri;
    int mode = 0;
    String contentid = "";
    FirebaseAuth auth;
    MyImageRcyclerAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        get_profile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        adapter = new MyImageRcyclerAdapter(getContext(), auth.getCurrentUser().getUid());
        binding.myImgRecyclerview.setAdapter(adapter);
        binding.myImgRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 3));

        get_profile();

        binding.editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 1;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
                binding.editProfileBtn.setVisibility(View.GONE);
                binding.profileSaveBtn.setVisibility(View.VISIBLE);
                binding.showContent.setVisibility(View.GONE);
                binding.editContent.setVisibility(View.VISIBLE);
                binding.editContent.setText(binding.showContent.getText());
            }
        });

        binding.profileSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 0;
                binding.profileSaveBtn.setVisibility(View.GONE);
                binding.editProfileBtn.setVisibility(View.VISIBLE);
                binding.showContent.setVisibility(View.VISIBLE);
                binding.editContent.setVisibility(View.GONE);
                String content = binding.editContent.getText().toString();
                binding.showContent.setText(content);
                binding.progressbar.setVisibility(View.VISIBLE);
                String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                if(uri != null){
                    storage.getReference().child("profile").child(filename).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgurl = uri.toString();
                                    ProfileData data = new ProfileData(user.userId, imgurl, user.favorite, user.follow, user.follower, content, user.uid, user.follows, user.follwers);
                                    firestore.collection("profile")
                                            .document(contentid).set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    get_profile();
                                                }
                                            });
                                }
                            });
                        }
                    });
                }
                else{
                    ProfileData data = new ProfileData(user.userId, user.userProfile, user.favorite, user.follow, user.follower, content, user.uid, user.follows, user.follows);
                    firestore.collection("profile")
                            .document(contentid).set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    get_profile();
                                }
                            });
                }
            }
        });


        binding.userProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == 1){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0);
                }
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
            }
        });

        binding.followerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewFollow.class);
                intent.putExtra("users", user.follwers);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        binding.followingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewFollow.class);
                intent.putExtra("users", user.follows);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
            uri = data.getData();
            binding.userProfileImg.setImageURI(uri);
        }
    }

    private void reProfile(){
        binding.progressbar.setVisibility(View.GONE);
        Glide.with(getContext())
                .load(user.userProfile)
                .into(binding.userProfileImg);
        if(user.content.equals("")){
            binding.showContent.setText("#문구를 입력해주세요");
        }
        else{
            binding.showContent.setText(user.content);
        }
        binding.userNameTxt.setText(user.userId);
        binding.followCount.setText(String.valueOf(user.follow));
        binding.followerCount.setText(String.valueOf(user.follower));
        binding.favoriteCount.setText(String.valueOf(user.favorite));
    }

    private void get_profile(){
        firestore.collection("profile").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot data : queryDocumentSnapshots.getDocuments()){
                    ProfileData item = data.toObject(ProfileData.class);
                    if(item.uid.equals(auth.getCurrentUser().getUid())){
                        user = item;
                        contentid = data.getId();
                    }
                }
                reProfile();
            }
        });
    }
}