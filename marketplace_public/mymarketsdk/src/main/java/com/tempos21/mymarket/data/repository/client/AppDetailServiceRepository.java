package com.tempos21.mymarket.data.repository.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.GetAppDetailClient;
import com.tempos21.mymarket.data.client.mapper.AppDetailVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.apps.AppDetailVO;
import com.tempos21.mymarket.domain.dto.request.AppDetailRequest;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.app.AppDetail;

public class AppDetailServiceRepository extends AbstractRepository<AppDetailRequest, AppDetail> {

  public AppDetailServiceRepository(Context context) {
    super(context);
  }

  @Override
  public AppDetail get(AppDetailRequest input) throws Exception {
    try {
      GetAppDetailClient client = new GetAppDetailClient(getContext());
      ClientResponse<AppDetailVO> response = client.execute(input);
      AppDetailVOMapper mapper = new AppDetailVOMapper();
      return mapper.toModel(response.data);
    } catch (Exception e) {
      if (MyMarket.getInstance().isDebug()) {
        e.printStackTrace();
      }
      throw new Exception(e.getMessage());

    }
  }

  @Override
  public void store(AppDetail output) throws Exception {
    throw new UnsupportedOperationException();
  }
}
