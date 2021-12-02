package com.example.project.model;

import java.util.ArrayList;

public class DetailViewData {
    public String userId;
    public String userProfile;
    public String imageUrl;
    public String content;
    public int favorite;
    public ArrayList<String> favorites;
    public String uid;

    public DetailViewData(String userId, String userProfile, String imageUrl, int favorite, String content, ArrayList<String> favorites, String uid){
        this.userId = userId;
        this.userProfile = userProfile;
        this.imageUrl = imageUrl;
        this.favorite = favorite;
        this.content = content;
        this.favorites = favorites;
        this.uid = uid;
    }

    public DetailViewData(){

    }
}
