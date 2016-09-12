package com.tempos21.market.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.tempos21.market.Constants;
import com.tempos21.market.gcm.RegisterGCM;
import com.tempos21.market.util.Util;
import com.tempos21.market.ui.presenter.LoginPresenterImpl;
import com.tempos21.market.ui.fragment.MainActivityFragment;
import com.tempos21.market.ui.view.GenericView;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.MyMarketPreferences;
import com.worldline.alfredo.R;
import com.tempos21.market.util.MToast;


public class LoginActivity extends TActivity implements GenericView<com.tempos21.mymarket.sdk.model.User>, OnClickListener {

	private EditText userText;
	private EditText passwordText;
	private View btnEnter;
	private ProgressBar loginProgress;

	private LoginPresenterImpl presenter;

	private String userName;
	private String userPass;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		findViews();
		setListeners();
		init();
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


	private void init(){
		presenter = new LoginPresenterImpl(this, this);

		if (Util.netConnect(this)) {
			startLogin();
		}else{
			MToast.makeText(this, getString(R.string.app_name) + getString(R.string.no_internet), MToast.LENGTH_LONG).show();
			finish();
		}
	}


	private void startLogin() {
		userName = MyMarketPreferences.getInstance(this).getString(Constants.MY_MARKET_PREFERENCE_KEY_USER_NAME, "");
		userPass = MyMarketPreferences.getInstance(this).getString(Constants.MY_MARKET_PREFERENCE_KEY_USER_PASSWORD, "");

		if (!userName.equalsIgnoreCase("") && !userPass.equalsIgnoreCase("")) {
			userText.setFocusableInTouchMode(false);
			passwordText.setFocusableInTouchMode(false);
			userText.setText(userName);
			passwordText.setText(userPass);
			presenter.login(userName, userPass);
		} else {
			userText.setFocusableInTouchMode(true);
			passwordText.setFocusableInTouchMode(true);
			userText.setText("");
			passwordText.setText("");
		}
	}


	@Override
	public void onClick(View v) {
		hideSoftKeyboard();
		presenter.login(userText.getText().toString(), passwordText.getText().toString());
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
	}


	@Override
	public void onViewSuccess(com.tempos21.mymarket.sdk.model.User list) {
		MyMarketPreferences.getInstance(this).setString(Constants.MY_MARKET_PREFERENCE_KEY_NAME, list.name);
		MyMarketPreferences.getInstance(this).setInt(Constants.MY_MARKET_PREFERENCE_KEY_USER_ID, list.id.intValue());

		String token = MyMarketPreferences.getInstance(this).getString(Constants.MY_MARKET_PREFERENCE_KEY_TOKEN,null);
		if(token == null){
			new RegisterGCM(this);
		}else{
			Log.e(LoginActivity.class.toString(), "Token registered: " + token);
		}

		Intent intent = new Intent(this, MainActivityFragment.class);
		startActivity(intent);
		finish();
	}


	@Override
	public void onViewError(long id, Exception e) {
		if(e.getMessage() != null && (e.getMessage().equalsIgnoreCase(MyMarket.SERVER_ERROR_AUTHENTICATION_REQUIRED) || e.getMessage().equalsIgnoreCase(MyMarket.SERVER_ERROR_GATEWAY_TIME_OUT) || e.getMessage().equalsIgnoreCase(MyMarket.SERVER_ERROR_UNAVAILABLE))){
			finish();
		}
		showMessage(e.getMessage());
	}

}