package com.tempos21.mymarket.data.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.mapper.InstalledVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.set_installed_apps.DatumVO;
import com.tempos21.mymarket.domain.dto.request.InstalledAppRequest;
import com.tempos21.mymarket.sdk.util.Strings;

import java.util.List;

import retrofit.client.Response;

public class SetInstalledAppsClient extends Client<InstalledAppRequest, ClientResponse<List<DatumVO>>> {

    public SetInstalledAppsClient(Context context) {
        super(context);
    }

    @Override
    protected ClientResponse<List<DatumVO>> request(InstalledAppRequest request) throws Exception {
        MarketClientService service = MarketClientAdapter.getInstance(getContext()).getService();
        Response serviceResponse = service.setInstalledApps(request.getJsonArray());
        ClientResponse<List<DatumVO>> response = InstalledVOMapper.mapResponse(Strings.convertStreamToString(serviceResponse.getBody().in(), "UTF-8"));
        if ("OK".equals(response.status)) {
            return response;
        } else {
            throw new Exception("Set Installed Apps fail");
        }
    }
}
