package com.tempos21.mymarket.data.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.mapper.AppVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.apps.AppListVO;
import com.tempos21.mymarket.domain.dto.request.AppListRequest;
import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.util.ErrorHandler;
import com.tempos21.mymarket.sdk.util.Strings;

import retrofit.client.Response;

public class GetAppListClient extends Client<AppListRequest, ClientResponse<AppListVO>> {

    private final AppListType appListType;

    public GetAppListClient(Context context, AppListType appListType) {
        super(context);
        this.appListType = appListType;
    }

    @Override
    protected ClientResponse<AppListVO> request(AppListRequest request) throws Exception {
        MarketClientService service = MarketClientAdapter.getInstance(getContext()).getService();
        Response serviceResponse = null;
        switch (appListType) {
            case DEFAULT:
                serviceResponse = service.getAppListMarket( // getAppListMarket
                        (request.elements == 0 ? null : request.start), // Start element
                        (request.elements == 0 ? null : request.elements), // Number of elements per page
                        (request.elements == 0 ? null : request.sorting), // Sorting
                        (request.countryId == 0 ? null : request.countryId), // Country id
                        (request.categoryId == 0 ? null : request.categoryId), // Category id
                        (request.languageId == 0 ? null : request.languageId), // Language id
                        (request.elements == 0 ? null : request.hasPromoImage) // If it must have a promo image
                );
                break;
            case SEARCH:
                serviceResponse = service.getSearchAppListMarket( // getSearchAppListMarket
                        (request.words), // Search query
                        (request.elements == 0 ? null : request.start), // Start element
                        (request.elements == 0 ? null : request.elements), // Number of elements per page
                        (request.elements == 0 ? null : request.sorting) // Sorting
                );
                break;
            case INSTALLED:
                serviceResponse = service.getInstalledAppList( // getInstalledAppListMarket
                        (request.elements == 0 ? null : request.start), // Start element
                        (request.elements == 0 ? null : request.elements), // Number of elements per page
                        (request.elements == 0 ? null : request.sorting) // Sorting
                );
                break;
            case UPDATABLE:
                serviceResponse = service.getUpdatableAppList( // getUpdatableAppListMarket
                        (request.elements == 0 ? null : request.start), // Start element
                        (request.elements == 0 ? null : request.elements), // Number of elements per page
                        (request.elements == 0 ? null : request.sorting) // Sorting
                );
                break;
            case MOST_POPULAR:
                serviceResponse = service.getAppListMarket( // getMostPopularAppListMarket
                        (request.elements == 0 ? null : request.start), // Start element
                        (request.elements == 0 ? null : request.elements), // Number of elements per page
                        (request.elements == 0 ? null : request.sorting), // Sorting
                        (request.countryId == 0 ? null : request.countryId), // Country id
                        (request.categoryId == 0 ? null : request.categoryId), // Category id
                        (request.languageId == 0 ? null : request.languageId), // Language id
                        (request.elements == 0 ? null : request.hasPromoImage) // If it must have a promo image
                );
                break;
            case LATEST:
                serviceResponse = service.getAppListMarket( // getLatestAppListMarket
                        (request.elements == 0 ? null : request.start), // Start element
                        (request.elements == 0 ? null : request.elements), // Number of elements per page
                        (request.elements == 0 ? null : request.sorting), // Sorting
                        (request.countryId == 0 ? null : request.countryId), // Country id
                        (request.categoryId == 0 ? null : request.categoryId), // Category id
                        (request.languageId == 0 ? null : request.languageId), // Language id
                        (request.elements == 0 ? null : request.hasPromoImage) // If it must have a promo image
                );
                break;
        }
        ClientResponse<AppListVO> response = AppVOMapper.mapResponse(Strings.convertStreamToString(serviceResponse.getBody().in(), "UTF-8"));
        if ("OK".equals(response.status)) {
            return response;
        } else {
            ErrorHandler.getInstance(getContext()).checkClientResponseError(response);
            throw new Exception("Apps list loading fail");
        }
    }
}
