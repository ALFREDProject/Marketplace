package com.tempos21.market.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.tempos21.market.Constants;
import com.tempos21.market.db.AppModelDetail;
import com.tempos21.market.db.ScreenshotsModel;
import com.tempos21.market.ui.adapter.ScreenshotsAdapter;
import com.tempos21.market.ui.presenter.AppDetailPresenterImpl;
import com.tempos21.market.ui.view.GenericViewSubRequest;
import com.tempos21.market.ui.view.SizeChangeNotifyingTextView;
import com.tempos21.market.util.BinaryInstaller;
import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.market.util.Util;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.AppRate;
import com.tempos21.mymarket.sdk.model.app.AppDetail;
import com.tempos21.mymarket.sdk.model.app.Os;
import com.tempos21.mymarket.sdk.model.app.Platform;
import com.tempos21.mymarket.sdk.util.Logger;
import com.tempos21.rampload.RampLoad;
import com.worldline.alfredo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eu.alfred.api.market.responses.listener.GetAppDetailsResponseListener;
import eu.alfred.api.market.responses.listener.InstallBinaryResponseListener;


public class AppActivity extends TActivity implements GenericViewSubRequest<AppDetail, List<AppRate>>, OnClickListener, RampLoad.DownloadListener, RampLoad.StatusListener, MyMarket.OnMarketSetInstalledAppsListener {

    public static final String EXTRA_APP_ID = "appId";
    public static final String EXTRA_PACKAGE_NAME = "packageName";
    public static final String EXTRA_CAN_REVIEW = "canReview";
    public static final String PACKAGE_PERSONAL_ASSISTANT = "eu.alfred.personalassistant";
    private final Random random = new Random(System.currentTimeMillis());
    private ImageView appIcon;
    private TextView appName;
    private TextView appVersion;
    private TextView appSize;
    private TextView appPublicationDate;
    private SizeChangeNotifyingTextView appDescription;
    private View appProgress;
    private View appScreenshotsLayout;
    private TextView appInstall;
    private RatingBar appRating;
    private Gallery appScreenshots;
    private View appInstallProgres;
    private TextView appUnableLoad;
    private TextView authorName;
    private View appSizeLayout;
    private TextView writeReview;
    private DisplayImageOptions options;
    private ScrollView scroll;
    private String id;
    private AppModelDetail appModel;
    private AppDetailPresenterImpl appDetailPresenter;
    private AppDetail app;
    private ScreenshotsModel screenShotModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);

        findViews();
        setListeners();
        configUniversalLoader();

        id = getIntent().getExtras().getString(AppActivity.EXTRA_APP_ID, "");
        appModel = new AppModelDetail(this);
        screenShotModel = new ScreenshotsModel(this);

        getData();
    }

    @Override
    public void onPause() {
        super.onPause();

        MyMarket.removeDownloadListener(this);
        MyMarket.removeStatusListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        MyMarket.addDownloadListener(this);
        MyMarket.addStatusListener(this);

        getData();

        // TODO set installed apps
    }

    private void getData() {
        if (Util.netConnect(this)) {
            appUnableLoad.setVisibility(View.INVISIBLE);
            scroll.setVisibility(View.INVISIBLE);
            appProgress.setVisibility(View.VISIBLE);

            getDataFromServer();
        } else {
            appUnableLoad.setVisibility(View.VISIBLE);
            appUnableLoad.setText(R.string.unable_load);
            scroll.setVisibility(View.INVISIBLE);
            appProgress.setVisibility(View.INVISIBLE);
        }
    }

    public void findViews() {
        appName = (TextView) findViewById(R.id.appName);
        authorName = (TextView) findViewById(R.id.authorName);
        appDescription = (SizeChangeNotifyingTextView) findViewById(R.id.appDescription);
        appVersion = (TextView) findViewById(R.id.appVersion);
        appSize = (TextView) findViewById(R.id.appSize);
        appPublicationDate = (TextView) findViewById(R.id.appPublicationDate);
        appProgress = findViewById(R.id.appProgress);
        appRating = (RatingBar) findViewById(R.id.appRating);
        appIcon = (ImageView) findViewById(R.id.appIcon);
        appScreenshots = (Gallery) findViewById(R.id.appScreenshots);
        appScreenshotsLayout = findViewById(R.id.appScreenshotsLayout);
        appInstall = (TextView) findViewById(R.id.appInstall);
        appSizeLayout = findViewById(R.id.appSizeLayout);
        appInstallProgres = findViewById(R.id.appInstallProgress);
        appUnableLoad = (TextView) findViewById(R.id.appUnableLoad);
        writeReview = (TextView) findViewById(R.id.writeReview);
        scroll = (ScrollView) findViewById(R.id.scroll);
    }

    public void setListeners() {
        appInstall.setOnClickListener(this);
        writeReview.setOnClickListener(this);
        appIcon.setOnClickListener(this);
    }

    public void configUniversalLoader() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_image) // resource or drawable
                .showImageForEmptyUri(R.drawable.no_image) // resource or drawable
                .showImageOnFail(R.drawable.no_image) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(0)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(false) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                .build();
    }

    private void getDataFromServer() {

        MarketPlaceHelper.getInstance().marketPlace.getAppDetails(id, new GetAppDetailsResponseListener() {
            @Override
            public void onSuccess(eu.alfred.api.market.responses.apps.AppDetail appDetail) {
                if (appDetail != null) {

                    AppDetail app;

                    app = new AppDetail();

                    app.id = appDetail.id;
                    app.name = appDetail.name;
                    app.description = appDetail.description;
                    app.screenshots = appDetail.screenshots;
                    app.versionString = appDetail.versionString;
                    app.iconUrl = appDetail.iconUrl;
                    app.rating = appDetail.rating;
                    app.promoUrl = appDetail.promoUrl;
                    app.versionNumber = appDetail.versionNumber;
                    app.date = appDetail.date;
                    app.size = appDetail.size;
                    app.notificationEmails = appDetail.notificationEmails;
                    app.allowed = appDetail.allowed;
                    app.supportEmails = appDetail.supportEmails;
                    app.author = appDetail.author;
                    app.externalUrl = appDetail.externalUrl;
                    app.versionId = appDetail.versionId;
                    app.externalBinary = appDetail.externalBinary;
                    app.packageName = appDetail.packageName;

                    app.platform = new ArrayList<Platform>();


                    List<Platform> platforms = new ArrayList<Platform>();
                    Platform platform;
                    for (eu.alfred.api.market.responses.apps.Platform platform1 : appDetail.platform) {
                        platform = new Platform();
                        platform.id = platform1.id != null ? platform1.id : -1;
                        platform.name = platform1.name;

                        Os os = new Os();
                        os.extension = platform1.os.extension;
                        os.id = platform1.os.id != null ? platform1.os.id : -1;
                        os.name = platform1.os.name;

                        platform.os = os;
                        platforms.add(platform);
                    }
                    app.platform = platforms;

                    onViewSuccess(app);
                } else {
                    onViewError(-1, new NullPointerException());
                }
            }

            @Override
            public void onError(Exception e) {
                onViewError(-1, e);
            }
        });
        /*
        appDetailPresenter = new AppDetailPresenterImpl(this);
        appDetailPresenter.getAppDetail((long) Integer.parseInt(id));
        */
    }

    @Override
    public void showProgress() {
        scroll.setVisibility(View.GONE);
        appProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        scroll.setVisibility(View.VISIBLE);
        appProgress.setVisibility(View.GONE);
    }

    @Override
    public void onViewSuccess(AppDetail appDetail) {
        app = appDetail;

        showInstallButtons();
        setInitialView();
        //saveInDB(appDetail);
        setData(app);
    }

    @Override
    public void onViewError(long id, Exception e) {
        if (e.getMessage() != null && e.getMessage().equalsIgnoreCase(Constants.WRONG_CREDENTIALS)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            appUnableLoad.setVisibility(View.VISIBLE);
            appUnableLoad.setText(R.string.unable_load);
            scroll.setVisibility(View.INVISIBLE);
        }
    }

    private void showInstallButtons() {
        switch (MyMarket.getInstance().getAppState(app)) {
            case UPDATE_FROM_DISK:
                appInstall.setText(getString(R.string.update_app_title));
                appInstallProgres.setVisibility(View.GONE);
                appInstall.setVisibility(View.VISIBLE);
                break;

            case DOWNLOAD_UPDATE:
                appInstall.setText(getString(R.string.update_app_title));
                appInstallProgres.setVisibility(View.GONE);
                appInstall.setVisibility(View.VISIBLE);
                break;

            case INSTALL:
                appInstall.setText(R.string.install);
                appInstallProgres.setVisibility(View.GONE);
                appInstall.setVisibility(View.VISIBLE);
                // TODO set installed apps
                break;

            case UNINSTALL:
                if (!TextUtils.equals(PACKAGE_PERSONAL_ASSISTANT, app.packageName)) {
                    appInstall.setText(R.string.uninstall);
                    appInstall.setVisibility(View.VISIBLE);
                    appInstallProgres.setVisibility(View.GONE);
                    // TODO set installed apps
                } else {
                    appInstall.setText(R.string.uninstall);
                    appInstall.setVisibility(View.INVISIBLE);
                    appInstallProgres.setVisibility(View.GONE);
                    // TODO set installed apps
                }
                break;

            case DOWNLOAD:
                appInstall.setText(R.string.download);
                appInstall.setVisibility(View.VISIBLE);
                appInstallProgres.setVisibility(View.GONE);
                // TODO set installed apps
                break;

            case DOWNLOADING:
                appInstall.setVisibility(View.GONE);
                appInstallProgres.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setInitialView() {
        scroll.setVisibility(View.VISIBLE);
        appInstallProgres.setVisibility(View.GONE);
        appUnableLoad.setVisibility(View.GONE);
    }

    private void saveInDB(AppDetail appDetail) {
        if (appModel.isOnDB("" + appDetail.id)) {
            appModel.deleteApp(appDetail);
            appModel.insertApp(appDetail);
        } else {
            appModel.insertApp(appDetail);
        }

        if (screenShotModel.isOnDB("" + appDetail.id)) {
            screenShotModel.deleteScreenshot("" + appDetail.id);
            screenShotModel.insertScreenshot(appDetail.id, appDetail.screenshots);
        } else {
            screenShotModel.insertScreenshot(appDetail.id, appDetail.screenshots);
        }
    }

    public void setData(AppDetail app1) {

        appRating.setRating((float) app1.rating);
        appName.setText(app1.name);
        authorName.setText(app1.author);

        ImageLoader.getInstance().displayImage(Constants.GET_IMAGE_ICON + app1.iconUrl, appIcon, options);

        showInstallButtons();

        appDescription.setText(app1.description);

        if (app1.screenshots.size() > 0) {
            appScreenshots.setAdapter(new ScreenshotsAdapter(this, app1.screenshots));
            appScreenshotsLayout.setVisibility(View.VISIBLE);
        } else {
            appScreenshotsLayout.setVisibility(View.GONE);
        }

        appVersion.setText(" " + app1.versionString);

        if (app1.externalBinary) {
            appSizeLayout.setVisibility(View.GONE);
        } else {
            appSizeLayout.setVisibility(View.VISIBLE);
            appSize.setText(" " + Util.getFormattedSize(app1.size));
        }

        appPublicationDate.setText(" " + Util.getFormattedDate(app1.date));

        appProgress.setVisibility(View.GONE);
    }

    public void setDataFromDB() {
        setData(appModel.getApp(id));
    }

    @Override
    public void onViewSuccessSubRequest(List<AppRate> reviews) {

        /*
        ReviewModel reviewModel = new ReviewModel(this);
        reviewModel.setReviews(reviews, id);
        */
    }

    @Override
    public void onViewErrorSubRequest(long id, Exception e) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.writeReview:
                launchRatings();
                break;
            case R.id.appInstall:
                installOrUninstallApp();
                break;
            case R.id.appIcon:
                if (isPackageInstalled(app.packageName, this)) {
                    openApp(app.packageName);
                }
                break;
        }
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

    private void launchRatings() {
        Intent i = new Intent(this, RatingsActivity.class);
        i.putExtra(RatingsActivity.EXTRA_APP_ID, id);
        i.putExtra(AppActivity.EXTRA_PACKAGE_NAME, app.packageName);
        i.putExtra(RatingsActivity.EXTRA_CAN_REVIEW, getIntent().getExtras().getBoolean(EXTRA_CAN_REVIEW, true));
        startActivity(i);
    }

    private void installOrUninstallApp() {
        switch (MyMarket.getInstance().getAppState(app)) {
            case UPDATE_FROM_DISK:
                MyMarket.getInstance().update(app);
                appInstallProgres.setVisibility(View.VISIBLE);
                appInstall.setVisibility(View.GONE);
                break;

            case DOWNLOAD_UPDATE:
                appInstallProgres.setVisibility(View.VISIBLE);
                appInstall.setVisibility(View.GONE);

                MarketPlaceHelper.getInstance().marketPlace.installBinary(app.id, "" + app.versionId, new InstallBinaryResponseListener() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            BinaryInstaller.installApplication(AppActivity.this, "" + app.id, "" + app.versionId, app.versionNumber, s);
                        } catch (Exception e) {
                        }
                        showInstallButtons();
                    }

                    @Override
                    public void onError(Exception e) {
                        showInstallButtons();
                    }
                });
                break;

            case DOWNLOAD:
                appInstallProgres.setVisibility(View.VISIBLE);
                appInstall.setVisibility(View.GONE);

                MarketPlaceHelper.getInstance().marketPlace.installBinary(app.id, "" + app.versionId, new InstallBinaryResponseListener() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            BinaryInstaller.installApplication(AppActivity.this, "" + app.id, "" + app.versionId, app.versionNumber, s);
                            // TODO set installed apps
                        } catch (Exception e) {
                        }
                        showInstallButtons();
                    }

                    @Override
                    public void onError(Exception e) {
                        showInstallButtons();
                    }
                });
                break;

            case UNINSTALL:
                if (!TextUtils.equals(PACKAGE_PERSONAL_ASSISTANT, app.packageName)) {
                    MyMarket.getInstance().uninstall(app);
                    // TODO set installed apps
                }
                break;

            case INSTALL:
                MyMarket.getInstance().install(app);
                break;
        }
    }

    @Override
    public void onDownloadStart(int id, String url, String path) {
        showInstallButtons();
    }


    @Override
    public void onDownloadProgress(int id, String url, String path, float progress) {
        showInstallButtons();
    }


    @Override
    public void onDownloadFinish(int id, String url, String path) {
        showInstallButtons();
    }


    @Override
    public void onDownloadFailed(int id, String url, String path, Exception exception) {
        showInstallButtons();
    }


    @Override
    public void onStatusIdle() {
        Logger.logE("Idle");
    }


    @Override
    public void onStatusDownloading() {
        Logger.logE("Downloading");
    }


    @Override
    public void onSetInstalledAppsSuccess() {
    }


    @Override
    public void onSetInstalledAppsError(long id, Exception e) {
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
