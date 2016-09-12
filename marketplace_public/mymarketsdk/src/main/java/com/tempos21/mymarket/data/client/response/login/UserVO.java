package com.tempos21.mymarket.data.client.response.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserVO {

  @Expose
  public String dasUser;
  @Expose
  @SerializedName("isTester")
  public Boolean tester;
  @Expose
  @SerializedName("isApprover")
  public Boolean approver;
  @Expose
  public String authToken;
  @Expose
  public String name;
  @Expose
  public Integer id;
}
