package com.tempos21.market.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tempos21.market.db.UserModel;
import com.tempos21.market.device.BinaryInstaller;
import com.tempos21.market.util.MarketPlaceHelper;
import com.worldline.alfredo.R;

import eu.alfred.api.market.responses.listener.SetAppRateResponseListener;
import eu.alfred.api.market.responses.set_app_rate.SetAppRateResponse;

public class RateActivity extends TActivity implements OnClickListener {

    public static final String EXTRA_APP_ID = "appId";
    public static final String EXTRA_APP_PACKAGE = "package";
    private TextView ratingTitle;
    private TextView ratingBody;
    private TextView ratingSave;
    private ProgressBar ratingProgress;
    private RatingBar ratingRate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate);

        findViews();
        initViews();
        setListeners();
    }


    public void findViews() {
        ratingTitle = (TextView) findViewById(R.id.ratingTitle);
        ratingBody = (TextView) findViewById(R.id.ratingBody);
        ratingSave = (TextView) findViewById(R.id.ratingSave);
        ratingProgress = (ProgressBar) findViewById(R.id.ratingsProgress);
        ratingRate = (RatingBar) findViewById(R.id.ratingRate);
    }


    public void initViews() {
        ratingProgress.setVisibility(View.INVISIBLE);
    }


    public void setListeners() {
        ratingSave.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == ratingSave.getId()) {
            storeReview();
        }
    }


    private void storeReview() {
        String appId = getIntent().getExtras().getString(EXTRA_APP_ID);
        String packageName = getIntent().getExtras().getString(EXTRA_APP_PACKAGE);
        String title = ratingTitle.getText().toString();
        String body = ratingBody.getText().toString();
        int rate = Math.round(ratingRate.getRating());

        UserModel model = new UserModel(this);
        int userId = model.getUser().getId();

        int versionNumber = BinaryInstaller.getVersion(this, packageName);

        int appIdParsed = -1;
        try {
            appIdParsed = Integer.parseInt(appId);
        } catch (Exception ignored) {
        }
        MarketPlaceHelper.getInstance().marketPlace.setAppRate(
                appIdParsed,
                title,
                rate,
                userId,
                body,
                versionNumber,
                new SetAppRateResponseListener() {
                    @Override
                    public void onSuccess(SetAppRateResponse setAppRateResponse) {
                        onSetRatingServiceResponse(true);
                    }

                    @Override
                    public void onError(Exception e) {
                        onSetRatingServiceResponse(false);
                    }
                });
        /*
        SetRatingService service = new SetRatingService(Constants.SET_RATING_SERVICE, this);
        service.setupParameters(appId, title, body, rate, userId, versionNumber);
        service.setOnSetRatingServiceResponseListener(this);
        service.runService();
        */
    }


    public void onSetRatingServiceResponse(boolean success) {
        if (success) {
            this.setResult(RESULT_OK);
            finish();
        }
    }

}
