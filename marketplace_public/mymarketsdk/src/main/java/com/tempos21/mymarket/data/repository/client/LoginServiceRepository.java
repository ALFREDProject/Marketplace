package com.tempos21.mymarket.data.repository.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.LoginClient;
import com.tempos21.mymarket.data.client.mapper.UserVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.login.UserVO;
import com.tempos21.mymarket.domain.dto.request.LoginRequest;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.User;

public class LoginServiceRepository extends AbstractRepository<LoginRequest, User> {

  public LoginServiceRepository(Context context) {
    super(context);
  }

  @Override
  public User get(LoginRequest input) throws Exception {
    try {
      LoginClient client = new LoginClient(getContext());
      ClientResponse<UserVO> response = client.execute(input);
      UserVOMapper mapper = new UserVOMapper();
      return mapper.toModel(response.data);
    } catch (Exception e) {
      if (MyMarket.getInstance().isDebug()) {
        e.printStackTrace();
      }
      throw new Exception(e.getMessage());

    }
  }

  @Override
  public void store(User output) throws Exception {
    throw new UnsupportedOperationException();
  }
}
