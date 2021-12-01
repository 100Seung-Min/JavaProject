package com.example.project.model;

public class ProfileData {
    public String userId;
    public String userProfile;
    public int favorite;
    public int follow;
    public int follower;
    public String content;

    public ProfileData(String userId, String userProfile, int favorite, int follow, int follower, String content){
        this.userId = userId;
        this.userProfile = userProfile;
        this.favorite = favorite;
        this.follow = follow;
        this.follower = follower;
        this.content = content;
    }

    public ProfileData(){

    }
}
