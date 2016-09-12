package com.tempos21.market.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.tempos21.market.C2DMReceiver;
import com.tempos21.market.client.http.T21HttpClientWithSSL;
import com.tempos21.market.device.BinaryInstaller;
import com.tempos21.market.ui.fragment.HomeFragment;
import com.tempos21.market.util.MToast;
import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;


public class StartUpActivity extends TActivity {

    private final static String CLASS_NAME = StartUpActivity.class.getSimpleName() + " ";

    /**
     * This method checks wether the Android device is connected to the Internet
     * or not.
     *
     * @return true if the device has internet, false otherwise.
     */

    private static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TLog.d(CLASS_NAME + "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        clearApksCache();

        if (isOnline(this)) {
            T21HttpClientWithSSL.loadKeyStore(this);
            startLogin();
        } else {
            MToast.makeText(this, getString(R.string.app_name) + getString(R.string.no_internet), MToast.LENGTH_LONG).show();
            finish();
        }
        TLog.d(CLASS_NAME + "onStart()");

    }

    private void startLogin() {
        boolean login_error = false;
        Intent intent = new Intent(this, LoginActivity.class);

        if (getIntent().getExtras() != null) {
            login_error = getIntent().getExtras().getBoolean(HomeFragment.LOGIN_ERROR, false);
        }
        if (login_error) {
            intent.putExtra(HomeFragment.LOGIN_ERROR, true);
        }
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            startHome();
        }
    }


    private void startHome() {
        TLog.d(CLASS_NAME + "ready to register push");
        C2DMReceiver.register(this);
        TLog.d(CLASS_NAME + "waiting push...");
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        clearApksCache();
        finish();
    }


    private void clearApksCache() {
        BinaryInstaller.deleteApksCache(this);
    }

}