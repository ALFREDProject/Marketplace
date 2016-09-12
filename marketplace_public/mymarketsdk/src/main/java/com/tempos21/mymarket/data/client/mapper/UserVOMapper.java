package com.tempos21.mymarket.data.client.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.login.UserVO;
import com.tempos21.mymarket.sdk.model.User;

import java.lang.reflect.Type;

public class UserVOMapper extends BaseVOMapper<UserVO, User> {

  public static ClientResponse<UserVO> mapResponse(String json) {
    Type type = new TypeToken<ClientResponse<UserVO>>() {}.getType();
    return new Gson().fromJson(json, type);
  }

  @Override
  public User toModel(UserVO vo) {
    User user = new User();
    user.id = vo.id;
    user.name = vo.name;
    user.dasUser = vo.dasUser;
    user.authToken = vo.authToken;
    user.isTester = vo.tester;
    user.isApprover = vo.approver;
    return user;
  }

  @Override
  public UserVO toVO(User model) {
    return null;
  }
}
