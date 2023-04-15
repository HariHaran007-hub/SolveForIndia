package com.example.solveforindia.controller;

import android.app.Application;

import com.zero.golgol.model.ActiveChallenges;

import java.util.HashMap;

public class User extends Application {

    private static User instance;

    private String userId;
    private String username;
    private String age;
    private String gender;
    private String idNumber;
    private String phoneNumber;
    private String avatar;
    private HashMap<String, ActiveChallenges> challenges;
    private int gols;
    private int badges;
    private HashMap<String , String> golPals;


    public static User getInstance() {
        if (instance == null) instance = new User();
        return instance;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public HashMap<String, ActiveChallenges> getChallenges() {
        return challenges;
    }

    public void setChallenges(HashMap<String, ActiveChallenges> challenges) {
        this.challenges = challenges;
    }

    public int getGols() {
        return gols;
    }

    public void setGols(int gols) {
        this.gols = gols;
    }

    public int getBadges() {
        return badges;
    }

    public void setBadges(int badges) {
        this.badges = badges;
    }

    public HashMap<String, String> getGolPals() {
        return golPals;
    }

    public void setGolPals(HashMap<String, String> golPals) {
        this.golPals = golPals;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", avatar='" + avatar + '\'' +
                ", challenges=" + challenges +
                ", gols=" + gols +
                ", badges=" + badges +
                '}';
    }
}
