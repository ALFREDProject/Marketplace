package com.tempos21.market.client.service.impl;

import android.content.Context;

import com.tempos21.market.client.bean.Categories;
import com.tempos21.market.client.bean.ServiceResponse;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class GetCategoriesService extends DefaultSecureService {

    DefaultServiceHandler serviceHandler;
    private Categories categories = new Categories();
    private OnGetCategoriesServiceResponseListener onGetCategoriesServiceResponseListener;
    private ObjectMapper mapper;


    public GetCategoriesService(String url, Context context) {
        super(url, ServiceType.GET, context);
    }


    @Override
    public void runService() {
        super.runService();
    }


    @Override
    public void onSecureServiceResponse(int responseCode, ServiceResponse response) {
        int ErrorCode = responseCode;

        if (responseCode == ServiceErrorCodes.HTTP_OK) {
            mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            if (response.getData() != null) {
                JsonNode elements = response.getData();
                ErrorCode = saveCategories(elements, responseCode);
            } else {
                categories = null;
                ErrorCode = Integer.parseInt(response.getError_code());
            }
        }
        if (onGetCategoriesServiceResponseListener != null) {
            onGetCategoriesServiceResponseListener.onGetCategoriesResponse(ErrorCode, categories);
        }
    }


    private int saveCategories(JsonNode node, int errorCode) {
        try {
            if (node.has("categoryResponse")) {
                JsonNode elements = node.get("categoryResponse");

                categories = mapper.readValue(elements, Categories.class);
            }
        } catch (JsonParseException e) {
            errorCode = ServiceErrorCodes.PARSER_ERROR;
        } catch (JsonMappingException e) {
            errorCode = ServiceErrorCodes.MAPPER_ERROR;
        } catch (IOException e) {
            errorCode = ServiceErrorCodes.IO_ERROR;
        }

        return errorCode;
    }


    public OnGetCategoriesServiceResponseListener getOnServiceResponseListener() {
        return onGetCategoriesServiceResponseListener;
    }

    public void setOnGetCategoriesServiceResponseListener(OnGetCategoriesServiceResponseListener onServiceResponseListener) {
        this.onGetCategoriesServiceResponseListener = onServiceResponseListener;
    }

    public DefaultServiceHandler getServiceHandler() {
        return serviceHandler;
    }

    public void setServiceHandler(DefaultServiceHandler serviceHandler) {
        this.serviceHandler = serviceHandler;
    }


    public interface OnGetCategoriesServiceResponseListener {
        public void onGetCategoriesResponse(int responseCode, Categories categories);
    }
}
