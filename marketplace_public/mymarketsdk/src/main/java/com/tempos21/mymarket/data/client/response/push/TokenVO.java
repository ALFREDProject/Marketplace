package com.tempos21.mymarket.data.client.response.push;

import com.google.gson.annotations.Expose;
import com.tempos21.mymarket.data.client.response.apps.PlatformVO;
import com.tempos21.mymarket.data.client.response.login.UserVO;

public class TokenVO {

  @Expose
  public String uuid;
  @Expose
  public PlatformVO platforms;
  @Expose
  public UserVO users;
  @Expose
  public Integer version;
  @Expose
  public Integer id;
}

