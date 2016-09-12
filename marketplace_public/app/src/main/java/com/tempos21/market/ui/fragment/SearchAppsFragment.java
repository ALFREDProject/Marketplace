package com.tempos21.market.ui.fragment;

import java.util.List;

//import android.annotation.SuppressLint;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tempos21.market.Constants;
import com.tempos21.market.ui.LoginActivity;
import com.tempos21.market.ui.presenter.FilteredAppListPresenterImpl;
import com.tempos21.market.ui.view.GenericView;
import com.tempos21.mymarket.sdk.model.app.App;
import com.worldline.alfredo.R;
import com.tempos21.market.util.Util;
import com.tempos21.market.ui.AppActivity;
import com.tempos21.market.ui.view.listApps.ListAppsView;
import com.tempos21.market.ui.view.listApps.ListAppsView.OnItemClickListener;
import com.tempos21.market.ui.view.listApps.ListAppsView.OnLoadingStatusListener;
import com.tempos21.market.util.TLog;

//@SuppressLint("ValidFragment")
public final class SearchAppsFragment extends Fragment implements GenericView<List<App>>, OnLoadingStatusListener, OnItemClickListener {

	private View me;
	private ListAppsView listAppsView;
	private String query;
	private TextView notConnection;
    private TextView textTypeList;
    private ImageView iconTypeList;
	private FilteredAppListPresenterImpl presenter;
	private ProgressBar listProgressBar;
	private int appsCount = 0;


	public SearchAppsFragment(){
	}


    @SuppressLint("ValidFragment")
	public SearchAppsFragment(Activity context, String query) {
		this.query=query;
		TLog.i("Query arrived:" + query);
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		me = inflater.inflate(R.layout.latest_apps, null, true);

		findViews();
		setListeners();

		if (Util.netConnect(getActivity())) {
			notConnection.setVisibility(View.INVISIBLE);
			getData();
		} else {
			notConnection.setVisibility(View.VISIBLE);
			listAppsView.setVisibility(View.INVISIBLE);
			listProgressBar.setVisibility(View.GONE);
		}
		
		TLog.v("oncreateview");

		return me;
	}


	private void findViews() {
		notConnection = (TextView) me.findViewById(R.id.notConnection);
		listAppsView = (ListAppsView) me.findViewById(R.id.listAppsViews);
		listProgressBar = (ProgressBar) me.findViewById(R.id.listProgressBar);
        textTypeList = (TextView) me.findViewById(R.id.textTypeList);
        iconTypeList = (ImageView) me.findViewById(R.id.iconTypeList);
	}


	private void setListeners() {
		listAppsView.setOnLoadingStatusListener(this);
		listAppsView.setOnItemClickListener(this);

        textTypeList.setText(R.string.request_result);
        iconTypeList.setImageResource(R.drawable.search);
	}


	private void getData() {
		presenter = new FilteredAppListPresenterImpl(this);
		presenter.getAppList(query);
	}


	@Override
	public void showProgress() {
		listProgressBar.setVisibility(View.VISIBLE);
		listAppsView.setVisibility(View.GONE);
	}


	@Override
	public void hideProgress() {
		listProgressBar.setVisibility(View.GONE);
		listAppsView.setVisibility(View.VISIBLE);
	}


	@Override
	public void onViewSuccess(List<App> list) {
		if(list.size() > 0) {
			listAppsView.setApps(list);
			listAppsView.swapMode();
			listAppsView.setFinished();
		}else{
			listAppsView.setVisibility(View.INVISIBLE);
			notConnection.setVisibility(View.VISIBLE);
			notConnection.setText(R.string.not_found);
		}
	}


	@Override
	public void onViewError(long id, Exception e) {
		if(e.getMessage() != null && e.getMessage().equalsIgnoreCase(Constants.WRONG_CREDENTIALS)){
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
			getActivity().finish();
		}else{
			listAppsView.setFinished();
			notConnection.setText(R.string.unable_load);
			notConnection.setVisibility(View.VISIBLE);
		}
	}


	@Override
	public void onLoadingStatus(int itemCount) {
		appsCount = itemCount;
		getData();

	}


	@Override
	public void onItemClick(App app) {
		Intent i=new Intent(getActivity(), AppActivity.class);
		i.putExtra(AppActivity.EXTRA_APP_ID, ""+app.id);
		startActivity(i);
	}

}
