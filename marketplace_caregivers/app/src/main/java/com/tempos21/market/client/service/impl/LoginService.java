package com.tempos21.market.client.service.impl;

import android.content.Context;

import com.tempos21.market.Constants;
import com.tempos21.market.client.bean.ServiceResponse;
import com.tempos21.market.client.bean.User;
import com.tempos21.market.client.service.input.LoginInput;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class LoginService extends DefaultService {

    OnLoginResponseListener onLoginResponseListener;
    DefaultServiceHandler serviceHandler;
    private Context context;

    public LoginService(Context context) {
        super(Constants.LOGIN_SERVICE);
        this.context = context;
    }

    public OnLoginResponseListener getOnServiceResponseListener() {
        return onLoginResponseListener;
    }

    public void setOnServiceResponseListener(
            OnLoginResponseListener onServiceResponseListener) {
        this.onLoginResponseListener = onServiceResponseListener;
    }

    public DefaultServiceHandler getServiceHandler() {
        return serviceHandler;
    }

    public void setServiceHandler(DefaultServiceHandler serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    public void runService(LoginInput input) {
        setupParameters(input);
        serviceHandler = new DefaultServiceHandler(context, this);
        serviceHandler.setOnServiceResponseListener(this);
        serviceHandler.runPostServiceAsync();

    }

    private void setupParameters(LoginInput input) {
        clearEntitiyParameters();

        addEntityParameter(fields.J_USERNAME, input.getJ_username());
        addEntityParameter(fields.J_PASSWORD, input.getJ_password());
        addEntityParameter(fields.DEVICE_ID, input.getDevice_id());
        addEntityParameter(fields.PLATFORM, input.getPlatform());
        addEntityParameter(fields.VERSION, input.getVersion());
    }

    @Override
    public void onServiceResponse(int responseCode, ServiceResponse response) {

        int loginErrorCode = responseCode;
        User user = null;
        if (responseCode == ServiceErrorCodes.HTTP_OK) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            if (response.getData() != null) {
                try {
                    user = mapper.readValue(response.getData(), User.class);
                } catch (JsonParseException e) {
                    loginErrorCode = ServiceErrorCodes.WRONG_RESPONSE;
                } catch (JsonMappingException e) {
                    loginErrorCode = ServiceErrorCodes.WRONG_RESPONSE;
                } catch (IOException e) {
                    loginErrorCode = ServiceErrorCodes.IO_ERROR;
                }
            } else {
                user = null;
                loginErrorCode = Integer.parseInt(response.getError_code());
            }
        }
        if (onLoginResponseListener != null) {
            onLoginResponseListener.onLoginResponse(loginErrorCode, user);
        }
    }

    public interface OnLoginResponseListener {
        public void onLoginResponse(int responseCode, User user);
    }

    private class fields {
        public static final String J_USERNAME = "j_username";
        public static final String J_PASSWORD = "j_password";
        public static final String DEVICE_ID = "device_id";
        public static final String PLATFORM = "platform";
        public static final String VERSION = "version";
    }

}
