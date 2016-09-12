package com.tempos21.rampload.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.tempos21.mymarket.sdk.MyMarketPreferences;
import com.tempos21.rampload.RampLoad;
import com.tempos21.rampload.exception.RampLoadConnectionException;
import com.tempos21.rampload.exception.RampLoadInitServiceException;
import com.tempos21.rampload.exception.RampLoadInitValuesException;
import com.tempos21.rampload.manager.RampLoadDownloadManager;
import com.tempos21.rampload.model.RampLoadDownload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * RampLoadService class is a queue of tasks (see {@link IntentService}), which downloads
 * and saves a file to a location.
 * The progress and the result of the tast is notified via {@link LocalBroadcastManager}
 * and {@link RampLoad} registers and unregisters to it when needed to notify via {@link RampLoad.DownloadListener} and {@link RampLoad.StatusListener}.
 * For an easy usage see {@link RampLoad}.
 */
public class RampLoadService extends IntentService {

  /**
   * The field cookie used for the connection
   */
  private static final String FIELD_COOKIE = "Cookie";

  /**
   * The EXTRA_... constants are used to notify extra values via {@link LocalBroadcastManager}.
   */
  public static final String EXTRA_ID = "EXTRA_ID";
  public static final String EXTRA_PROGRESS = "EXTRA_PROGRESS";
  public static final String EXTRA_FILE_NAME = "EXTRA_FILE_NAME";
  public static final String EXTRA_FILE_URL = "EXTRA_FILE_URL";
  public static final String EXTRA_FILE_PATH = "EXTRA_FILE_PATH";
  public static final String EXTRA_ERROR = "EXTRA_ERROR";

  /**
   * The ACTION_... constants are used to notify the action of the intent via {@link LocalBroadcastManager}.
   */
  public static final String ACTION_START = "com.tempos21.rampload.action.ACTION_START";
  public static final String ACTION_PROGRESS = "com.tempos21.rampload.action.ACTION_PROGRESS";
  public static final String ACTION_FINISH = "com.tempos21.rampload.action.ACTION_FINISH";
  public static final String ACTION_FAILED = "com.tempos21.rampload.action.ACTION_FAILED";
  public static final String ACTION_IDLE = "com.tempos21.rampload.action.ACTION_IDLE";
  public static final String ACTION_DOWNLOADING = "com.tempos21.rampload.action.ACTION_DOWNLOADING";

  /**
   * The step to notify, set to DEFAULT_PROGRESS_STEP. 1 means 100%.
   * see {@link #notifyProgress(int, float, String, String)}
   */
  private static final float DEFAULT_PROGRESS_STEP = 0.009f;

  /**
   * Contains all the file resources created in a single execution.
   * If the procedure fails, this {@Link List} will be used to clear and restore the previous state.
   * <p/>
   * To clarify, if the directory /fruit/ exists and
   * the directories /fruit/apple/ and /fruit/pear/ are created (and stored in filesCreated),
   * if an error occurs, filesCreated will contain /fruit/apple/ and /fruit/pear/,
   * those two elements will be deleted but not /fruit/.
   */
  private final List<String> filesCreated = new ArrayList<>();

  /**
   * The general state of execution. If the state is IDLE it means it is not executing anything,
   * but if it is DOWNLOADING it will execute until no elements are found to process.
   */
  private static RampLoad.RampLoadStatus state;

  /**
   * Used to know how many RampLoadService are queued like an {@link IntentService} and running to.
   */
  private static int numIntentServicesRunning = 0;

  public RampLoadService() {
    super("RampLoadService");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    numIntentServicesRunning++;
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    init();
    start();
    numIntentServicesRunning--;
  }

  /**
   * Initializes the values needed for each RampLoadService execution.
   */
  private void init() {
    RampLoadDownloadManager.init(getApplicationContext());
    filesCreated.clear();
  }

  /**
   * Starts the procedure of downloading and saving files.
   * Puts an element from {@link RampLoadDownloadManager.RampLoadQueueType#QUEUE_IDLE} to {@link RampLoadDownloadManager.RampLoadQueueType#QUEUE_DOWNLOADING} in {@link RampLoadDownloadManager}.
   * Then it downloads using the {@link RampLoadDownload#url} and saves the data into a file using the {@link RampLoadDownload#path}.
   * It notifies the progress of the procedure via {@link LocalBroadcastManager}.
   */
  private void start() {
    RampLoadDownload rampLoadDownload = RampLoadDownloadManager.getInstance().moveOneIdleToDownloading();
    if (rampLoadDownload != null && !TextUtils.isEmpty(rampLoadDownload.name) && !TextUtils.isEmpty(rampLoadDownload.url) && !TextUtils.isEmpty(rampLoadDownload.path)) {
      notifyDownloading();
      notifyStart(rampLoadDownload.id, rampLoadDownload.name, rampLoadDownload.url, rampLoadDownload.path);
      try {
        downloadAndSaveFile(rampLoadDownload);
        RampLoadDownloadManager.getInstance().removeDownloadingById(rampLoadDownload.id);
        notifyFinish(rampLoadDownload.id, rampLoadDownload.name, rampLoadDownload.url, rampLoadDownload.path);
      } catch (Exception exception) {
        notifyProgress(rampLoadDownload.id, 1, rampLoadDownload.name, rampLoadDownload.url, rampLoadDownload.path);
        deleteFoldersAsc(filesCreated);
        RampLoadDownloadManager.getInstance().removeDownloadingById(rampLoadDownload.id);
        notifyFailed(rampLoadDownload.id, rampLoadDownload.name, rampLoadDownload.url, rampLoadDownload.path, exception);
      }
      try {
        startDownloadService(getApplicationContext());
      } catch (RampLoadInitServiceException e) {
        notifyIdle();
      }
    } else if (rampLoadDownload != null) {
      notifyDownloading();
      notifyStart(rampLoadDownload.id, rampLoadDownload.name, rampLoadDownload.url, rampLoadDownload.path);
      notifyProgress(rampLoadDownload.id, 1, rampLoadDownload.name, rampLoadDownload.url, rampLoadDownload.path);
      RampLoadDownloadManager.getInstance().removeDownloadingById(rampLoadDownload.id);
      notifyFailed(rampLoadDownload.id, rampLoadDownload.name, rampLoadDownload.url, rampLoadDownload.path, new NullPointerException("RampLoadRequest is null or has null attributes."));
      try {
        startDownloadService(getApplicationContext());
      } catch (RampLoadInitServiceException e) {
        notifyIdle();
      }
    } else {
      notifyIdle();
    }
  }

  /**
   * Downloads the file from the {@link RampLoadDownload#url} value
   * and saves it to the target {@link RampLoadDownload#path}.
   * If any error occurs, it will throw an Exception.
   *
   * @param rampLoadDownload The Request containing the input values.
   * @throws Exception An Exception containing information of the error.
   */
  private void downloadAndSaveFile(RampLoadDownload rampLoadDownload) throws Exception {
    if (!isConnected()) {
      throw new RampLoadConnectionException("WIFI or MOBILE connection is disabled.");
    }
    if (TextUtils.isEmpty(rampLoadDownload.url)) {
      throw new RampLoadInitValuesException("The url is empty or null.");
    } else if (TextUtils.isEmpty(rampLoadDownload.path)) {
      throw new RampLoadInitValuesException("The path is empty or null.");
    }
    String dir = rampLoadDownload.path.substring(0, rampLoadDownload.path.lastIndexOf("/")) + "/";
    String filename = rampLoadDownload.path.substring(rampLoadDownload.path.lastIndexOf("/") + 1);
    List<String> pathsCreated = createDirectories(dir);
    File file = new File(dir, filename);
    pathsCreated.add(0, file.getAbsolutePath());
    filesCreated.addAll(0, pathsCreated);
    FileOutputStream fos = null;
    InputStream is = null;
    BufferedInputStream bis = null;
    try {
      fos = new FileOutputStream(file);
      URL u = new URL(rampLoadDownload.url);
      URLConnection urlConnection = u.openConnection();
      String cookie = MyMarketPreferences.getInstance(getApplicationContext()).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_SESSION_ID, null);
      if (cookie != null) {
        urlConnection.addRequestProperty(FIELD_COOKIE, cookie);
      }
      is = urlConnection.getInputStream();
      bis = new BufferedInputStream(is);
      float currentSize = 0;
      int totalSize = urlConnection.getContentLength();
      float progress = 0;
      notifyProgress(rampLoadDownload.id, progress, rampLoadDownload.name, rampLoadDownload.url, rampLoadDownload.path);
      int currentByte;
      while ((currentByte = bis.read()) != -1) {
        fos.write((byte) currentByte);
        currentSize++;
        if ((currentSize / totalSize) - progress >= DEFAULT_PROGRESS_STEP) {
          progress = currentSize / totalSize;
          notifyProgress(rampLoadDownload.id, progress, rampLoadDownload.name, rampLoadDownload.url, rampLoadDownload.path);
        }
      }
      progress = 1;
      notifyProgress(rampLoadDownload.id, progress, rampLoadDownload.name, rampLoadDownload.url, rampLoadDownload.path);
    } catch (IOException exception) {
      exception.printStackTrace();
      throw exception;
    } finally {
      // ensure disposal
      try {
        if (fos != null) {
          fos.close();
        }
      } catch (Exception exception) {
        // No problem
      }
      try {
        if (is != null) {
          is.close();
        }
      } catch (Exception exception) {
        // No problem
      }
      try {
        if (bis != null) {
          bis.close();
        }
      } catch (Exception exception) {
        // No problem
      }
    }
  }

  /**
   * Creates the directories needed to access to directoryPath.
   *
   * @param directoryPath The target path.
   * @return Returns the list of paths created into the file system.
   */
  private List<String> createDirectories(String directoryPath) {
    List<String> pathsFoldersCreated = new ArrayList<>();
    File parentFileExists = new File(directoryPath);
    File file = new File(directoryPath);
    String lastParent;
    while (!parentFileExists.exists()) {    // store the paths that are not created, and go up until a parent exists
      pathsFoldersCreated.add(parentFileExists.getAbsolutePath());
      lastParent = parentFileExists.getAbsolutePath();
      if (parentFileExists.getParentFile().getAbsolutePath().equals(lastParent)) {
        parentFileExists = parentFileExists.getParentFile().getParentFile();
      } else {
        parentFileExists = parentFileExists.getParentFile();
      }
    }
    file.mkdirs();
    return pathsFoldersCreated;
  }

  /**
   * Deletes all the paths in order from 0 to pathsFoldersCreated.size()-1 from pathsFoldersCreated.
   * The parameter pathsFoldersCreated will be modified, each element removed from pathsFoldersCreated,
   * represents that the path is removed from the file system.
   *
   * @param pathsFoldersCreated The paths to be deleted, it will be modified accoding to the removal.
   * @return Returns true if all the paths are removed from the filesytem, false otherwise.
   */
  private boolean deleteFoldersAsc(List<String> pathsFoldersCreated) {
    if (pathsFoldersCreated != null) {
      String path;
      File tmpFile;
      for (int i = 0; i < pathsFoldersCreated.size(); i++) {
        path = pathsFoldersCreated.get(i);
        tmpFile = new File(path);
        if (tmpFile.exists()) {
          tmpFile.delete();
          pathsFoldersCreated.remove(i);
          i--;
        }
      }
      return pathsFoldersCreated.isEmpty();
    } else {
      return false;
    }
  }

  /**
   * Notifies about the start of a single task.
   *
   * @param id   The unique id of the input {@link RampLoadDownload}.
   * @param url  The url of the input {@link RampLoadDownload}.
   * @param path The path of the input {@link RampLoadDownload}.
   */
  private void notifyStart(int id, String name, String url, String path) {
    Intent intent = new Intent(ACTION_START);
    intent.putExtra(EXTRA_ID, id);
    intent.putExtra(EXTRA_FILE_NAME, name);
    intent.putExtra(EXTRA_FILE_URL, url);
    intent.putExtra(EXTRA_FILE_PATH, path);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }

  /**
   * Notifies about the progress of a single task.
   *
   * @param id       The unique id of the input {@link RampLoadDownload}.
   * @param progress The progress between [0,1] representing the total progress of the task.
   * @param url      The url of the input {@link RampLoadDownload}.
   * @param path     The path of the input {@link RampLoadDownload}.
   */
  private void notifyProgress(int id, float progress, String name, String url, String path) {
    Intent intent = new Intent(ACTION_PROGRESS);
    intent.putExtra(EXTRA_ID, id);
    intent.putExtra(EXTRA_PROGRESS, progress);
    intent.putExtra(EXTRA_FILE_NAME, name);
    intent.putExtra(EXTRA_FILE_URL, url);
    intent.putExtra(EXTRA_FILE_PATH, path);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }

  /**
   * Notifies about the end of a single task.
   *
   * @param id   The unique id of the input {@link RampLoadDownload}.
   * @param url  The url of the input {@link RampLoadDownload}.
   * @param path The path of the input {@link RampLoadDownload}.
   */
  private void notifyFinish(int id, String name, String url, String path) {
    Intent intent = new Intent(ACTION_FINISH);
    intent.putExtra(EXTRA_ID, id);
    intent.putExtra(EXTRA_FILE_NAME, name);
    intent.putExtra(EXTRA_FILE_URL, url);
    intent.putExtra(EXTRA_FILE_PATH, path);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }

  /**
   * Notifies about the failure of a single task.
   *
   * @param id        The unique id of the input {@link RampLoadDownload}.
   * @param url       The url of the input {@link RampLoadDownload}.
   * @param path      The path of the input {@link RampLoadDownload}.
   * @param exception See {@link com.tempos21.rampload.exception.RampLoadException} and {@link IOException}
   */
  private void notifyFailed(int id, String name, String url, String path, Exception exception) {
    Intent intent = new Intent(ACTION_FAILED);
    intent.putExtra(EXTRA_ID, id);
    intent.putExtra(EXTRA_FILE_NAME, name);
    intent.putExtra(EXTRA_FILE_URL, url);
    intent.putExtra(EXTRA_FILE_PATH, path);
    intent.putExtra(EXTRA_ERROR, exception);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }

  /**
   * Notifies about the start of the procedure of all the tasks.
   */
  private void notifyDownloading() {
    if (state != RampLoad.RampLoadStatus.DOWNLOADING) {
      state = RampLoad.RampLoadStatus.DOWNLOADING;
      Intent intent = new Intent(ACTION_DOWNLOADING);
      LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
  }

  /**
   * Notifies about the end of the procedure of all the tasks.
   */
  private void notifyIdle() {
    if (state != RampLoad.RampLoadStatus.IDLE) {
      state = RampLoad.RampLoadStatus.IDLE;
      Intent intent = new Intent(ACTION_IDLE);
      LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
  }

  /**
   * Checks if there is any internet connection.
   *
   * @return
   */
  private boolean isConnected() {
    ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    return (conMgr.getActiveNetworkInfo() != null);
  }

  /**
   * Starts the {@link RampLoadService}, if the number of queued {@link RampLoadService} plus the running is more than 2,
   * it won't call a new {@link RampLoadService} because the existing ones will start another if needed.
   *
   * @param context
   * @throws RampLoadInitServiceException Throws an Exception f the service can't be started.
   */
  public static void startDownloadService(Context context) throws RampLoadInitServiceException {
    if (numIntentServicesRunning <= 2) {  // ensure at least 1 is running
      Intent intent = new Intent(context, RampLoadService.class);
      if (context.startService(intent) == null) {
        throw new RampLoadInitServiceException("RampLoadService can't be started.");
      }
    }
  }
}