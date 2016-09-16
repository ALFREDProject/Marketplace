package com.tempos21.market.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tempos21.market.Constants;
import com.tempos21.market.gcm.GCMHarvester;
import com.tempos21.mymarket.sdk.MyMarketPreferences;
import com.tempos21.rampload.util.Prefs;
import com.worldline.alfredo.R;

import java.util.ArrayList;


public class MainActivityFragment extends FragmentActivity implements MenuFragment.MenuFragmentBehaviorListener, HomeFragment.HomeFragmentBehaviorListener {

    public static final int REQ_CODE_SPEECH_INPUT = 100;

    private final int main = R.layout.main_holder;
    private MenuFragment menuFragment;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private CategoryFragment categoryFragment;
    private SearchFragment searchFragment;
    private ListAppsFragment listAppsFragment;
    private MyAppsFragment myAppsFragment;
    private UpdatedFragment updatedFragment;
    private int tapExitCount = 0;
    private boolean showExitMessage;

    private void initFragments() {
        if (menuFragment == null) {
            menuFragment = new MenuFragment();
            menuFragment.setMenuFragmentBehaviorListener(this);
        }
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            homeFragment.setHomeFragmentBehaviorListener(this);
        }

        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }

        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }

        if (categoryFragment == null) {
            categoryFragment = new CategoryFragment();
        }


        if (listAppsFragment == null) {
            listAppsFragment = new ListAppsFragment();
        }

        if (myAppsFragment == null) {
            myAppsFragment = new MyAppsFragment();
        }


        if (updatedFragment == null) {
            updatedFragment = new UpdatedFragment();
        }


        replaceFragment(R.id.topFragment, menuFragment, false);
        replaceFragment(R.id.bottomFragment, homeFragment, false);
        tapExitCount = 1;
    }

    private void registerPush() {
        String registrationId = MyMarketPreferences.getInstance(getApplicationContext()).getString(Constants.MY_MARKET_PREFERENCE_KEY_TOKEN, null);
        GCMHarvester pushH = new GCMHarvester();
        pushH.pushRegister(registrationId);
    }

    public int getUserImage() {
        String user = Prefs.getString(Constants.MY_MARKET_PREFERENCE_KEY_USER_NAME, "");

        if (user.equalsIgnoreCase("otto")) {
            return R.drawable.otto_profile;
        }

        if (user.equalsIgnoreCase("olivia")) {
            return R.drawable.olivia_profile;
        }

        if (user.equalsIgnoreCase("hilde")) {
            return R.drawable.hilde_profile;
        }

        return R.drawable.olivia_profile;
    }

    public void replaceFragment(int srcViewHolderId, Fragment fragment, boolean addToBckStack) {
//        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//            trans.replace(srcViewHolderId, fragment);
//        if (addToBckStack) {
//            trans.addToBackStack(fragment.getClass().getSimpleName());
//        }
//        trans.commit();

        replaceMainFragment(srcViewHolderId, fragment, addToBckStack, true);


    }

    private void clearBackStack() {

        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();

        }
    }

    public int replaceMainFragment(int srcViewHolderId, Fragment f, boolean mustBeAddedToBackStack, boolean mustBeAnimated) {
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();

        if (mustBeAnimated) {
            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }

        ft.replace(srcViewHolderId, f, ((Object) f).getClass().getName());

        if (mustBeAddedToBackStack) {
            ft.addToBackStack(null);
        }
        return ft.commit();
    }

    private void addFragment(int srcViewHolderId, Fragment fragment, boolean addToBckStack) {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.add(srcViewHolderId, fragment);
        if (addToBckStack) {
            trans.addToBackStack(fragment.getClass().getSimpleName());
        }
        trans.commit();
    }

    @Override
    public void onSearchButtonClicked() {
        menuFragment.searchBtn.setSelected(true);
        menuFragment.homeBtn.setSelected(false);
        clearBackStack();
        replaceFragment(R.id.bottomFragment, searchFragment, false);
    }

    @Override
    public void onHomeButtonClicked() {
        menuFragment.searchBtn.setSelected(false);
        menuFragment.homeBtn.setSelected(true);
        clearBackStack();
        replaceFragment(R.id.bottomFragment, homeFragment, false);
        tapExitCount = 1;
    }

    @Override
    public void onProfileButtonClicked() {
        menuFragment.searchBtn.setSelected(false);
        menuFragment.homeBtn.setSelected(false);
        clearBackStack();
        replaceFragment(R.id.bottomFragment, profileFragment, false);
    }

    @Override
    public void onLatestButtonClicked() {
        menuFragment.searchBtn.setSelected(false);
        menuFragment.homeBtn.setSelected(false);
        replaceFragment(R.id.bottomFragment, listAppsFragment, true);
    }

    @Override
    public void onCategoriesButtonClicked() {
        menuFragment.searchBtn.setSelected(false);
        menuFragment.homeBtn.setSelected(false);
        replaceFragment(R.id.bottomFragment, categoryFragment, true);
    }

    @Override
    public void onMyAppButtonClicked() {
        menuFragment.searchBtn.setSelected(false);
        menuFragment.homeBtn.setSelected(false);
        replaceFragment(R.id.bottomFragment, myAppsFragment, true);
    }

    @Override
    public void onUpdateAppsButtonClicked() {
        menuFragment.searchBtn.setSelected(false);
        menuFragment.homeBtn.setSelected(false);
        replaceFragment(R.id.bottomFragment, updatedFragment, true);
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (!result.isEmpty()) {
                        searchFragment.sendSearchTextToServer(result.get(0));
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (homeFragment.isPaused()) {
            onHomeButtonClicked();
        } else {
            if (tapExitCount > 0) {
                tapExitCount--;
                showMessage(MainActivityFragment.this, getString(R.string.tap_again_to_exit));
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(main);
        initFragments();
        registerPush();

    }

    protected void showMessage(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        View view = toast.getView();
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setBackgroundColor(getResources().getColor(R.color.transparent));
        text.setTextColor(getResources().getColor(R.color.White));
        /*here you can do anything with text*/
        toast.show();
    }


}
