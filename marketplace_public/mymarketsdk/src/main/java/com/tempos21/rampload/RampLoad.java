package com.tempos21.rampload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.tempos21.mymarket.sdk.manager.DownloadNotificationManager;
import com.tempos21.rampload.exception.RampLoadInitException;
import com.tempos21.rampload.exception.RampLoadInitServiceException;
import com.tempos21.rampload.exception.RampLoadInitValuesException;
import com.tempos21.rampload.manager.RampLoadDownloadManager;
import com.tempos21.rampload.model.RampLoadDownload;
import com.tempos21.rampload.service.RampLoadService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Singleton facade class used to simplify the management of a download queue.
 * It uses {@link RampLoadService} to process {@link RampLoadDownload}.
 * The {@link RampLoadService} notifies back the progress and status via {@link LocalBroadcastManager},
 * and notifies the {@link DownloadListener} and
 * {@link StatusListener} if there are any added.
 * All the {@link RampLoadDownload} passed by {@link RampLoad#download(RampLoadDownload)} are
 * stored by {@link RampLoadDownloadManager} in the queue:
 * {@link RampLoadDownloadManager.RampLoadQueueType#QUEUE_IDLE}.
 * When the {@link RampLoadService} starts processing this elements, the current element processing
 * will be put in {@link RampLoadDownloadManager.RampLoadQueueType#QUEUE_DOWNLOADING}.
 * If the application is killed, the next time {@link RampLoadDownloadManager} is initialized
 * via {@link RampLoadDownloadManager#init(Context)} the downloads pending that are in
 * {@link RampLoadDownloadManager.RampLoadQueueType#QUEUE_DOWNLOADING}
 * will be moved into
 * {@link RampLoadDownloadManager.RampLoadQueueType#QUEUE_IDLE}.
 * <br><br>
 * USAGE:
 * <br><br>
 * To use this class call:<br>
 * - {@link RampLoad#init(Context)}
 * and then:<br>
 * - {@link RampLoad#getInstance()} with {@link RampLoad#download(RampLoadDownload)}.
 * <br><br>
 * The progress can be notified via multiple listeners:<br>
 * - {@link RampLoad#addDownloadListener(DownloadListener)}<br>
 * - {@link RampLoad#addStatusListener(StatusListener)}<br>
 * To remove a single listener:<br>
 * - {@link RampLoad#removeDownloadListener(DownloadListener)}<br>
 * - {@link RampLoad#removeStatusListener(StatusListener)}
 * <br><br>
 * There are some additional methods to provide more information:
 * - {@link RampLoad#getStatus()}
 * - {@link RampLoad#getIdle()}
 * - {@link RampLoad#getDownloading()}
 * - {@link RampLoad#clearIdle()}
 */
public class RampLoad {

  /**
   * Represents the different status of {@link RampLoad} and {@link RampLoadService}.
   */
  public enum RampLoadStatus {
    IDLE,
    DOWNLOADING
  }

  private static RampLoad instance;
  private final Context context;
  private final Random random;
  private final List<DownloadListener> downloadListeners = new ArrayList<>();
  private final List<StatusListener> statusListeners = new ArrayList<>();
  private RampLoadStatus status = RampLoadStatus.IDLE;
  private boolean registeredBroadcastReceiver = false;

  /**
   * Initializes the Rampload.
   *
   * @param context
   * @return Returns true if it is initialized with this call, false if it was initialized before.
   */
  public static boolean init(Context context) {
    if (instance == null) {
      instance = new RampLoad(context);
      return true;
    }
    return false;
  }

  /**
   * Returns the instance of {@link RampLoad}.
   *
   * @return Returns the instance of {@link RampLoad}.
   */
  public static RampLoad getInstance() {
    if (instance == null) {
      throw new RuntimeException("RampLoad must be initialized");
    }
    return instance;
  }

  protected RampLoad(Context context) {
    this.context = context.getApplicationContext();
    this.random = new Random(System.currentTimeMillis());
    DownloadNotificationManager.init(context);
    RampLoadDownloadManager.init(this.context);
    registerReceiver();
  }

  /**
   * Adds an element to the queue of downloads to process and starts {@link RampLoadService} if necessary.
   * Call it multiple times to add multiple download requests, the {@link RampLoadService} will be processing them asynchronously.
   *
   * @param download The request of a donwload.
   * @return Returns a unique id identifying the request.
   * @throws RampLoadInitException Throws a {@link RampLoadInitException} if the
   *                               {@link RampLoadDownload} parameter is null or has null or empty attributes:
   *                               - {@link RampLoadDownload#path}
   *                               - {@link RampLoadDownload#url}
   */
  public int download(RampLoadDownload download) throws RampLoadInitException {
    try {
      boolean alreadyIntoIdleList = false;
      for (RampLoadDownload idle : getIdle()) {
        if (idle.id == download.id) {
          alreadyIntoIdleList = true;
          break;
        }
      }
      if (!alreadyIntoIdleList) {
        addIdle(download);
      }
      RampLoadService.startDownloadService(context);
      return download.id;
    } catch (RampLoadInitValuesException e) {
      // The download parameter has wrong values
      throw e;
    } catch (RampLoadInitServiceException e) {
      // The service can't be started
      throw e;
    } catch (RampLoadInitException e) {
      // The service can't be started
      throw e;
    }
  }

  /**
   * Adds a listener to the {@link RampLoad#downloadListeners} to get notified by events of this class.
   * To remove it call {@link RampLoad#removeDownloadListener(DownloadListener)} passing the parameter instance.
   *
   * @param listener The listener to be added.
   * @return Returns true if the listener is added correctly.
   */
  public boolean addDownloadListener(DownloadListener listener) {
    synchronized (downloadListeners) {
      return listener != null && !downloadListeners.contains(listener) && downloadListeners.add(listener);
    }
  }

  /**
   * Removes a listener from {@link RampLoad#downloadListeners}.
   * To add one call {@link RampLoad#addDownloadListener(DownloadListener)}.
   *
   * @param listener The listener to be removed.
   * @return Returns true if the listener is removed correctly.
   */
  public boolean removeDownloadListener(DownloadListener listener) {
    synchronized (downloadListeners) {
      return listener != null && downloadListeners.contains(listener) && downloadListeners.remove(listener);
    }
  }

  /**
   * Adds a listener to the {@link RampLoad#statusListeners} to get notified by events of this class.
   * To remove it call {@link RampLoad#removeStatusListener(StatusListener)} passing the instance.
   *
   * @param listener The listener to be added.
   * @return Returns true if the listener is added correctly.
   */
  public boolean addStatusListener(StatusListener listener) {
    synchronized (statusListeners) {
      return listener != null && !statusListeners.contains(listener) && statusListeners.add(listener);
    }
  }

  /**
   * Removes a listener from {@link RampLoad#statusListeners}.
   * To add one call {@link RampLoad#addStatusListener(StatusListener)}.
   *
   * @param listener The listener to be removed.
   * @return Returns true if the listener is removed correctly.
   */
  public boolean removeStatusListener(StatusListener listener) {
    synchronized (statusListeners) {
      return listener != null && statusListeners.contains(listener) && statusListeners.remove(listener);
    }
  }

  /**
   * Returns the status of the download queue:<br>
   * - {@link RampLoadStatus#IDLE} means nothing downloading and there is any {@link RampLoadService} queued or running.<br>
   * - {@link RampLoadStatus#DOWNLOADING} means that there is at least one {@link RampLoadService} queued or downloading.<br>
   *
   * @return Returns the status of the procedure. See {@link RampLoadStatus} and {@link RampLoadService}.
   */
  public RampLoadStatus getStatus() {
    synchronized (statusListeners) {
      return status;
    }
  }

  /**
   * Returns all the {@link RampLoadDownload} that are in the {@link RampLoadDownloadManager.RampLoadQueueType#QUEUE_IDLE} queue.
   * Do not rely on this values because this list will change. See {@link RampLoadDownloadManager}.
   *
   * @return Returns a {@link List} containing all the queued items to process.
   */
  public List<RampLoadDownload> getIdle() {
    return RampLoadDownloadManager.getInstance().getList(RampLoadDownloadManager.RampLoadQueueType.QUEUE_IDLE);
  }

  /**
   * Returns all the {@link RampLoadDownload} that are in the {@link RampLoadDownloadManager.RampLoadQueueType#QUEUE_DOWNLOADING} queue.
   * Do not rely on this values because this list will change. See {@link RampLoadDownloadManager}.
   *
   * @return Returns a {@link List} containing all the current items that are being processed.
   */
  public List<RampLoadDownload> getDownloading() {
    return RampLoadDownloadManager.getInstance().getList(RampLoadDownloadManager.RampLoadQueueType.QUEUE_DOWNLOADING);
  }

  /**
   * Clears the {@link RampLoadDownloadManager.RampLoadQueueType#QUEUE_IDLE}.
   * Note that calling {@link RampLoad#getIdle()} and then {@link RampLoad#clearIdle()}
   * can refer to different lists since the {@link RampLoadService} can be executing and put one idle
   * to downloading.
   */
  public void clearIdle() {
    RampLoadDownloadManager.getInstance().clear(RampLoadDownloadManager.RampLoadQueueType.QUEUE_IDLE);
  }

  /**
   * Checks rampLoadDownload parameter and adds an item to the {@link RampLoadDownloadManager.RampLoadQueueType#QUEUE_IDLE} queue.
   *
   * @param rampLoadDownload The item to be added in the {@link RampLoadDownloadManager.RampLoadQueueType#QUEUE_IDLE} queue.
   * @return Returns the unique id of the rampLoadDownload request.
   * @throws RampLoadInitException Throws a RampLoadInitException if the rampLoadDownload is null or has null or empty attributes or the rampLoadDownload can't be saved in the {@link RampLoadDownloadManager.RampLoadQueueType#QUEUE_IDLE} queue.
   */
  private int addIdle(RampLoadDownload rampLoadDownload) throws RampLoadInitException {
    if (rampLoadDownload != null && !TextUtils.isEmpty(rampLoadDownload.name) && !TextUtils.isEmpty(rampLoadDownload.url) && !TextUtils.isEmpty(rampLoadDownload.path)) {
      // registerReceiver();
      boolean added = RampLoadDownloadManager.getInstance().addIdle(rampLoadDownload);
      if (!added) {
        throw new RampLoadInitException("Can't save rampLoadDownload: " + rampLoadDownload + ".");
      }
      return rampLoadDownload.id; // id;
    }
    throw new RampLoadInitValuesException("rampLoadDownload parameter values are null or empty.");
  }

  public void removeDownloadingById(int id) {
    RampLoadDownloadManager.getInstance().removeDownloadingById(id);
  }

  public void removeIdleById(int id) {
    RampLoadDownloadManager.getInstance().removeIdleById(id);
  }

  /**
   * Registers a {@link BroadcastReceiver} to the LocalBroadcastManager.
   * This method and {@link #unRegisterReceiver()} are called automatically to open and dispose resources.
   */
  private synchronized void registerReceiver() {
    if (!registeredBroadcastReceiver) {
      registeredBroadcastReceiver = true;
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction(RampLoadService.ACTION_START);
      intentFilter.addAction(RampLoadService.ACTION_PROGRESS);
      intentFilter.addAction(RampLoadService.ACTION_FINISH);
      intentFilter.addAction(RampLoadService.ACTION_FAILED);
      intentFilter.addAction(RampLoadService.ACTION_IDLE);
      intentFilter.addAction(RampLoadService.ACTION_DOWNLOADING);
      LocalBroadcastManager.getInstance(this.context).registerReceiver(downloadNotifyReceiver, intentFilter);

    }
  }

  /**
   * Unregisters a {@link BroadcastReceiver} to the LocalBroadcastManager.
   * This method and {@link #registerReceiver()} are called automatically to open and dispose resources.
   */
  private synchronized void unRegisterReceiver() {
    if (registeredBroadcastReceiver) {
      registeredBroadcastReceiver = false;
      LocalBroadcastManager.getInstance(this.context).unregisterReceiver(downloadNotifyReceiver);
    }
  }

  /**
   * Implementation of the {@link BroadcastReceiver} which will receiver events from the
   * {@link RampLoadService}.<br><br>
   * See:<br>
   * {@link RampLoadService#ACTION_START}<br>
   * {@link RampLoadService#ACTION_PROGRESS}<br>
   * {@link RampLoadService#ACTION_FINISH}<br>
   * {@link RampLoadService#ACTION_FAILED}<br>
   * {@link RampLoadService#ACTION_IDLE}<br>
   * {@link RampLoadService#ACTION_DOWNLOADING}<br><br>
   * {@link RampLoadService#EXTRA_ID}<br>
   * {@link RampLoadService#EXTRA_PROGRESS}<br>
   * {@link RampLoadService#EXTRA_FILE_URL}<br>
   * {@link RampLoadService#EXTRA_FILE_PATH}<br>
   * {@link RampLoadService#EXTRA_ERROR}
   */
  private BroadcastReceiver downloadNotifyReceiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      Bundle extras = intent.getExtras();
      if (extras != null) {
        int id = extras.getInt(RampLoadService.EXTRA_ID);
        String name = extras.getString(RampLoadService.EXTRA_FILE_NAME);
        String url = extras.getString(RampLoadService.EXTRA_FILE_URL);
        String path = extras.getString(RampLoadService.EXTRA_FILE_PATH);
        if (TextUtils.equals(action, RampLoadService.ACTION_START)) {
          synchronized (downloadListeners) {
            if (downloadListeners.size() == 0) {
              DownloadNotificationManager.getInstance().addDownloading(id, new RampLoadDownload(id, name, url, path));
            }
            for (DownloadListener listener : downloadListeners) {
              if (listener != null) {
                listener.onDownloadStart(id, url, path);
              }
            }
          }
        } else if (TextUtils.equals(action, RampLoadService.ACTION_PROGRESS)) {
          float progress = extras.getFloat(RampLoadService.EXTRA_PROGRESS);
          synchronized (downloadListeners) {
            final int p = (int) (progress * 100);
            if (downloadListeners.size() == 0) {
              new Thread(new Runnable() {

                @Override
                public void run() {
                  DownloadNotificationManager.getInstance().notifyToManager(DownloadNotificationManager.NOTIFICATION_DOWNLOADING, 100, p, false);
                }
              }).start();
            }
            for (DownloadListener listener : downloadListeners) {
              if (listener != null) {
                listener.onDownloadProgress(id, url, path, progress);
              }
            }
          }
        } else if (TextUtils.equals(action, RampLoadService.ACTION_FINISH)) {
          synchronized (downloadListeners) {
            if (downloadListeners.size() == 0) {
              if (!DownloadNotificationManager.getInstance().isAlreadyDownloaded(id)) {
                DownloadNotificationManager.getInstance().addDownloaded(id, new RampLoadDownload(id, name, url, path));
              }
            }
            for (DownloadListener listener : downloadListeners) {
              if (listener != null) {
                listener.onDownloadFinish(id, url, path);
              }
            }
          }
        } else if (TextUtils.equals(action, RampLoadService.ACTION_FAILED)) {
          Exception exception = null;
          try {
            exception = (Exception) extras.getSerializable(RampLoadService.EXTRA_ERROR);
          } catch (Exception e) {
            e.printStackTrace();
          }
          synchronized (downloadListeners) {
            if (downloadListeners.size() == 0) {
              DownloadNotificationManager.getInstance().removeDownloading(id);
            }
            for (DownloadListener listener : downloadListeners) {
              if (listener != null) {
                listener.onDownloadFailed(id, url, path, exception);
              }
            }
          }
        }
      }
      if (TextUtils.equals(action, RampLoadService.ACTION_IDLE)) {
        synchronized (statusListeners) {
          status = RampLoadStatus.IDLE;
          for (StatusListener listener : statusListeners) {
            if (listener != null) {
              listener.onStatusIdle();
            }
          }
        }
        // unRegisterReceiver();
      } else if (TextUtils.equals(action, RampLoadService.ACTION_DOWNLOADING)) {
        synchronized (statusListeners) {
          status = RampLoadStatus.DOWNLOADING;
          for (StatusListener listener : statusListeners) {
            if (listener != null) {
              listener.onStatusDownloading();
            }
          }
        }
      }
    }
  };

  /**
   * Interface used to notify the state of a single task. See {@link RampLoadService}.
   */
  public interface DownloadListener {

    void onDownloadStart(int id, String url, String path);

    void onDownloadProgress(int id, String url, String path, float progress);

    void onDownloadFinish(int id, String url, String path);

    void onDownloadFailed(int id, String url, String path, Exception exception);
  }

  /**
   * Interface used to notify an Idle and Downloading state. See {@link RampLoadService}.
   */
  public interface StatusListener {

    void onStatusIdle();

    void onStatusDownloading();
  }
}