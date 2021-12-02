package com.example.project.model;

import java.util.ArrayList;

public class ProfileData {
    public String userId;
    public String userProfile;
    public int favorite;
    public int follow;
    public int follower;
    public String content;
    public String uid;
    public ArrayList<String> follows;
    public ArrayList<String> follwers;

    public ProfileData(String userId, String userProfile, int favorite, int follow, int follower, String content, String uid, ArrayList<String> follows, ArrayList<String> follwers){
        this.userId = userId;
        this.userProfile = userProfile;
        this.favorite = favorite;
        this.follow = follow;
        this.follower = follower;
        this.content = content;
        this.uid = uid;
        this.follows = follows;
        this.follwers = follwers;
    }

    public ProfileData(){

    }
}
