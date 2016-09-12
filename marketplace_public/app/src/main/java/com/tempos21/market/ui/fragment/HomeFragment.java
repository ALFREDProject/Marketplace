package com.tempos21.market.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.worldline.alfredo.R;

/**
 * Created by a576023 on 06/03/2015.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private final int mainView = R.layout.home_fragment_view;
    private View latestBtn;
    private View categoriesBtn;
    private View myAppBtn;
    private View updateAppsBtn;
    private HomeFragmentBehaviorListener homeFragmentBehaviorListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(mainView, null, true);
        latestBtn = v.findViewById(R.id.latestBtn);
        categoriesBtn = v.findViewById(R.id.categoriesBtn);
        myAppBtn = v.findViewById(R.id.myAppBtn);
        updateAppsBtn = v.findViewById(R.id.updateAppsBtn);
        setupListener();
        return v;
    }

    private void setupListener() {
        latestBtn.setOnClickListener(this);
        categoriesBtn.setOnClickListener(this);
        myAppBtn.setOnClickListener(this);
        updateAppsBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.latestBtn:
                if (homeFragmentBehaviorListener != null) {
                    homeFragmentBehaviorListener.onLatestButtonClicked();
                }
                break;

            case R.id.categoriesBtn:
                if (homeFragmentBehaviorListener != null) {
                    homeFragmentBehaviorListener.onCategoriesButtonClicked();
                }
                break;

            case R.id.myAppBtn:
                if (homeFragmentBehaviorListener != null) {
                    homeFragmentBehaviorListener.onMyAppButtonClicked();
                }
                break;

            case R.id.updateAppsBtn:
                if (homeFragmentBehaviorListener != null) {
                    homeFragmentBehaviorListener.onUpdateAppsButtonClicked();
                }
                break;
            default:
                break;
        }
    }

    public void setHomeFragmentBehaviorListener(HomeFragmentBehaviorListener homeFragmentBehaviorListener) {
        this.homeFragmentBehaviorListener = homeFragmentBehaviorListener;
    }

    public interface HomeFragmentBehaviorListener {
        public void onLatestButtonClicked();

        public void onCategoriesButtonClicked();

        public void onMyAppButtonClicked();

        public void onUpdateAppsButtonClicked();
    }

}
