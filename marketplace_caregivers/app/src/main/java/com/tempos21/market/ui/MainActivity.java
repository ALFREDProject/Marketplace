package com.tempos21.market.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.tempos21.market.client.bean.User;
import com.tempos21.market.db.UserModel;
import com.tempos21.market.device.Device;
import com.tempos21.market.ui.fragment.CategoriesFragment;
import com.tempos21.market.ui.fragment.CountriesFragment;
import com.tempos21.market.ui.fragment.HomeFragment;
import com.tempos21.market.ui.fragment.ListAppsFragment;
import com.tempos21.market.ui.fragment.MyAppsFragment;
import com.tempos21.market.ui.fragment.SearchAppsFragment;
import com.tempos21.market.ui.fragment.TestAppsFragment;
import com.tempos21.market.ui.fragment.UpdatedFragment;
import com.tempos21.market.ui.view.FlingView;
import com.tempos21.market.ui.view.FlingView.OnFlingListener;
import com.tempos21.market.ui.view.MenuButtons;
import com.tempos21.market.ui.view.MenuButtons.OnMenuClickListener;
import com.tempos21.market.ui.view.MenuButtons.onSearchListener;
import com.tempos21.market.ui.view.SlidingMenu;
import com.worldline.alfredo.R;

public class MainActivity extends TFragmentActivity implements OnMenuClickListener, OnFlingListener, onSearchListener {

    // private FlingView flingView;
    private SlidingMenu slidingMenu;
    private MenuButtons menuButtons;
    private User user;
    private int currentOption = 1;
    private FlingView flingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        setOrientation();
        findViews();
        setListeners();
        getData();
        setData();

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.getExtras().getBoolean("notification")) {
            openSectionFragment(MenuButtons.UPDATED, true);
            menuButtons.selectedUpdates();
            slidingMenu.setStartClosed(true);
            menuButtons.clickUpdate();
        } else {
            setStartingFragment();
        }
    }

    public void setOrientation() {
        if (Device.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
    }

    public void findViews() {
        // flingView = (FlingView) findViewById(R.id.flingView);
        slidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
        menuButtons = (MenuButtons) findViewById(R.id.menuButtons);
        flingView = (FlingView) findViewById(R.id.flingView);

    }

    public void setListeners() {
        // flingView.setOnFlingListener(this);
        menuButtons.setOnMenuClickListener(this);
        flingView.setOnFlingListener(this);
        menuButtons.setOnSearchListener(this);
        menuButtons.setSlidingMode(slidingMenu);
    }

    private void setStartingFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setContext(this);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        // transaction.addToBackStack(null);
        transaction.replace(R.id.mainFragment, homeFragment);
        transaction.commit();
    }

    private void getData() {
        UserModel model = new UserModel(this);
        user = model.getUser();
    }

    private void setData() {
        menuButtons.setTester(user.isIsTester());
//		menuButtons.setDeveloper(user.isApprover());
    }

    @Override
    public void onMenuClick(int option) {
        slidingMenu.setMode(SlidingMenu.COLLAPSED);
        if (option > 0) {
            openSectionFragment(option, true);
        }
    }

    /*private void openSectionFragment(int option) {
        openSectionFragment(option, false);
    }*/

    private void openSectionFragment(int option, boolean force) {
        if (currentOption != option || force) {
            Fragment newFragment = null;
            currentOption = option;
            if (option == MenuButtons.HOME) {
                newFragment = new HomeFragment();
                ((HomeFragment) newFragment).setContext(this);
            }
            if (option == MenuButtons.LATEST) {
                newFragment = new ListAppsFragment(this);
                ((ListAppsFragment) newFragment).setTitle(getString(R.string.menuLatest));
            }
            if (option == MenuButtons.CATEGORIES) {
                newFragment = new CategoriesFragment();
                ((CategoriesFragment) newFragment).setContext(this);
            }
            if (option == MenuButtons.COUNTRIES) {
                newFragment = new CountriesFragment(this);
            }
            if (option == MenuButtons.MYAPPS) {
                newFragment = new MyAppsFragment(this);
            }
            if (option == MenuButtons.UPDATED) {
                newFragment = new UpdatedFragment(this);
            }
            if (option == MenuButtons.TESTING) {
                newFragment = new TestAppsFragment(this);
            }
            if (newFragment != null) {
                FragmentManager m = this.getSupportFragmentManager();
                FragmentTransaction transaction = m.beginTransaction();
                transaction.setCustomAnimations(R.anim.fragment_enter_right, R.anim.fragment_exit_left);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(R.id.mainFragment, newFragment);
                transaction.commit();

            }
        }
    }

    @Override
    public void onFlingRight() {
        slidingMenu.unfold();
    }

    @Override
    public void onFlingLeft() {
        slidingMenu.fold();

    }

    @Override
    public void onSearch(String query) {
        Fragment newFragment = new SearchAppsFragment(this, query);
        FragmentManager m = this.getSupportFragmentManager();
        FragmentTransaction transaction = m.beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter_right, R.anim.fragment_exit_left);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.mainFragment, newFragment);
        transaction.commit();
        slidingMenu.setMode(SlidingMenu.COLLAPSED);

    }

    @Override
    public void onBackPressed() {
        if (slidingMenu.getMode() == SlidingMenu.COLLAPSED) {
            slidingMenu.setMode(SlidingMenu.NORMAL);
        } else if (slidingMenu.getMode() == SlidingMenu.EXPANDED) {
            slidingMenu.setMode(SlidingMenu.NORMAL);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU)
            if (slidingMenu.getMode() == SlidingMenu.COLLAPSED) {
                slidingMenu.unfold();
            } else {
                slidingMenu.setMode(SlidingMenu.COLLAPSED);
            }
        return super.onKeyDown(keyCode, event);
    }
}
