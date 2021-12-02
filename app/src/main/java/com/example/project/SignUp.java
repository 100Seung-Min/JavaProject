package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.project.databinding.ActivitySignUpBinding;
import com.example.project.model.ProfileData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;

    private FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = binding.inputId.getText().toString();
                String pw = binding.inputPw.getText().toString();
                String checkPw = binding.checkPw.getText().toString();
                String nickName = binding.inputNickname.getText().toString();
                if(id.isEmpty() || pw.isEmpty() || nickName.isEmpty() || checkPw.isEmpty()){
                    Toast.makeText(getApplicationContext(), "모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(!pw.equals(checkPw)){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
                else if(!id.contains("@")){
                    Toast.makeText(getApplicationContext(), "올바른 이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.createUserWithEmailAndPassword(id, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d("여기", "onComplete: " + auth.getCurrentUser().getUid());
                                firestore.collection("profile").document().set(new ProfileData(nickName, "", 0,0,0,"", auth.getCurrentUser().getUid(), new ArrayList<String>(), new ArrayList<String>())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        finish();
                                    }
                                });
                                                            }
                            else {
                                Toast.makeText(getApplicationContext(), "계정생성 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}