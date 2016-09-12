package com.tempos21.mymarket.data.repository.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.GetLanguageListClient;
import com.tempos21.mymarket.data.client.mapper.LanguageVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.language.LanguageListVO;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.Language;

import java.util.List;

public class LanguageListServiceRepository extends AbstractRepository<Void, List<Language>> {

  public LanguageListServiceRepository(Context context) {
    super(context);
  }

  @Override
  public List<Language> get(Void input) throws Exception {
    try {
      GetLanguageListClient client = new GetLanguageListClient(getContext());
      ClientResponse<LanguageListVO> response = client.execute(null);
      LanguageVOMapper mapper = new LanguageVOMapper();
      return mapper.toModel(response.data.languageVO);
    } catch (Exception e) {
      if (MyMarket.getInstance().isDebug()) {
        e.printStackTrace();
      }
      throw new Exception(e.getMessage());

    }
  }

  @Override
  public void store(List<Language> output) throws Exception {
    throw new UnsupportedOperationException();
  }
}
