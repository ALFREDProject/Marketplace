package com.tempos21.market.ui.fragment;

//import android.annotation.SuppressLint;

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
import com.tempos21.market.db.AppModel;
import com.tempos21.market.ui.AppActivity;
import com.tempos21.market.ui.LoginActivity;
import com.tempos21.market.ui.presenter.AppListByCategoryPresenterImpl;
import com.tempos21.market.ui.presenter.AppListPresenter;
import com.tempos21.market.ui.view.GenericView;
import com.tempos21.market.ui.view.listApps.ListAppsView;
import com.tempos21.market.ui.view.listApps.ListAppsView.OnItemClickListener;
import com.tempos21.market.ui.view.listApps.ListAppsView.OnLoadingStatusListener;
import com.tempos21.market.util.TLog;
import com.tempos21.market.util.Util;
import com.tempos21.mymarket.sdk.model.app.App;
import com.worldline.alfredo.R;

import java.util.List;


//@SuppressLint("ValidFragment")
public final class ListAppsByCategoryFragment extends Fragment implements GenericView<List<App>>, OnItemClickListener, OnLoadingStatusListener {

    private View me;
    private ListAppsView listAppsView;
    private String categoryId = "";
    private int appsCount = 0;
    private TextView notConnection;
    private ProgressBar listProgressBar;
    private TextView textTypeList;
    private ImageView iconTypeList;
    private AppListPresenter presenter;
    private List<App> list;
    private AppModel model;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        me = inflater.inflate(R.layout.latest_apps, null, true);
        model = new AppModel(getActivity());
        model.setCategoryId(categoryId);

        findViews();
        setListeners();
        setCategory();

        if (Util.netConnect(getActivity())) {
            notConnection.setVisibility(View.INVISIBLE);
            listAppsView.setVisibility(View.VISIBLE);
            getData();
        } else {
            if (model.isEmpty()) {
                notConnection.setVisibility(View.VISIBLE);
                listAppsView.setVisibility(View.INVISIBLE);
                hideProgress();
            } else {
                list = model.getAppsByCategory(categoryId);

                listAppsView.setApps(list);
                listAppsView.swapMode();
                listAppsView.setFinished();

                notConnection.setVisibility(View.INVISIBLE);
                listAppsView.setVisibility(View.VISIBLE);
                hideProgress();
            }
        }
        TLog.v("oncreateview");

        return me;
    }


    private void findViews() {
        notConnection = (TextView) me.findViewById(R.id.notConnection);
        listAppsView = (ListAppsView) me.findViewById(R.id.listAppsViews);
        listProgressBar = (ProgressBar) me.findViewById(R.id.listProgressBar);
    }


    private void setListeners() {
        listAppsView.setOnLoadingStatusListener(this);
        listAppsView.setOnItemClickListener(this);
    }


    private void setCategory() {
        textTypeList = (TextView) me.findViewById(R.id.textTypeList);
        iconTypeList = (ImageView) me.findViewById(R.id.iconTypeList);
        int icon = 0;
        String text = "";

        if (categoryId != null) {
            if (categoryId.equalsIgnoreCase("2")) {
                text = getString(R.string.games).toString();
                icon = R.drawable.game;
            }
            if (categoryId.equalsIgnoreCase("3")) {
                text = getString(R.string.sports).toString();
                icon = R.drawable.ball;
            }
            if (categoryId.equalsIgnoreCase("4")) {
                text = getString(R.string.medicine).toString();
                icon = R.drawable.medicine;
            }
            if (categoryId.equalsIgnoreCase("1")) {
                text = getString(R.string.food).toString();
                icon = R.drawable.apple;
            }
            if (categoryId.equalsIgnoreCase("5")) {
                text = getString(R.string.books).toString();
                icon = R.drawable.book;
            }
            if (categoryId.equalsIgnoreCase("6")) {
                text = getString(R.string.travel).toString();
                icon = R.drawable.plane;
            }

            textTypeList.setText(text);
            iconTypeList.setImageResource(icon);
        }
    }


    public void getData() {
        presenter = new AppListByCategoryPresenterImpl(this, Integer.parseInt(categoryId));
        presenter.getAppList();
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
        listAppsView.setApps(list);
        listAppsView.swapMode();
        listAppsView.setFinished();

        this.list = list;

        if (list.size() < 1) {
            listAppsView.setVisibility(View.GONE);
            listProgressBar.setVisibility(View.GONE);
            notConnection.setVisibility(View.VISIBLE);
            notConnection.setText(R.string.not_apps);
        }
        saveInDB();
    }

    @Override
    public void onViewError(long id, Exception e) {
        if (e.getMessage() != null && e.getMessage().equalsIgnoreCase(Constants.WRONG_CREDENTIALS)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            listAppsView.setFinished();
            notConnection.setText(R.string.unable_load);
            notConnection.setVisibility(View.VISIBLE);
        }
    }

    private void saveInDB() {
        for (App app : list) {
            if (model.isOnDB(app)) {
                model.deleteApp(app);
                model.insertApp(app);
            } else {
                model.insertApp(app);
            }
        }

    }

    @Override
    public void onLoadingStatus(int itemCount) {
        appsCount = itemCount;
        getData();
    }


    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public void onItemClick(App app) {
        Intent i = new Intent(getActivity(), AppActivity.class);
        i.putExtra(AppActivity.EXTRA_APP_ID, "" + app.id);
        startActivity(i);
    }

}
