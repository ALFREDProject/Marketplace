package com.tempos21.market.gcm;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tempos21.market.Constants;
import com.tempos21.mymarket.sdk.MyMarketPreferences;
import com.worldline.alfredo.R;

import java.io.IOException;


public class RegisterGCM {
	
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    private GoogleCloudMessaging gcm;
	private Activity context;


    public RegisterGCM(Activity context) {
    	this.context = context;
    	
        // Check device for Play Services APK.
        Boolean result = checkPlayServices();
        if (result == null) {
        	Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        } else if (result) {
            new CheckServicesTask().execute();
        }
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private Boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            
        	if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //This device is not supported
                return null;
            }
            return false;
        }
        return true;
    }

    
    private class CheckServicesTask extends AsyncTask<Void, Void, String> {
        
    	@Override
        protected String doInBackground(Void... voids) {
    		//Proceed with GCM registration.
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            
            String regId = MyMarketPreferences.getInstance(context).getString(Constants.MY_MARKET_PREFERENCE_KEY_TOKEN, null);
            if (regId == null) {
                try {
                    regId = gcm.register(Constants.GCM_SENDER_ID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return regId;
        }

        
        @Override
        protected void onPostExecute(String registerId) {
            MyMarketPreferences.getInstance(context).setString(Constants.MY_MARKET_PREFERENCE_KEY_TOKEN, registerId);
        }
        
    }

}

