package com.tempos21.market.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tempos21.market.client.bean.User;
import com.tempos21.market.client.service.input.LoginInput;
import com.tempos21.market.db.UserModel;
import com.tempos21.market.device.Device;
import com.tempos21.market.device.DeviceUuidFactory;
import com.tempos21.market.device.OsVersion;
import com.tempos21.market.ui.fragment.HomeFragment;
import com.tempos21.market.ui.view.SlidingMenu;
import com.tempos21.market.util.MToast;
import com.tempos21.market.util.MarketPlaceHelper;
import com.worldline.alfredo.R;

import eu.alfred.api.market.responses.listener.LoginResponseListener;

public class LoginActivity extends TActivity implements OnClickListener {
    // Constants for methods setResults()

    public static final String USER = "user";
    public static final String RESPONSE_CODE = "response_code";

    GestureDetector gestures;
    SlidingMenu m;

    private EditText userText;
    private EditText passwordText;
    private int version = 0;
    private String platform = "Android";
    private DeviceUuidFactory device;
    private View btnEnter;
    private User user;

    private ProgressBar loginProgress;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOrientation();
        setContentView(R.layout.login);
        findViews();
        setListeners();
        getData();
        setData();
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
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        performLogin();
                    }
                });
            }
        }).start();

    }

    public void setOrientation() {
        if (Device.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

    private void getData() {
        boolean login_error = false;

        if (getIntent().getExtras() != null) {
            login_error = getIntent().getExtras().getBoolean(HomeFragment.LOGIN_ERROR, false);
        }

        if (login_error) {
            user = null;
        } else {
            UserModel model = new UserModel(this);
            user = model.getUser();
        }
    }

    private void setData() {
        setLoadingMode(false);

        if (user != null) {
            userText.setFocusableInTouchMode(false);
            passwordText.setFocusableInTouchMode(false);
            userText.setText(user.getJ_username());
            passwordText.setText(user.getJ_password());
        } else {
            userText.setFocusableInTouchMode(true);
            passwordText.setFocusableInTouchMode(true);
            userText.setText("");
            passwordText.setText("");
            // if (Constants.DEBUG) {
            // userText.setText(getString(R.string.test_user));
            // passwordText.setText(getString(R.string.test_password));
            //
            // }
        }
    }

    private void getDeviceData() {
        if (user != null) {
            try {
                device = new DeviceUuidFactory(this);
                user.setDevice_id(device.getDeviceUuid().toString());
            } catch (Exception e) {
                user.setDevice_id("unknown");
            }
            version = OsVersion.getOsVersion(this);

            user.setVersion(Integer.toString(version));

            user.setPlatform(platform);
        }
    }

    @Override
    public void onClick(View v) {
        user = new User();
        user.setJ_username(userText.getText().toString());
        user.setJ_password(passwordText.getText().toString());
        performLogin();
    }

    public void performLogin() {
        if (user != null && MarketPlaceHelper.getInstance().marketPlace != null) {
            setLoadingMode(true);
            getDeviceData();
            LoginInput login = getLoginParams();
            // TODO perform login in the market
            MarketPlaceHelper.getInstance().marketPlace.login(login.getJ_username(), login.getJ_password(), new LoginResponseListener() {
                @Override
                public void onSuccess(eu.alfred.api.market.responses.login.User user) {
                    if (user != null) {
                        onLoginResponse(true, user);
                    } else {
                        onLoginResponse(false, user);
                    }
                }

                @Override
                public void onError(Exception e) {
                    onLoginResponse(false, null);
                }
            });
        }
    }

    private LoginInput getLoginParams() {
        LoginInput li = new LoginInput();

        li.setJ_username(user.getJ_username());
        li.setJ_password(user.getJ_password());
        li.setDevice_id(user.getDevice_id());
        li.setPlatform(user.getPlatform());
        li.setVersion(user.getVersion());

        return li;
    }

    // TODO
    public void onLoginResponse(boolean successLogin, eu.alfred.api.market.responses.login.User loggedUser) {
        if (successLogin && user != null) {
            if (MarketPlaceHelper.getInstance().marketPlace == null) {
                MarketPlaceHelper.getInstance().setMarketPlace(marketPlace);
            }
            user.setApprover(loggedUser.approver);
            user.setIsTester(loggedUser.tester);
            user.setName(loggedUser.name);
            user.setId(loggedUser.id);
            UserModel model = new UserModel(this);
            model.setUser(user);
            setResult(RESULT_OK);
            finish();
        } else {
            setLoadingMode(false);
            HandleErrors();

            userText.setFocusableInTouchMode(true);
            passwordText.setFocusableInTouchMode(true);
            userText.setText("");
            passwordText.setText("");
            // TODO: Set error messages
        }

    }

    private void HandleErrors() {
        String message = getString(R.string.ErrGeneral);
        /*message;
        switch (responseCode) {
            case ServiceErrorCodes.LOGIN_ERROR:
                message = getString(R.string.ErrLogin);
                break;
            default:
                message = getString(R.string.ErrGeneral);
        }*/
        MToast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setLoadingMode(boolean isLoading) {
        userText.setEnabled(!isLoading);
        passwordText.setEnabled(!isLoading);
        btnEnter.setEnabled(!isLoading);
        if (isLoading) {
            loginProgress.setVisibility(View.VISIBLE);
        } else {
            loginProgress.setVisibility(View.GONE);
        }
    }

}