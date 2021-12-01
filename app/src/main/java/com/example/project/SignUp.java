package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.project.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("NAME", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();

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
                                edit.putString("username", nickName);
                                edit.commit();
                                finish();
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