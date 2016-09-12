package com.tempos21.market.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tempos21.ioutils.filecache.FileLoaderQueue;
import com.tempos21.ioutils.filecache.FileLoaderQueue.OnFileLoadedListener;
import com.tempos21.market.Constants;
import com.tempos21.market.client.bean.App;
import com.tempos21.market.client.bean.OS;
import com.tempos21.market.client.bean.Platform;
import com.tempos21.market.client.bean.Platforms;
import com.tempos21.market.db.AppModel;
import com.tempos21.market.device.BinaryInstaller;
import com.tempos21.market.ui.adapter.ScreenshotsAdapter;
import com.tempos21.market.ui.view.RoundeCornersImage;
import com.tempos21.market.ui.view.SizeChangeNotifyingTextView;
import com.tempos21.market.ui.view.SizeChangeNotifyingTextView.OnSizeChangeListener;
import com.tempos21.market.util.AppsStateControl;
import com.tempos21.market.util.MToast;
import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import eu.alfred.api.market.MarketPlace;
import eu.alfred.api.market.responses.apps.AppDetail;
import eu.alfred.api.market.responses.listener.GetAppDetailsResponseListener;
import eu.alfred.api.market.responses.listener.InstallBinaryResponseListener;


public class AppActivity extends TActivity implements OnClickListener, OnFileLoadedListener, OnSizeChangeListener {//, OnGetInstalledAppsServiceResponseListener, OnModifyInstalledAppsServiceResponseListener {

    public static final String EXTRA_APP_ID = "appId";
    public static final String EXTRA_PACKAGE_NAME = "packageName";
    public static final String EXTRA_CAN_REVIEW = "canReview";
    public static final String EXTRA_TESTING = "appsForTesting";
    public static final String EXTRA_DOWNLOAD_APP = "download";
    private static final int COLLAPSED_LINES = 3;
    private String appId;
    private RoundeCornersImage appIcon;
    private TextView appName;
    private TextView appVersion;
    private TextView appSize;
    private TextView appPublicationDate;
    private LinearLayout appMore;

    private boolean foldedDescription = true;
    private App app;
    private SizeChangeNotifyingTextView appDescription;

    private View appProgress;
    private View mainLayout;
    private View appIconProgress;
    private View appScreenshotsLayout;
    private View appRatings;
    private View appReport;
    private View appInstall;
    private RatingBar appRating;
    private Gallery appScreenshots;

    private FileLoaderQueue loader;

    private View appInstallProgres;

    private View appUninstall;

    private View appUnableLoad;
    private TextView authorName;
    private View appSizeLayout;
    private int retry = 0;
    private int test = 0;
//	private String type;
//	private int reply = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);

        loader = new FileLoaderQueue(this, getString(R.string.app_name));
        loader.setOnFileLoadedListener(this);

        findViews();
        setListeners();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgress();
        setInitialView();
        getData();
//		showInstallButtons();
//		notifyInstall();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFileLoaded(String id, Bitmap bitmap) {
        if ("icon".equals(id)) {
            appIcon.setVisibility(View.VISIBLE);
            appIconProgress.setVisibility(View.INVISIBLE);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_image);
            }
            appIcon.setImage(bitmap);
        }
    }

    public void findViews() {
        // appIcon=(SquarePhoto) findViewById(R.id.appIcon);
        appName = (TextView) findViewById(R.id.appName);
        authorName = (TextView) findViewById(R.id.authorName);
        appDescription = (SizeChangeNotifyingTextView) findViewById(R.id.appDescription);
        appVersion = (TextView) findViewById(R.id.appVersion);
        appSize = (TextView) findViewById(R.id.appSize);
        appPublicationDate = (TextView) findViewById(R.id.appPublicationDate);
        appMore = (LinearLayout) findViewById(R.id.appMore);
        appProgress = findViewById(R.id.appProgress);
        mainLayout = findViewById(R.id.mainLayout);
        appRating = (RatingBar) findViewById(R.id.appRating);
        appIcon = (RoundeCornersImage) findViewById(R.id.appIcon);
        appIconProgress = findViewById(R.id.appIconProgress);
        appScreenshots = (Gallery) findViewById(R.id.appScreenshots);
        appScreenshotsLayout = findViewById(R.id.appScreenshotsLayout);
        appRatings = findViewById(R.id.appRatings);
        appReport = findViewById(R.id.appReport);
        appInstall = findViewById(R.id.appInstall);
        appUninstall = findViewById(R.id.appUninstall);
        appSizeLayout = findViewById(R.id.appSizeLayout);
        appInstallProgres = findViewById(R.id.appInstallProgress);
        appUnableLoad = findViewById(R.id.appUnableLoad);
    }

    public void setListeners() {
        appMore.setOnClickListener(this);
        appRatings.setOnClickListener(this);
        appDescription.setOnSizeChangedListener(this);
        appReport.setOnClickListener(this);
        appInstall.setOnClickListener(this);
        appUninstall.setOnClickListener(this);
    }

    public void showProgress() {
        appProgress.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    public void setInitialView() {
        //showProgress();
        appIcon.setVisibility(View.INVISIBLE);
        appIconProgress.setVisibility(View.VISIBLE);
        appInstall.setVisibility(View.GONE);
        appInstallProgres.setVisibility(View.GONE);
        appUninstall.setVisibility(View.GONE);
        appUnableLoad.setVisibility(View.GONE);
    }

    public void getData() {
        appId = getIntent().getExtras().getString(EXTRA_APP_ID);
        test = getIntent().getExtras().getInt(EXTRA_DOWNLOAD_APP, 0);

        if (appId != null) {

            MarketPlaceHelper.getInstance().marketPlace.getAppDetails(appId, new GetAppDetailsResponseListener() {
                @Override
                public void onSuccess(AppDetail appDetail) {
                    App app;

                    app = new App();
                    app.setId("" + (appDetail.id != null ? appDetail.id : -1));
                    app.setName(appDetail.name);
                    app.setDate(appDetail.date);
                    app.setPackageName(appDetail.packageName);
                    app.setNotificationEmails(appDetail.notificationEmails);
                    app.setExternalBinary(appDetail.externalBinary != null ? appDetail.externalBinary : false);
                    app.setVersion_number(appDetail.versionNumber != null ? appDetail.versionNumber : 1);
                    app.setAllowed(appDetail.allowed != null ? appDetail.allowed : false);
                    app.setSupportEmail(appDetail.supportEmails);
                    app.setVersion_string(appDetail.versionString);
                    app.setIcon_url(appDetail.iconUrl);
                    app.setRating(appDetail.rating != null ? appDetail.rating : 1);
                    app.setPromo_url(appDetail.promoUrl);
                    app.setAuthor(appDetail.author);
                    app.setExternalUrl(appDetail.externalUrl);
                    app.setVersion_id("" + (appDetail.versionId != null ? appDetail.versionId : 1));
                    Platforms platforms = new Platforms();
                    Platform platform;
                    for (eu.alfred.api.market.responses.apps.Platform platform1 : appDetail.platform) {
                        platform = new Platform();
                        platform.setId(platform1.id != null ? platform1.id : -1);
                        platform.setName(platform1.name);

                        com.tempos21.market.client.bean.OS os = new OS();
                        os.setExtension(platform1.os.extension);
                        os.setId(platform1.os.id != null ? platform1.os.id : -1);
                        os.setName(platform1.os.name);

                        platform.setOs(os);
                        platforms.add(platform);
                    }
                    app.setPlatform(platforms);

                    onGetAppServiceResponse(true, app);
                }

                @Override
                public void onError(Exception e) {
                    onGetAppServiceResponse(false, null);
                }
            });
            /*GetAppService appService = new GetAppService(Constants.GETAPP_SERVICE, this);
            appService.setupParameters(appId);
            if (getIntent().getExtras().getString(EXTRA_TESTING) != null) {
                appService.setupParameterVersion(getIntent().getExtras().getString(EXTRA_TESTING));
            }
            appService.setOnGetAppServiceResponseListener(this);
            appService.runService();*/
        }
    }

    public void onGetAppServiceResponse(boolean success, App app) {
        if (success) {
            this.app = app;
            setData();
            showMain();

            loader.loadFile("icon", Constants.GET_IMAGE_ICON + app.getIcon_url());

            Intent appActivity = new Intent();
            appActivity.putExtra("id", app.getId());
            appActivity.putExtra("rating", app.getRating());
            setResult(0, appActivity);

            retry = 0;
        } else {
            /*if (responseCode == ServiceErrorCodes.LOGIN_ERROR) {
                Intent intent = new Intent(this, StartUpActivity.class);
                intent.putExtra(HomeFragment.LOGIN_ERROR, true);
                startActivity(intent);
                finish();
            } else {*/
            if (retry < 3) {
                getData();
                retry++;
            } else {
                showNoLoaded();
                retry = 0;
            }
            //}
        }

        AppModel model = new AppModel(this);
        if (BinaryInstaller.isAppInstalled(this, app.getPackageName())) {
            if (!model.isOnDB(app)) {
                model.insertApp(app);
            }
        } else {
            if (model.isOnDB(app)) {
                model.deleteApp(app);
            }
        }
    }

    private void setData() {
        appName.setText(app.getName());
        authorName.setText(app.getAuthor());
        appVersion.setText(app.getVersion_string());

        if (app.isExternalBinary()) {
            appSizeLayout.setVisibility(View.GONE);
        } else {
            appSizeLayout.setVisibility(View.VISIBLE);
            appSize.setText(getFormattedSize(app.getSize()));
        }

        appPublicationDate.setText(getFormattedDate(app.getDate()));
        appDescription.setText(app.getDescription());
        appRating.setRating((float) app.getRating());

        if (app.getScreenshots().size() > 0) {
            appScreenshots.setAdapter(new ScreenshotsAdapter(this, app.getScreenshots()));
        } else {
            appScreenshotsLayout.setVisibility(View.GONE);
        }
        if (app.getNotificationEmails() == null || app.getNotificationEmails().length() < 2) {
            appReport.setVisibility(View.GONE);
        }
        showInstallButtons();
    }

    private String getFormattedSize(double mb) {
        String size = "";

        if (mb >= 1) {
            size = "" + mb;
            size = size.substring(0, 4);
            size = size.concat(" MB");
        } else {
            if (mb == 0) {
                size = "0.0 MB";
            } else {
                mb = mb * 1000;
                size = "" + mb;
                size = size.substring(0, 6);
                size = size.concat(" KB");
            }
        }

        return size;
    }

    private CharSequence getFormattedDate(String date) {
        // SimpleDateFormat sourceFormat = new
        // SimpleDateFormat("yyyy-dd-MM HH:mm:ss.S");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM dd, yyyy");
        DateFormatSymbols usSymbols = new DateFormatSymbols(Locale.ENGLISH);
        targetFormat.setDateFormatSymbols(usSymbols);
        try {
            return targetFormat.format(sourceFormat.parse(date));

        } catch (ParseException e) {
            return date;
        }
    }

    private void showInstallButtons() {
        AppsStateControl control;

        if (app != null) {
            if (BinaryInstaller.isAppInstalled(this, app.getPackageName())) {
                appUninstall.setVisibility(View.VISIBLE);
                appInstall.setVisibility(View.INVISIBLE);
                control = new AppsStateControl(this, app);
            } else {
                appUninstall.setVisibility(View.INVISIBLE);
                appInstall.setVisibility(View.VISIBLE);
                control = new AppsStateControl(this);
            }
            control.getInstalledApps();
        }
    }

    public void showMain() {
        appProgress.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        mainLayout.setAnimation(fadeIn);
    }

    private void showNoLoaded() {
        appUnableLoad.setVisibility(View.VISIBLE);
        appProgress.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        appUnableLoad.setAnimation(fadeIn);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == appMore.getId()) {
            setDescription();
            foldedDescription = !foldedDescription;
        }

        if (v.getId() == appRatings.getId()) {
            Intent i = new Intent(this, RatingsActivity.class);
            i.putExtra(RatingsActivity.EXTRA_APP_ID, app.getId());
            i.putExtra(AppActivity.EXTRA_PACKAGE_NAME, app.getPackageName());
            i.putExtra(RatingsActivity.EXTRA_CAN_REVIEW, getIntent().getExtras().getBoolean(EXTRA_CAN_REVIEW, true));
//			startActivityForResult(i,0);
            startActivity(i);
        }

        if (v.getId() == appReport.getId()) {
            sendReportMail();
        }

        if (v.getId() == appInstall.getId()) {
            appInstall.setVisibility(View.INVISIBLE);
            appInstallProgres.setVisibility(View.VISIBLE);

            if (app.isAllowed()) {
                DownloadApp down = new DownloadApp(this, MarketPlaceHelper.getInstance().marketPlace, app.getId(), app.getVersion_id(), app.getVersion_number(), app.isExternalBinary(), app.getExternalUrl());
                down.execute();
            } else {
                showMessage(this);
            }
        }

        if (v.getId() == appUninstall.getId()) {
            if (BinaryInstaller.isAppInstalled(this, app.getPackageName())) {
                BinaryInstaller.unInstallApplication(this, app.getPackageName());
            }
            showInstallButtons();
        }
    }

    public void setDescription() {
        if (foldedDescription) {
            animateDescription(appDescription.getLineCount());
        } else {
            animateDescription(COLLAPSED_LINES);
        }
    }

    private void animateDescription(int targetLines) {
        int sourceLines = COLLAPSED_LINES;
        if (targetLines == COLLAPSED_LINES) {
            sourceLines = appDescription.getLineCount();
        }
        int step = (int) Math.signum(targetLines - sourceLines);
        int sourceHeight = sourceLines * appDescription.getLineHeight();
        int targetHeight = targetLines * appDescription.getLineHeight();
        DescriptionExpander expander = new DescriptionExpander(sourceHeight, targetHeight, step);
        expander.execute();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        TLog.v("Lines of text: " + appDescription.getLineCount());
        if (appDescription.getLineCount() > COLLAPSED_LINES) {
            appMore.setVisibility(View.VISIBLE);
        } else {
            appMore.setVisibility(View.GONE);
        }
    }


    private void sendReportMail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{app.getNotificationEmails()});
        i.putExtra(Intent.EXTRA_SUBJECT, String.format(getString(R.string.report_subject), app.getName()));
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.report_body));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            MToast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMessage(final Context context) {
        final AlertDialog.Builder dialog = new Builder(this);
        dialog.setTitle(R.string.install_app_title);
        dialog.setMessage(R.string.install_app_ask);
        dialog.setPositiveButton(R.string.ok, new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                DownloadApp down = new DownloadApp(context, MarketPlaceHelper.getInstance().marketPlace, app.getId(), app.getVersion_id(), app.getVersion_number(), app.isExternalBinary(), app.getExternalUrl());
                down.execute();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                appInstall.setVisibility(View.VISIBLE);
                appInstallProgres.setVisibility(View.INVISIBLE);
            }
        });
        dialog.show();
    }

    private class DescriptionExpander extends AsyncTask<Void, Integer, Void> {

        private int currentHeight;
        private int targetHeight;
        private int step;

        public DescriptionExpander(int sourceHeight, int targetHeight, int step) {
            this.currentHeight = sourceHeight;
            this.targetHeight = targetHeight;
            this.step = step;
        }

        @Override
        protected Void doInBackground(Void... params) {
            int toWait = 0;
            int maxWait = 3;
            while (!heightAchieved()) {
                currentHeight += step;
                publishProgress(currentHeight);
                try {
                    toWait++;
                    if (toWait >= maxWait) {
                        Thread.sleep(1);
                        toWait = 0;
                    }

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }

        private boolean heightAchieved() {
            if (step > 0) {
                return currentHeight >= targetHeight;
            } else {
                return currentHeight <= targetHeight;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            appDescription.setHeight(currentHeight);
        }


    }


    private class DownloadApp {

        private final static int INSTALL_ERROR = 0;
        private final static int INSTALL_OK = 1;

        private Context context;
        private String appId;
        private int appVersion;
        private boolean external;
        private String externalUrl;
        private String versionId;
        private MarketPlace marketPlace;
        private String pathAppDownloaded;
        private boolean downloading;
        private long appIdLong;

        public DownloadApp(Context context, MarketPlace marketPlace, String appId, String versionId, int appVersion, boolean external, String externalUrl) {
            this.context = context;
            this.appId = appId;
            this.appVersion = appVersion;
            this.external = external;
            this.externalUrl = externalUrl;
            this.versionId = versionId;
            this.marketPlace = marketPlace;
            pathAppDownloaded = "";
            downloading = true;
            appIdLong = -1;
        }

        protected void execute() {
            int result = INSTALL_ERROR;
            try {
                if (external) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(externalUrl));
                    startActivity(intent);
                } else {
                    downloading = true;
                    pathAppDownloaded = "";
                    appIdLong = -1;
                    try {
                        appIdLong = Long.parseLong(appId);
                    } catch (Exception ignored) {
                    }
                    marketPlace.installBinary(appIdLong, versionId, new InstallBinaryResponseListener() {
                        @Override
                        public void onSuccess(String s) {
                            pathAppDownloaded = s;
                            downloading = false;

                            try {
                                BinaryInstaller.installApplication(context, pathAppDownloaded);
                            } catch (Exception e) {
                                e.printStackTrace();
                                MToast.makeText(context, context.getString(R.string.installError), Toast.LENGTH_LONG).show();
                            }

                            showInstallButtons();
                            appInstallProgres.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            pathAppDownloaded = null;
                            downloading = false;

                            showInstallButtons();
                            appInstallProgres.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            } catch (Exception e) {
                result = INSTALL_ERROR;
                // do nothing, app was not installed.
            }
            result = INSTALL_OK;
        }

    }

    private class DownloadApp2 extends AsyncTask<Void, Void, Integer> {

        private final static int INSTALL_ERROR = 0;
        private final static int INSTALL_OK = 1;

        private Context context;
        private String appId;
        private int appVersion;
        private boolean external;
        private String externalUrl;
        private String versionId;
        private MarketPlace marketPlace;
        private String dataStr;
        private boolean downloading;
        private long appIdLong;

        public DownloadApp2(Context context, MarketPlace marketPlace, String appId, String versionId, int appVersion, boolean external, String externalUrl) {
            this.context = context;
            this.appId = appId;
            this.appVersion = appVersion;
            this.external = external;
            this.externalUrl = externalUrl;
            this.versionId = versionId;
            this.marketPlace = marketPlace;
            dataStr = "";
            downloading = true;
            appIdLong = -1;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                if (external) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(externalUrl));
                    startActivity(intent);
                } else {
                    downloading = true;
                    dataStr = "";
                    appIdLong = -1;
                    try {
                        appIdLong = Long.parseLong(appId);
                    } catch (Exception e) {
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            marketPlace.installBinary(appIdLong, versionId, new InstallBinaryResponseListener() {
                                @Override
                                public void onSuccess(String s) {
                                    dataStr = s;
                                    downloading = false;
                                }

                                @Override
                                public void onError(Exception e) {
                                    dataStr = null;
                                    downloading = false;
                                }
                            });
                            Looper.loop();
                        }
                    }).start();
                    while (downloading) {
                    }
                    if (TextUtils.isEmpty(dataStr)) {
                        throw new RuntimeException("Error installing the application.");
                    }
                    BinaryInstaller.installApplication(context, appId, versionId, appVersion, test, dataStr);
                }
            } catch (Exception e) {
                return INSTALL_ERROR;
                // do nothing, app was not installed.
            }
            return INSTALL_OK;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == INSTALL_ERROR) {
                MToast.makeText(context, context.getString(R.string.installError), Toast.LENGTH_LONG).show();
            }
            showInstallButtons();
            appInstallProgres.setVisibility(View.INVISIBLE);
        }

    }


//	private void notifyInstall() {
//	AsyncNotifyInstall notifyInstall;
//	int versionNumber;
//	
//	if (app == null) {
//		return;
//	}
//	
//	if (BinaryInstaller.isAppInstalled(this, app.getPackageName())) {
//		GetInstalledAppsService searchAppsService = new GetInstalledAppsService(Constants.GET_INSTALLED_APP_SERVICE, this);
//		searchAppsService.setOnGetInstalledAppsServiceResponseListener(this);
//		GetAppsInput input = new GetAppsInput();
//		input.setSorting(GetAppsInput.SORTING_APPS_DATE);
//		input.setStart(0);
//		input.setElements(-1);
//		searchAppsService.setupParameters(input);
//		searchAppsService.runService();
//	}else{
//		type = "D";
//		versionNumber = app.getVersion_number();
//		
//		notifyInstall = new AsyncNotifyInstall(this, app.getId(), type, versionNumber);
//		notifyInstall.execute();
//	}
//}
//
//
//@Override
//public void onGetInstalledAppsServiceResponse(int responseCode, Apps apps) {
//	AsyncNotifyInstall notifyInstall;
//	int versionNumber;
//	boolean find = false;
//	versionNumber = BinaryInstaller.getVersion(this, app.getPackageName());
//	
//	if(apps != null){
//		
//		for(App myApp : apps){
//			if(myApp.getId().equals(app.getId())){
//				find = true;
//			}
//		}
//		
//		if(!find){
//			type = "I";
//			notifyInstall = new AsyncNotifyInstall(this, app.getId(), type, versionNumber);
//			notifyInstall.execute();
//		}
//	}
//}
//
//
//private class AsyncNotifyInstall extends AsyncTask<Void, Void, Void> {
//	
//	Transaction transaction;
//	private Context context;
//
//	public AsyncNotifyInstall(Context context, String id, String type, int version) {
//		this.transaction = new Transaction(id, type, version);
//		this.context = context;
//	}
//
//	@Override
//	protected Void doInBackground(Void... params) {
//		Transactions change = new Transactions();
//		change.add(transaction);
//		TransactionsInput input = new TransactionsInput();
//		input.setTransactions(change);
//
//		TransactionsService service = new TransactionsService(Constants.TRANSACTIONS_SERVICE, context);
//		service.setOnModifyInstalledAppsServiceResponseListener(AppActivity.this);
//		service.setupParameters(input);
//		service.runService();
//		return null;
//	}
//}
//
//
//@Override
//public void onModifyInstalledAppsServiceResponse(int responseCode, ReportsUpdates reports) {
//	if(!(responseCode == ServiceErrorCodes.OK && reports.get(0).getResult().equals("ok")) && reply  < 3){
//		AsyncNotifyInstall notifyInstall = new AsyncNotifyInstall(this, app.getId(), type, BinaryInstaller.getVersion(this, app.getPackageName()));
//		notifyInstall.execute();
//		reply++;
//	}else{
//		reply = 0;
//	}
//}


//	public String getAppId() {
//		return appId;
//	}
//
//	
//	public void setAppId(String appId) {
//		this.appId = appId;
//	}


//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		getData();
//	}

}
