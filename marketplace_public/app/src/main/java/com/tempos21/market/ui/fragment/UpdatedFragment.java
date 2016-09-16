package com.tempos21.market.ui.fragment;


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
import com.tempos21.market.ui.AppActivity;
import com.tempos21.market.ui.LoginActivity;
import com.tempos21.market.ui.presenter.UpdatableAppListPresenterImpl;
import com.tempos21.market.ui.view.GenericView;
import com.tempos21.market.ui.view.listApps.ListAppsView;
import com.tempos21.market.util.TLog;
import com.tempos21.market.util.Util;
import com.tempos21.mymarket.sdk.model.app.App;
import com.worldline.alfredo.R;

import java.util.List;


//@SuppressLint("ValidFragment")
public class UpdatedFragment extends Fragment implements GenericView<List<App>>, ListAppsView.OnItemClickListener {

    private View fragmentView;
    private ListAppsView updatesList;
    private ProgressBar loading;
    private TextView notConnection;
    private TextView textUpdateList;
    private ImageView iconUpdateList;
    private UpdatableAppListPresenterImpl presenter;


    public UpdatedFragment() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.updated, null, true);

        findViews();
        setListeners();

        if (Util.netConnect(getActivity())) {
            notConnection.setVisibility(View.INVISIBLE);
            getData();
        } else {
            notConnection.setVisibility(View.VISIBLE);
            updatesList.setVisibility(View.INVISIBLE);
        }

        TLog.v("onCreateView");

        return fragmentView;
    }


    private void findViews() {
        updatesList = (ListAppsView) fragmentView.findViewById(R.id.updateList);
        notConnection = (TextView) fragmentView.findViewById(R.id.notConnection);
        loading = (ProgressBar) fragmentView.findViewById(R.id.loading);
        textUpdateList = (TextView) fragmentView.findViewById(R.id.textUpdateList);
        iconUpdateList = (ImageView) fragmentView.findViewById(R.id.iconUpdateList);
    }


    private void setListeners() {
        updatesList.setOnItemClickListener(this);

        textUpdateList.setText(R.string.update_app_title);
        iconUpdateList.setImageResource(R.drawable.update);
    }


    private void getData() {
        presenter = new UpdatableAppListPresenterImpl(this);
        presenter.getAppList();
    }


    @Override
    public void showProgress() {
        loading.setVisibility(View.VISIBLE);
        updatesList.setVisibility(View.GONE);
    }


    @Override
    public void hideProgress() {
        loading.setVisibility(View.GONE);
        updatesList.setVisibility(View.VISIBLE);
    }


    @Override
    public void onViewSuccess(List<App> list) {
        if (list.size() > 0) {
            updatesList.setApps(list);
            updatesList.swapMode();
            updatesList.setFinished();
        } else {
            updatesList.setVisibility(View.GONE);
            notConnection.setVisibility(View.VISIBLE);
            notConnection.setText(R.string.not_apps);
        }
    }


    @Override
    public void onViewError(long id, Exception e) {
        if (e.getMessage() != null && e.getMessage().equalsIgnoreCase(Constants.WRONG_CREDENTIALS)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            updatesList.setFinished();
            notConnection.setText(R.string.unable_load);
            notConnection.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onItemClick(App app) {
        Intent i = new Intent(getActivity(), AppActivity.class);
        i.putExtra(AppActivity.EXTRA_APP_ID, "" + app.id);
        startActivity(i);
    }

}
