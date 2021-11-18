package com.example.project.model;

public class DetailViewData {
    public String userId;
    public String userProfile;
    public String imageUrl;
    public int follow;
    public int follower;
    public int favorite;

    public DetailViewData(String userId, String userProfile, String imageUrl, int follow, int follower, int favorite){
        this.userId = userId;
        this.userProfile = userProfile;
        this.imageUrl = imageUrl;
        this.follow = follow;
        this.follower = follower;
        this.favorite = favorite;
    }
}
