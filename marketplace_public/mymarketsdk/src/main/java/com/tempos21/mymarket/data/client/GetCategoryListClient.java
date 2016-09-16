package com.tempos21.mymarket.data.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.mapper.CategoryVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.category.CategoryListVO;
import com.tempos21.mymarket.sdk.util.ErrorHandler;
import com.tempos21.mymarket.sdk.util.Strings;

import retrofit.client.Response;

public class GetCategoryListClient extends Client<Void, ClientResponse<CategoryListVO>> {

    public GetCategoryListClient(Context context) {
        super(context);
    }

    @Override
    protected ClientResponse<CategoryListVO> request(Void request) throws Exception {
        MarketClientService service = MarketClientAdapter.getInstance(getContext()).getService();
        Response serviceResponse = service.getCategoryList();
        ClientResponse<CategoryListVO> response = CategoryVOMapper.mapResponse(Strings.convertStreamToString(serviceResponse.getBody().in(), "UTF-8"));
        if ("OK".equals(response.status)) {
            return response;
        } else {
            ErrorHandler.getInstance(getContext()).checkClientResponseError(response);
            throw new Exception("Category list loading fail");
        }
    }
}
