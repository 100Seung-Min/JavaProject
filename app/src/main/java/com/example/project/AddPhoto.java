package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.project.databinding.ActivityAddPhotoBinding;
import com.example.project.model.DetailViewData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddPhoto extends AppCompatActivity {

    ActivityAddPhotoBinding binding;
    Uri uri;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Intent intent;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name = getIntent().getStringExtra("username");
        Log.d("여기1", "onCreate: " + name);

        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        binding.addPhotoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            case R.id.upload:
                String content = binding.addPhotoTxt.getText().toString();
                if(uri == null){
                    Toast.makeText(getApplicationContext(), "사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(content.isEmpty()){
                    Toast.makeText(getApplicationContext(), "오늘의 패션을 설명해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    storage.getReference().child("image").child(filename).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgurl = uri.toString();
                                    DetailViewData data = new DetailViewData(name, "", imgurl, 0, content, new ArrayList<String>());
                                    firestore.collection("photo")
                                            .document().set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    finish();
                                                }
                                            });
                                }
                            });
                        }
                    });
                }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
            uri = data.getData();
            binding.photoIcon.setVisibility(View.GONE);
            binding.addPhotoImg.setImageURI(uri);
        }
    }
}