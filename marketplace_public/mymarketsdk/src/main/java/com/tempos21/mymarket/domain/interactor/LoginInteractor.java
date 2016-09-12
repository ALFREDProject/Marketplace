package com.tempos21.mymarket.domain.interactor;

import android.content.Context;
import android.text.TextUtils;

import com.tempos21.mymarket.data.repository.client.LoginServiceRepository;
import com.tempos21.mymarket.domain.dto.request.LoginRequest;
import com.tempos21.mymarket.domain.dto.response.LoginResponse;

public class LoginInteractor extends Interactor<LoginRequest, LoginResponse> {

  public LoginInteractor(Context context) {
    super(context);
  }

  @Override
  protected LoginResponse perform(LoginRequest input) throws Exception {
    if (TextUtils.isEmpty(input.userName.trim())) {
      throw new Exception("Credentials fail");
    } else if (TextUtils.isEmpty(input.userPassword.trim())) {
      throw new Exception("Credentials fail");
    }
    return login(input);
  }

  private LoginResponse login(LoginRequest input) throws Exception {
    LoginResponse mainResponse = new LoginResponse();
    LoginServiceRepository repository = new LoginServiceRepository(getContext());
    mainResponse.user = repository.get(input);
    return mainResponse;
  }
}