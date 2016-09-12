package com.tempos21.market.ui;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.android.debug.ViewServer;
import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;

import java.util.ArrayList;
import java.util.Map;

import eu.alfred.ui.AppActivity;

/***
 * <p><b>T21Activity base class</b></p>
 * To be used in T21 projects as standard base class for all Activities
 *
 * @author Sergi Martinez
 * @version 0.1 - proposal 1.
 */
public class TFragmentActivity extends AppActivity {

    /***
     * Called when the activity is first created. This is where you should do
     * all of your normal static set up: create views, bind data to lists, etc.
     * This method also provides you with a Bundle containing the activity's
     * previously frozen state, if there was one.
     * Always followed by onStart().
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TLog.i(this.getLocalClassName() + ": onCreate");
        MarketPlaceHelper.getInstance().setMarketPlace(marketPlace);
        if (TLog.LOG_ENABLED) {
            ViewServer.get(this).addWindow(this);
        }
    }

    /***
     * Called when the system is about to start resuming a previous
     * activity. This is typically used to commit unsaved changes to
     * persistent data, stop animations and other things that may be
     * consuming CPU, etc.
     * Implementations of this method must be very quick because the next
     * activity will not be resumed until this method returns.
     * Followed by either onResume() if the activity returns back to the
     * front, or onStop() if it becomes invisible to the user.
     */
    @Override
    protected void onPause() {
        super.onPause();
        TLog.i(this.getLocalClassName() + ": onPause");
    }

    /***
     * Called when the activity will start interacting with the user. At
     * this point your activity is at the top of the activity stack, with
     * user input going to it.
     * Always followed by onPause().
     */
    @Override
    protected void onResume() {
        super.onResume();
        TLog.i(this.getLocalClassName() + ": onResume");
        MarketPlaceHelper.getInstance().setMarketPlace(marketPlace);
        if (TLog.LOG_ENABLED) {
            ViewServer.get(this).setFocusedWindow(this);
        }
    }

    /***
     * Called when the activity is no longer visible to the user, because
     * another activity has been resumed and is covering this one. This may
     * happen either because a new activity is being started, an existing
     * one is being brought in front of this one, or this one is being
     * destroyed.
     * Followed by either onRestart() if this activity is coming back to
     * interact with the user, or onDestroy() if this activity is going
     * away.
     */
    @Override
    protected void onStop() {
        super.onStop();
        TLog.i(this.getLocalClassName() + ": onStop");
    }

    /***
     * The final call you receive before your activity is destroyed. This
     * can happen either because the activity is finishing (someone called
     * finish() on it, or because the system is temporarily destroying this
     * instance of the activity to save space. You can distinguish between
     * these two scenarios with the isFinishing() method.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        TLog.i(this.getLocalClassName() + ": onDestroy");
        if (TLog.LOG_ENABLED) {
            ViewServer.get(this).removeWindow(this);
        }
    }

    /**
     * Called after your activity has been stopped, prior to it being
     * started again.
     * Always followed by onStart()
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        TLog.i(this.getLocalClassName() + ": onRestart");
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.background_tile);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        getWindow().getDecorView().setBackgroundDrawable(bitmapDrawable);


    }

    /***
     * Called when the activity is becoming visible to the user.
     * Followed by onResume() if the activity comes to the foreground, or
     * onStop() if it becomes hidden.
     */
    @Override
    protected void onStart() {

        super.onStart();
        TLog.i(this.getLocalClassName() + ": onStart");
    }

    @Override
    public void performAction(String command, Map<String, String> map) {
        cade.sendActionResult(true);
    }

    @Override
    public void performWhQuery(String command, Map<String, String> map) {
        ArrayList<Map<String, String>> mapsReturn = new ArrayList<Map<String, String>>();
        cade.sendWHQueryResult(mapsReturn);
    }

    @Override
    public void performValidity(String command, Map<String, String> map) {
        cade.sendValidityResult(true);
    }

    @Override
    public void performEntityRecognizer(String command, Map<String, String> map) {
        ArrayList<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        cade.sendEntityRecognizerResult(resultList);
    }

}
