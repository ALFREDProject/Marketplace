package com.tempos21.rampload.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.tempos21.rampload.model.RampLoadDownload;
import com.tempos21.rampload.util.Prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the idle and downloading requests.
 * It has 2 queues:
 * - {@link RampLoadQueueType#QUEUE_IDLE}
 * - {@link RampLoadQueueType#QUEUE_DOWNLOADING}
 */
public class RampLoadDownloadManager {

  /**
   * Used to define the types of queues.
   */
  public enum RampLoadQueueType {
    QUEUE_IDLE("QUEUE_IDLE"),
    QUEUE_DOWNLOADING("QUEUE_DOWNLOADING");

    public final String value;

    RampLoadQueueType(String value) {
      this.value = value;
    }
  }

  private static RampLoadDownloadManager instance;
  private final Context context;
  private final Gson gson;

  public static boolean init(Context context) {
    if (instance == null) {
      instance = new RampLoadDownloadManager(context);
      return true;
    }
    return false;
  }

  public static RampLoadDownloadManager getInstance() {
    return instance;
  }

  /**
   * Adds the rampLoadDownload parameter to the {@link RampLoadQueueType#QUEUE_IDLE} queue.
   *
   * @param rampLoadDownload The parameter to be added.
   * @return Returns true if the rampLoadDownload parameter is added to the queue, false otherwise.
   */
  public synchronized boolean addIdle(RampLoadDownload rampLoadDownload) {
    return add(RampLoadQueueType.QUEUE_IDLE, rampLoadDownload);
  }

  /**
   * Performs the pop operation to {@link RampLoadQueueType#QUEUE_IDLE} the queue
   * and pushes the resulting element of the pop to the {@link RampLoadQueueType#QUEUE_DOWNLOADING} queue.
   *
   * @return Returns the moved {@link RampLoadDownload} element.
   */
  public synchronized RampLoadDownload moveOneIdleToDownloading() {
    return popFromAndPushTo(RampLoadQueueType.QUEUE_IDLE, RampLoadQueueType.QUEUE_DOWNLOADING);
  }

  /**
   * Removes one element from the {@link RampLoadQueueType#QUEUE_DOWNLOADING}
   * matching with the {@link RampLoadDownload#id}.
   *
   * @param id The id to match with one {@link RampLoadDownload} of the {@link RampLoadQueueType#QUEUE_DOWNLOADING} queue.
   * @return Returns the removed element from {@link RampLoadQueueType#QUEUE_DOWNLOADING} queue.
   */
  public synchronized RampLoadDownload removeDownloadingById(int id) {
    List<RampLoadDownload> listRequests = getList(RampLoadQueueType.QUEUE_DOWNLOADING);
    RampLoadDownload rampLoadDownloadRemove = null;
    for (RampLoadDownload rampLoadDownload : listRequests) {
      if (rampLoadDownload.id == id) {
        rampLoadDownloadRemove = rampLoadDownload;
        break;
      }
    }
    if (rampLoadDownloadRemove != null) {
      listRequests.remove(rampLoadDownloadRemove);
      save(RampLoadQueueType.QUEUE_DOWNLOADING, gson.toJson(listRequests.toArray()));
      return rampLoadDownloadRemove;
    }
    return null;
  }

  public synchronized RampLoadDownload removeIdleById(int id) {
    List<RampLoadDownload> listRequests = getList(RampLoadQueueType.QUEUE_IDLE);
    RampLoadDownload rampLoadDownloadRemove = null;
    for (RampLoadDownload rampLoadDownload : listRequests) {
      if (rampLoadDownload.id == id) {
        rampLoadDownloadRemove = rampLoadDownload;
        break;
      }
    }
    if (rampLoadDownloadRemove != null) {
      listRequests.remove(rampLoadDownloadRemove);
      save(RampLoadQueueType.QUEUE_IDLE, gson.toJson(listRequests.toArray()));
      return rampLoadDownloadRemove;
    }
    return null;
  }

  /**
   * Retrieves the queue with the given parameter {@link RampLoadQueueType}.
   * The push operation is in the position 0 of the list, and the pop is in the ( {@link List#size()} -1 ).
   *
   * @param key The type of queue to retrieve.
   * @return Returns a {@link List} containing RampLoadDownload elements.
   */
  public synchronized List<RampLoadDownload> getList(RampLoadQueueType key) {
    String jsonRequests = Prefs.getString(key.value, null);
    RampLoadDownload[] requests = gson.fromJson(jsonRequests, RampLoadDownload[].class);
    return toList(requests);
  }

  /**
   * Performs a pop operation from the fromKey queue and performs a push with the result element of the pop
   * into the toKey queue.
   *
   * @param fromKey The {@link RampLoadQueueType} queue to pop.
   * @param toKey   The {@link RampLoadQueueType} queue to push the result of the pop.
   * @return Returns moved {@link RampLoadDownload} from fromKey queue to toKey queue.
   */
  public synchronized RampLoadDownload popFromAndPushTo(RampLoadQueueType fromKey, RampLoadQueueType toKey) {
    if (fromKey != null && toKey != null) {
      List<RampLoadDownload> fromRequests = getList(fromKey);
      if (!fromRequests.isEmpty()) {
        RampLoadDownload rampLoadDownload = fromRequests.remove(fromRequests.size() - 1);
        save(fromKey, gson.toJson(fromRequests.toArray()));
        if (!add(toKey, rampLoadDownload)) {
          fromRequests.add(rampLoadDownload);
          save(fromKey, gson.toJson(fromRequests.toArray()));
          return null;
        }
        return rampLoadDownload;
      }
    }
    return null;
  }

  /**
   * Clears a queue with the type of the key parameter.
   *
   * @param key The type of queue to clear.
   */
  public synchronized void clear(RampLoadQueueType key) {
    save(key, gson.toJson(new ArrayList<>().toArray()));
  }

  private RampLoadDownloadManager(Context context) {
    this.context = context.getApplicationContext();
    this.gson = new Gson();
    Prefs.init(this.context);

    if (getList(RampLoadQueueType.QUEUE_IDLE) == null) {
      clear(RampLoadQueueType.QUEUE_IDLE);
    }
    List<RampLoadDownload> downloads = getList(RampLoadQueueType.QUEUE_DOWNLOADING);
    if (downloads == null) {
      clear(RampLoadQueueType.QUEUE_DOWNLOADING);
    } else if (!downloads.isEmpty()) {
      for (RampLoadDownload rampLoadDownload : downloads) {
        addIdle(rampLoadDownload);
      }
      clear(RampLoadQueueType.QUEUE_DOWNLOADING);
    }
  }

  /**
   * Sets a queue with the given json {@link String}.
   *
   * @param key  The type of queue to save the json.
   * @param json The queue in a json format.
   */
  private void save(RampLoadQueueType key, String json) {
    Prefs.setString(key.value, json);
  }

  /**
   * Adds a {@link RampLoadDownload} to the queue of the type key.
   *
   * @param key              The type of the queue to add the rampLoadDownload parameter.
   * @param rampLoadDownload The {@link RampLoadDownload} to add into the queue.
   * @return Returns true if the rampLoadDownload parameter is added successfully to the queue.
   */
  private boolean add(RampLoadQueueType key, RampLoadDownload rampLoadDownload) {
    if (key != null && rampLoadDownload != null) {
      List<RampLoadDownload> listRequests = getList(key);
      boolean abort = false;
      for (RampLoadDownload d : listRequests) {
        if (d.id == rampLoadDownload.id) {
          abort = true;
        }
      }
      if (!abort) {
        listRequests.add(0, rampLoadDownload);
        save(key, gson.toJson(listRequests.toArray()));
        return true;
      }
    }
    return false;
  }

  public void addTestIdles(RampLoadQueueType key, RampLoadDownload rampLoadDownload) {
    add(key, rampLoadDownload);
  }

  /**
   * Converts an array into a {@link List}.
   *
   * @param rampLoadDownloads The array to be converted.
   * @return Returns a {@link List} containing the elements of rampLoadDownloads.
   */
  private List<RampLoadDownload> toList(RampLoadDownload[] rampLoadDownloads) {
    if (rampLoadDownloads != null) {
      List<RampLoadDownload> requests = new ArrayList<>();
      Collections.addAll(requests, rampLoadDownloads);
      return requests;
    }
    return null;
  }
}