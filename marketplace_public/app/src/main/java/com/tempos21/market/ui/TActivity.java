
package com.tempos21.market.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tempos21.market.util.TLog;
/***
 * <p><b>T21Activity base class</b></p>
 * To be used in T21 projects as standard base class for all Activities 
 * @version 0.1 - proposal 1.
 * @author Sergi Martinez
 *
 */
public class TActivity extends Activity{

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
	}


	@Override
	public void setContentView(int layoutResID) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.setContentView(layoutResID);
	}
	
	/**
	 * Called after your activity has been stopped, prior to it being 
	 * started again.
	 * Always followed by onStart()
	 */	
	@Override
	protected void onRestart() {
		super.onStart();
		TLog.i(this.getLocalClassName() + ": onRestart");
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
	}


	protected void hideSoftKeyboard() {
		if (getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	protected void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
