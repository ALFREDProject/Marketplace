package com.tempos21.mymarket.data.repository.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.GetAppListClient;
import com.tempos21.mymarket.data.client.mapper.AppVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.apps.AppListVO;
import com.tempos21.mymarket.domain.dto.request.AppListRequest;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.app.App;
import com.tempos21.mymarket.sdk.model.AppListType;

import java.util.List;

public class AppListServiceRepository extends AbstractRepository<AppListRequest, List<App>> {

  private final AppListType appListType;

  public AppListServiceRepository(Context context, AppListType appListType) {
    super(context);
    this.appListType = appListType;
  }

  @Override
  public List<App> get(AppListRequest input) throws Exception {
    try {
      GetAppListClient getAppListClient = new GetAppListClient(getContext(), appListType);
      ClientResponse<AppListVO> response = getAppListClient.execute(input);
      AppVOMapper mapper = new AppVOMapper();
      return mapper.toModel(response.data.appVO);
    } catch (Exception e) {
      if (MyMarket.getInstance().isDebug()) {
        e.printStackTrace();
      }
      throw new Exception(e.getMessage());
    }
  }

  @Override
  public void store(List<App> output) throws Exception {
    throw new UnsupportedOperationException();
  }
}
