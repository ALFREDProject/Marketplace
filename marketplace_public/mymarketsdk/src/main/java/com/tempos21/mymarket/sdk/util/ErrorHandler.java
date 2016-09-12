package com.tempos21.mymarket.sdk.util;

import android.content.Context;

import com.tempos21.mymarket.data.client.response.ClientResponse;

public class ErrorHandler {

  public static final int ERROR_CODE_WRONG_CREDENTIALS = 111;

  private static ErrorHandler instance;
  private Context context;

  public static ErrorHandler getInstance(Context context) {
    if (instance == null) {
      instance = new ErrorHandler(context);
    }
    return instance;
  }

  private ErrorHandler(Context context) {
    this.context = context;
  }

  public void checkClientResponseError(ClientResponse response) throws Exception {
    switch (response.errorCode) {
      case ERROR_CODE_WRONG_CREDENTIALS:
        throw new Exception("Wrong credentials");
    }
  }
}
