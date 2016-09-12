package com.tempos21.market.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tempos21.market.Constants;
import com.tempos21.market.db.ReviewModel;
import com.tempos21.market.ui.presenter.RatePresenterImpl;
import com.tempos21.mymarket.domain.dto.request.RateRequest;
import com.tempos21.mymarket.sdk.MyMarketPreferences;
import com.tempos21.mymarket.sdk.model.AppRate;
import com.tempos21.mymarket.sdk.model.Rate;
import com.worldline.alfredo.R;
import com.tempos21.market.util.Util;
import com.tempos21.market.ui.adapter.RatingsAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;


public class RatingsActivity extends TActivity implements RatePresenterImpl.OnRateListener, OnClickListener {

	public final static String EXTRA_APP_ID="appId";
	public final String EXTRA_PACKAGE_NAME = "packageName";
	public static final String EXTRA_CAN_REVIEW = "canReview";

	private ListView ratingsList;
	private String appId;
	private ProgressBar ratingsProgress;
	private RatingsAdapter ratingsAdapter;
	private String packageAppName;
	private TextView noReviews;
    private EditText editWrite;
    private TextView cancelWrite;
    private TextView sendWrite;
    private RatingBar ratingsWrite;
	private ReviewModel reviewModel;
	private TextView reviewTitle;
	private View sendOpinion;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ratings);

        findViews();
		setListeners();

		appId = getIntent().getExtras().getString(EXTRA_APP_ID);
		reviewModel = new ReviewModel(this);

		if(!reviewModel.isEmpty(appId)){
			ratingsList.setVisibility(View.GONE);
			ratingsProgress.setVisibility(View.VISIBLE);
			noReviews.setVisibility(View.GONE);
			getData();
		}else{
			ratingsProgress.setVisibility(View.GONE);
			ratingsList.setVisibility(View.INVISIBLE);
			noReviews.setVisibility(View.VISIBLE);
			noReviews.setText(R.string.no_reviews);
		}

		if(Util.isAppInstalled(this, packageAppName) && getIntent().getExtras().getBoolean(EXTRA_CAN_REVIEW,true)){
			sendOpinion.setVisibility(View.VISIBLE);
			reviewTitle.setText(R.string.describe_problem);
		}else{
			sendOpinion.setVisibility(View.GONE);
			reviewTitle.setText(R.string.opinions);
		}

		if(!Util.netConnect(this)){
			sendOpinion.setVisibility(View.GONE);
			reviewTitle.setText(R.string.opinions);
		}
	}
	
	
	public void findViews() {
		ratingsList = (ListView) findViewById(R.id.ratingsList);
		ratingsProgress = (ProgressBar) findViewById(R.id.ratingsProgress);
		ratingsWrite = (RatingBar)findViewById(R.id.ratingWrite);
		noReviews = (TextView) findViewById(R.id.noReviews);
        editWrite = (EditText) findViewById(R.id.editWrite);
        cancelWrite = (TextView) findViewById(R.id.cancelWrite);
        sendWrite = (TextView) findViewById(R.id.sendWrite);
		reviewTitle = (TextView) findViewById(R.id.reviewTitle);
		sendOpinion = findViewById(R.id.sendOpinion);
	}
	
	
	public void setListeners() {
        cancelWrite.setOnClickListener(this);
        sendWrite.setOnClickListener(this);
	}
	
	
	public void getData() {
		packageAppName = getIntent().getExtras().getString(EXTRA_PACKAGE_NAME);

		ratingsList.setVisibility(View.VISIBLE);
		noReviews.setVisibility(View.GONE);

		ratingsAdapter = new RatingsAdapter(this, reviewModel.getReviews(appId));
		ratingsList.setAdapter(ratingsAdapter);

		ratingsProgress.setVisibility(View.GONE);
	}


	@Override
	public void onClick(View v) {
        switch(v.getId()) {
            case R.id.sendWrite:
				hideSoftKeyboard();
                storeReview();
                break;

            case R.id.cancelWrite:
                finish();
                break;
        }
	}


    private void storeReview() {
        String body = editWrite.getText().toString();
        int rate = Math.round(ratingsWrite.getRating());
        int versionNumber = Util.getVersion(this, packageAppName);
		int userId = MyMarketPreferences.getInstance(this).getInt(Constants.MY_MARKET_PREFERENCE_KEY_USER_ID, 0);

		RateRequest rateRequest = new RateRequest();
		rateRequest.author = userId;
		rateRequest.body = body;
		rateRequest.rate = rate;
		rateRequest.versionNumber = versionNumber;
		rateRequest.title = "";
		rateRequest.id = Integer.parseInt(appId);

		RatePresenterImpl ratePresenter = new RatePresenterImpl(this);
		ratePresenter.setRate(rateRequest);

		ratingsProgress.setVisibility(View.VISIBLE);
		ratingsList.setVisibility(View.GONE);
    }


	@Override
	public void OnRateListenerSucces(Rate rate) {
		ReviewModel reviewModel = new ReviewModel(this);
		reviewModel.setReview(convertToAppRate(rate), appId);

		getData();
	}


	private AppRate convertToAppRate(Rate rate){
		AppRate appRate = new AppRate();

		appRate.id = rate.id;
		appRate.userId = rate.users.id;
		appRate.versionString = rate.versionString;
		appRate.text = rate.comment;
		appRate.dateCreation = createDate(rate.date);
		appRate.userFullName = rate.users.name;
		appRate.title = "";
		appRate.rate = rate.score;
		appRate.userName = rate.users.name;

		return appRate;
	}


	private String createDate(long dateRate){
		Date date = new Date(dateRate);
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		return df2.format(date);
	}


	@Override
	public void OnRateListenerError(Exception e) {
		if(e.getMessage() != null && e.getMessage().equalsIgnoreCase(Constants.WRONG_CREDENTIALS)){
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

}
