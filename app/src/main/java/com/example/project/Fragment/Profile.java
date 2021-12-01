package com.example.project.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.databinding.FragmentProfileBinding;
import com.example.project.model.DetailViewData;
import com.example.project.model.ProfileData;
import com.google.android.gms.tasks.OnSuccessListener;
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
    String name;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    ProfileData user;
    Uri uri;
    int mode = 0;
    String contentid = "";

    public Profile(String name){
        this.name = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.userNameTxt.setText(name);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        user = new ProfileData(name, "", 0,0,0,"");

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
                reProfile();
            }
        });


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
                String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                if(contentid.isEmpty()){
                    storage.getReference().child("profile").child(filename).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgurl = uri.toString();
                                    ProfileData data = new ProfileData(name, imgurl, user.favorite, user.follow, user.follower, content);
                                    firestore.collection("profile")
                                            .document().set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    reProfile();
                                                }
                                            });
                                }
                            });
                        }
                    });
                }
                else {
                    storage.getReference().child("profile").child(filename).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgurl = uri.toString();
                                    ProfileData data = new ProfileData(name, imgurl, user.favorite, user.follow, user.follower, content);
                                    firestore.collection("profile")
                                            .document(contentid).set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    reProfile();
                                                }
                                            });
                                }
                            });
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
        Glide.with(getContext())
                .load(user.userProfile)
                .into(binding.userProfileImg);
        if(user.content != null){
            binding.showContent.setText(user.content);
        }
        binding.followCount.setText(String.valueOf(user.follow));
        binding.followerCount.setText(String.valueOf(user.follower));
        binding.favoriteCount.setText(String.valueOf(user.favorite));
    }
}