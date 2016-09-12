package com.tempos21.market.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.worldline.alfredo.R;

/**
 * Created by a576023 on 06/03/2015.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {

    private final int mainView = R.layout.menu_fragment_view;
    public View homeBtn;
    public View profileBtn;
    public View searchBtn;
    private MenuFragmentBehaviorListener menuFragmentBehaviorListener;
    private ImageView imageProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(mainView, null, true);
        homeBtn = v.findViewById(R.id.homeBtn);
        profileBtn = v.findViewById(R.id.profileBtn);
        searchBtn = v.findViewById(R.id.searchBtn);
        imageProfile = (ImageView) v.findViewById(R.id.imageView);
        imageProfile.setImageResource(((MainActivityFragment) getActivity()).getUserImage());

        homeBtn.setSelected(true);
        homeBtn.setOnClickListener(this);
        profileBtn.setOnClickListener(this);
        searchBtn.setSelected(false);
        searchBtn.setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileBtn:
                if (menuFragmentBehaviorListener != null) {
                    menuFragmentBehaviorListener.onProfileButtonClicked();
                }
                break;

            case R.id.homeBtn:
                if (menuFragmentBehaviorListener != null) {
                    menuFragmentBehaviorListener.onHomeButtonClicked();
                }
                break;

            case R.id.searchBtn:
                if (menuFragmentBehaviorListener != null) {
                    menuFragmentBehaviorListener.onSearchButtonClicked();
                }
                break;
            default:
                break;
        }
    }


    public void setMenuFragmentBehaviorListener(MenuFragmentBehaviorListener menuFragmentBehaviorListener) {
        this.menuFragmentBehaviorListener = menuFragmentBehaviorListener;
    }

    public interface MenuFragmentBehaviorListener {
        public void onSearchButtonClicked();

        public void onHomeButtonClicked();

        public void onProfileButtonClicked();
    }

}
