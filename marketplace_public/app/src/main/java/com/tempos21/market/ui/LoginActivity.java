package com.tempos21.market.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tempos21.market.Constants;
import com.tempos21.market.gcm.RegisterGCM;
import com.tempos21.market.ui.fragment.MainActivityFragment;
import com.tempos21.market.ui.presenter.LoginPresenterImpl;
import com.tempos21.market.ui.view.GenericView;
import com.tempos21.market.util.DialogUtils;
import com.tempos21.market.util.MToast;
import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.market.util.Util;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.MyMarketPreferences;
import com.tempos21.mymarket.sdk.util.MarketPlaceHelper2;
import com.tempos21.rampload.RampLoad;
import com.tempos21.rampload.model.RampLoadDownload;
import com.tempos21.rampload.util.Prefs;
import com.worldline.alfredo.R;

import java.io.File;

public class LoginActivity extends TActivity implements GenericView<com.tempos21.mymarket.sdk.model.User>, OnClickListener {

    private static final int ID_PERSONAL_ASSISTANT = -654;
    private EditText userText;
    private EditText passwordText;
    private View btnEnter;
    private ProgressBar loginProgress;

    private LoginPresenterImpl presenter;

    private String userName;
    private String userPass;
    private volatile boolean startedLogin;
    private boolean appPaused;

    public static void installApplication(Context context, String path) throws Exception {
        final PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(path, 0);
        if (packageInfo == null) {
            throw new Exception();
        }
        Uri packageURI = Uri.parse("package:" + packageInfo.packageName);

        Intent intent = new Intent(Intent.ACTION_VIEW, packageURI);
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private static String getCacheDirectory(Context context) {
        return Environment.getExternalStorageDirectory() + "/" + "marketDownload" + "/";
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if (!isPersonalAssistantInstalled()) {
            DialogUtils.showConfirmDialog(this, getString(R.string.need_pa_title_dialog), getString(R.string.need_pa_msg_dialog), getString(R.string.need_pa_ok_dialog), getString(R.string.need_pa_exit_dialog), false, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadPersonalAssistant();
                    finish();
                }
            }, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            return;
        }

        findViews();
        setListeners();
        init();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MarketPlaceHelper.getInstance().marketPlace == null) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MarketPlaceHelper.getInstance().setMarketPlace(marketPlace);
                    MarketPlaceHelper2.getInstance().setMarketPlace(MarketPlaceHelper.getInstance().marketPlace);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startLogin();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        appPaused = true;
    }

    private void downloadPersonalAssistant() {
        RampLoad.getInstance().addDownloadListener(new RampLoad.DownloadListener() {
            @Override
            public void onDownloadStart(int id, String url, String path) {
            }

            @Override
            public void onDownloadProgress(int id, String url, String path, float progress) {
            }

            @Override
            public void onDownloadFinish(int id, String url, String path) {
                if (id == ID_PERSONAL_ASSISTANT) {
                    try {
                        installApplication(LoginActivity.this, path);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, getString(R.string.error_downloading_personal_assistant), Toast.LENGTH_SHORT).show();
                    }
                    int counter = 20;
                    while (RampLoad.getInstance().removeDownloadListener(this) && counter > 0) {
                        counter--;
                    }
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.error_downloading_personal_assistant), Toast.LENGTH_SHORT).show();
                    int counter = 20;
                    while (RampLoad.getInstance().removeDownloadListener(this) && counter > 0) {
                        counter--;
                    }
                    finish();
                }
            }

            @Override
            public void onDownloadFailed(int id, String url, String path, Exception exception) {
                Toast.makeText(LoginActivity.this, getString(R.string.error_downloading_personal_assistant), Toast.LENGTH_SHORT).show();
                int counter = 20;
                while (RampLoad.getInstance().removeDownloadListener(this) && counter > 0) {
                    counter--;
                }
                finish();
            }
        });
        try {
            RampLoad.getInstance().download(createRampLoadDownloadForPersonalAssistant());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.error_downloading_personal_assistant), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private RampLoadDownload createRampLoadDownloadForPersonalAssistant() {
        String url = getUrlPersonalAssistant();
        String destination = getDestinationPersonalAssistant();
        return new RampLoadDownload(ID_PERSONAL_ASSISTANT, "PersonalAssistantDownload", url, destination);
    }

    private String getUrlPersonalAssistant() {
        String url = ""; // TODO
        return url;
    }

    private String getDestinationPersonalAssistant() {
        String destinationPath = getCacheDirectory(this) + "marketDownload.apk";
        return destinationPath;
    }

    private boolean isPersonalAssistantInstalled() {
        return isPackageInstalled(AppActivity.PACKAGE_PERSONAL_ASSISTANT, this);
    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void openApp(String packageName) {
        try {
            String appToOpenPackage = packageName;
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appToOpenPackage);
            startActivity(launchIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findViews() {
        btnEnter = findViewById(R.id.enter);
        userText = (EditText) findViewById(R.id.dasUser);
        passwordText = (EditText) findViewById(R.id.password);
        loginProgress = (ProgressBar) findViewById(R.id.loginProgress);
    }

    private void setListeners() {
        btnEnter.setOnClickListener(this);
    }

    private void init() {
        appPaused = false;
        presenter = new LoginPresenterImpl(this, this);

        if (Util.netConnect(this)) {
            startLogin();
        } else {
            MToast.makeText(this, getString(R.string.app_name) + getString(R.string.no_internet), MToast.LENGTH_LONG).show();
            finish();
        }
    }

    private void startLogin() {
        if (startedLogin) {
            return;
        }
        if (MarketPlaceHelper.getInstance().marketPlace == null) {
            return;
        }
        startedLogin = true;
        userName = Prefs.getString(Constants.MY_MARKET_PREFERENCE_KEY_USER_NAME, "");
        userPass = Prefs.getString(Constants.MY_MARKET_PREFERENCE_KEY_USER_PASSWORD, "");

        if (!userName.equalsIgnoreCase("") && !userPass.equalsIgnoreCase("")) {
            userText.setFocusableInTouchMode(false);
            passwordText.setFocusableInTouchMode(false);
            userText.setText(userName);
            passwordText.setText(userPass);
            hideSoftKeyboard();
            presenter.login(userName, userPass);
            hideSoftKeyboard();
        } else {
            userText.setFocusableInTouchMode(true);
            passwordText.setFocusableInTouchMode(true);
            userText.setText("");
            passwordText.setText("");
        }
    }

    @Override
    public void showProgress() {
        loginProgress.setVisibility(View.VISIBLE);
        btnEnter.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        loginProgress.setVisibility(View.GONE);
        btnEnter.setVisibility(View.VISIBLE);
    }    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
        presenter.login(userText.getText().toString(), passwordText.getText().toString());
        hideSoftKeyboard();
    }

    @Override
    public void onViewSuccess(com.tempos21.mymarket.sdk.model.User list) {
        MyMarketPreferences.getInstance(this).setString(Constants.MY_MARKET_PREFERENCE_KEY_NAME, list.name);
        MyMarketPreferences.getInstance(this).setInt(Constants.MY_MARKET_PREFERENCE_KEY_USER_ID, list.id.intValue());

        String token = MyMarketPreferences.getInstance(this).getString(Constants.MY_MARKET_PREFERENCE_KEY_TOKEN, null);
        if (token == null) {
            new RegisterGCM(this);
        } else {
            Log.e(LoginActivity.class.toString(), "Token registered: " + token);
        }

        if (!appPaused) {
            Intent intent = new Intent(this, MainActivityFragment.class);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void onViewError(long id, Exception e) {
        if (e.getMessage() != null && (e.getMessage().equalsIgnoreCase(MyMarket.SERVER_ERROR_AUTHENTICATION_REQUIRED) || e.getMessage().equalsIgnoreCase(MyMarket.SERVER_ERROR_GATEWAY_TIME_OUT) || e.getMessage().equalsIgnoreCase(MyMarket.SERVER_ERROR_UNAVAILABLE))) {
            finish();
        }
        showMessage(e.getMessage());
    }




}