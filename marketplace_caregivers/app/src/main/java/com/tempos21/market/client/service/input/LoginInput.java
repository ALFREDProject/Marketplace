package com.tempos21.market.client.service.input;

import com.tempos21.market.client.bean.User;

/***
 * This object stores the fields needed to run the login service
 *
 * @author Sergi Martinez
 */
public class LoginInput implements Input {
    private String j_username;
    private String j_password;
    private String device_id;
    private String platform;
    private String version;

    public LoginInput() {
        return;
    }

    public LoginInput(User user) {
        this.j_username = user.getJ_username();
        this.j_password = user.getJ_password();
        this.device_id = user.getDevice_id();
        this.platform = user.getPlatform();
        this.version = user.getVersion();
    }


    public String getJ_username() {
        return j_username;
    }

    public void setJ_username(String j_username) {
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


}
