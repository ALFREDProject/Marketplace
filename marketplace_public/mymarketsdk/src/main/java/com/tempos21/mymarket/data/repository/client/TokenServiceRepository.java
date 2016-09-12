package com.tempos21.mymarket.data.repository.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.SetTokenClient;
import com.tempos21.mymarket.data.client.mapper.TokenVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.push.TokenVO;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.domain.dto.request.TokenRequest;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.Token;

public class TokenServiceRepository extends AbstractRepository<TokenRequest, Token> {

  public TokenServiceRepository(Context context) {
    super(context);
  }

  @Override
  public Token get(TokenRequest input) throws Exception {
    try {
      SetTokenClient client = new SetTokenClient(getContext());
      ClientResponse<TokenVO> response = client.execute(input);
      TokenVOMapper mapper = new TokenVOMapper();
      return mapper.toModel(response.data);
    } catch (Exception e) {
      if (MyMarket.getInstance().isDebug()) {
        e.printStackTrace();
      }
      throw new Exception(e.getMessage());
    }
  }

  @Override
  public void store(Token output) throws Exception {
    throw new UnsupportedOperationException();
  }
}
