package com.tempos21.mymarket.data.client.response.app_rate;

import com.google.gson.annotations.Expose;
import com.tempos21.mymarket.data.client.response.login.UserVO;

public class RateVO {

    @Expose
    public String versionString;
    @Expose
    public Integer score;
    @Expose
    public String subject;
    @Expose
    public Long date;
    @Expose
    public Integer id;
    @Expose
    public String comment;
    @Expose
    public UserVO users;
}

