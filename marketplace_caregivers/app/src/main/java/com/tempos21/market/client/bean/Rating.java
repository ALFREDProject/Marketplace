package com.tempos21.market.client.bean;

public class Rating {
    private float rate;
    private int userId;
    private int id;
    private String title;
    private String text;
    private String versionString;
    private String userName;
    private String dateCreation;
    private String userFullName;


    public Rating() {

    }


    public Rating(int rate, int userId, int id, String title, String text) {
        this.rate = rate;
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getVersionString() {
        return versionString;
    }


    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getDateCreation() {
        return dateCreation;
    }


    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }


    public String getUserFullName() {
        return userFullName;
    }


    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }


}
