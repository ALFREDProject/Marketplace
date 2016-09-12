package com.tempos21.mymarket.data.client;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tempos21.mymarket.data.client.exception.NoConnectionException;
import com.tempos21.mymarket.data.task.Task;

/**
 * Base class to execute a request to a server.
 *
 * @param <O> The type of object returned
 */
abstract class Client<I, O> extends Task<I, O> {

  public Client(Context context) {
    super(context);
  }

  @Override
  protected final O perform(I request) throws Exception {
    if (isConnected(getContext())) {
      return request(request);
    } else {
      throw new NoConnectionException();
    }
  }

  protected abstract O request(I request) throws Exception;

  private boolean isConnected(final Context context) {
    ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ||
      conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTING ||
      conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
      conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTING) {
      return true;
    } else if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
      return false;
    }
    return false;
  }
}

