package com.example.project.model;

public class RankingData {
    public String userId;
    public int favorite;
    public String userProfile;

    public RankingData(String userId, String userProfile, int favorite){
        this.favorite = favorite;
        this.userId = userId;
        this.userProfile = userProfile;
    }
}
