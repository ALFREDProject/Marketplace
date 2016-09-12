package com.tempos21.market.client.bean;

public class User {

    private String name;
    private String j_username;
    private String j_password;
    private String device_id;
    private String platform;
    private String version;
    private boolean isApprover;
    private boolean isTester;
    private int id;
    private String authToken;
    private String dasUser;

    public String toString() {
        String result = "name: " + name;
        result += "\ndasUser:" + dasUser;
        result += "\nj_username: " + j_username;
        result += "\ndevice_id: " + device_id;
        result += "\nplatform: " + platform;
        result += "\nversion: " + version;
        result += "\nisApprover: " + isApprover();
        result += "\nisTester: " + isIsTester();
        result += "\nid: " + id;
        result += "\nauthToken: " + authToken;
        return result;

    }

    public String getDasUser() {
        return dasUser;
    }

    public void setDasUser(String dasUser) {
        this.j_username = dasUser;
        this.dasUser = dasUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getJ_username() {
        return j_username;
    }

    public void setJ_username(String j_username) {
        this.dasUser = j_username;
        this.j_username = j_username;
    }

    public String getJ_password() {
        return j_password;
    }

    public void setJ_password(String j_password) {
        this.j_password = j_password;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isApprover() {
        return isApprover;
    }

    public void setApprover(boolean isApprover) {
        this.isApprover = isApprover;
    }

    public boolean isIsTester() {
        return isTester;
    }

    public void setIsTester(boolean isTester) {
        this.isTester = isTester;
    }

}
