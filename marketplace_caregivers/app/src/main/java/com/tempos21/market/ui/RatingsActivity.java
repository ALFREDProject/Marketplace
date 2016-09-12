package com.tempos21.market.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tempos21.market.client.bean.Rating;
import com.tempos21.market.client.bean.Ratings;
import com.tempos21.market.device.BinaryInstaller;
import com.tempos21.market.device.Connection;
import com.tempos21.market.ui.adapter.RatingsAdapter;
import com.tempos21.market.util.MarketPlaceHelper;
import com.worldline.alfredo.R;

import eu.alfred.api.market.responses.app_rate.AppRate;
import eu.alfred.api.market.responses.app_rate.AppRateList;
import eu.alfred.api.market.responses.listener.GetAppRateListResponseListener;


public class RatingsActivity extends TActivity implements OnClickListener {

    public final static String EXTRA_APP_ID = "appId";
    public static final String EXTRA_CAN_REVIEW = "canReview";
    public final String EXTRA_PACKAGE_NAME = "packageName";
    private ListView ratingsList;
    private String appId;
    private ProgressBar ratingsProgress;
    private RatingsAdapter ratingsAdapter;
    private View ratingsRate;
    private String packageAppName;
    private TextView noReviews;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratings);
        findViews();
        setListeners();

        if (Connection.netConnect(this)) {
            noReviews.setVisibility(View.GONE);
            getData();
        } else {
            ratingsProgress.setVisibility(View.GONE);
            ratingsList.setVisibility(View.INVISIBLE);
            ratingsRate.setVisibility(View.GONE);
            noReviews.setVisibility(View.VISIBLE);
            noReviews.setText(R.string.not_connection);
        }

    }


    public void findViews() {
        ratingsList = (ListView) findViewById(R.id.ratingsList);
        ratingsProgress = (ProgressBar) findViewById(R.id.ratingsProgress);
        ratingsRate = findViewById(R.id.ratingWrite);
        noReviews = (TextView) findViewById(R.id.noReviews);
    }


    public void setListeners() {
        ratingsRate.setOnClickListener(this);
    }


    public void getData() {
        appId = getIntent().getExtras().getString(EXTRA_APP_ID);
        packageAppName = getIntent().getExtras().getString(EXTRA_PACKAGE_NAME);
//		int versionNumber = BinaryInstaller.getVersion(this, packageAppName);

        if (BinaryInstaller.isAppInstalled(this, packageAppName) && getIntent().getExtras().getBoolean(EXTRA_CAN_REVIEW, true)) {
            ratingsRate.setVisibility(View.VISIBLE);
        } else {
            ratingsRate.setVisibility(View.GONE);
        }

        ratingsList.setVisibility(View.GONE);
        ratingsProgress.setVisibility(View.VISIBLE);

        MarketPlaceHelper.getInstance().marketPlace.getAppRateList(appId, new GetAppRateListResponseListener() {
            @Override
            public void onSuccess(AppRateList appRateList) {
                Ratings ratings = new Ratings();
                Rating rating;
                for (AppRate appRate : appRateList.appRates) {
                    rating = new Rating();
                    rating.setId(appRate.id);
                    rating.setDateCreation(appRate.dateCreation);
                    rating.setRate(appRate.rate);
                    rating.setText(appRate.text);
                    rating.setTitle(appRate.title);
                    rating.setUserFullName(appRate.userFullName);
                    rating.setUserId(appRate.userId);
                    rating.setUserName(appRate.userName);
                    rating.setVersionString(appRate.versionString);
                    ratings.add(rating);
                }
                onGetRatingsServiceResponse(true, ratings);
            }

            @Override
            public void onError(Exception e) {
                onGetRatingsServiceResponse(false, new Ratings());
            }
        });
        // TODO
        /*
        GetRatings getRatings = new GetRatings(Constants.GET_RATINGS_SERVICE, this);
        getRatings.setOnGetRatingsServiceResponseListener(this);
        getRatings.setupParameters(appId);
//		getRatings.setupParameters(""+versionNumber);
        getRatings.runService();
        */
    }


    public void onGetRatingsServiceResponse(boolean success, Ratings ratings) {
        ratingsProgress.setVisibility(View.GONE);
        if (success) {
            if (ratings.size() < 1) {
                ratingsList.setVisibility(View.INVISIBLE);
                noReviews.setVisibility(View.VISIBLE);
            } else {
                ratingsList.setVisibility(View.VISIBLE);
                noReviews.setVisibility(View.GONE);
                ratingsAdapter = new RatingsAdapter(this, ratings);
                ratingsList.setAdapter(ratingsAdapter);
            }
        } else {
            /*if (responseCode == ServiceErrorCodes.LOGIN_ERROR) {
                Intent intent = new Intent(this, StartUpActivity.class);
                intent.putExtra(HomeFragment.LOGIN_ERROR, true);
                startActivity(intent);
                finish();
            } else {*/
            ratingsList.setVisibility(View.INVISIBLE);
            noReviews.setVisibility(View.VISIBLE);
            noReviews.setText(R.string.server_error);
            //}

        }
    }


    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, RateActivity.class);
        i.putExtra(RateActivity.EXTRA_APP_ID, appId);
        i.putExtra(RateActivity.EXTRA_APP_PACKAGE, packageAppName);
        startActivityForResult(i, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (resultCode==RESULT_OK) {
        getData();
        //}
    }
}
