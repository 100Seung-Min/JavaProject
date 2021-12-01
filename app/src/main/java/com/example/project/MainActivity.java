package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.project.Fragment.Profile;
import com.example.project.Fragment.Ranking;
import com.example.project.Fragment.ShowSns;
import com.example.project.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getSharedPreferences("NAME", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();

        nickname = preferences.getString("username", "");
        Log.d("여기", "onCreate: " + nickname);

        binding.mainBottomNavigation.setItemIconTintList(null);

        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_1, new ShowSns(nickname)).commit();

        binding.mainBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_1, new ShowSns(nickname)).commit();
                        break;
                    case R.id.rangking:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_1, new Ranking()).commit();
                        break;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_1, new Profile(nickname)).commit();
                        break;
                }
                return true;
            }
        });
    }
}