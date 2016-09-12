package com.tempos21.market.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.worldline.alfredo.R;


/**
 * This class show all Error messages of the application
 *
 * @author A519130
 */
public class ShowError {

    private static DialogInterface reconnectDialog = null;


    /**
     * This method show an Alert Dialog with an specific error ocurred
     *
     * @param title   id of Title of error
     * @param message id of body error message
     */
    public static void showAlertDialog(Context context, int title, int message) {
        final AlertDialog.Builder dialog = new Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.ok,
                new android.content.DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        try {
            dialog.show();
        } catch (Exception ignored) {
        }
    }


    /**
     * This method show an Alert Dialog to indicate the reconnecting process
     *
     * @param context Context of Activity to show the Alert
     */
    public static void showReconnectionDialog(Context context) {
        final AlertDialog.Builder dialog = new Builder(context);

        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.reconnect_view, null);

        dialog.setView(view);
        dialog.setTitle(R.string.lost_session);
        try {
            reconnectDialog = dialog.show();
        } catch (Exception ignored) {
        }
    }


    /**
     * This method hide the reconnecting alert dialog when reconnection process finish
     */
    public static void hideReconnectionDialog() {
        if (reconnectDialog != null) {
            try {
                reconnectDialog.dismiss();
            } catch (Exception ignored) {
            }
        }
    }
}
