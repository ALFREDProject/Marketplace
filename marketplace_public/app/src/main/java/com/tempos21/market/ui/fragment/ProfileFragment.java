package com.tempos21.market.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.tempos21.market.Constants;
import com.tempos21.mymarket.sdk.MyMarketPreferences;
import com.worldline.alfredo.R;

/**
 * Created by a576023 on 06/03/2015.
 */
public class ProfileFragment extends Fragment {

    private final int mainView = R.layout.profile_fragment_view;
    private EditText nameEditText;
    private EditText emailEditText;
    private ImageView userProfileImg;
    private String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        name = MyMarketPreferences.getInstance(getActivity()).getString(Constants.MY_MARKET_PREFERENCE_KEY_NAME, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(mainView, null, true);
        nameEditText = (EditText) v.findViewById(R.id.nameEditText);
        userProfileImg = (ImageView) v.findViewById(R.id.userProfileImg);
        userProfileImg.setImageResource(((MainActivityFragment)getActivity()).getUserImage());
        emailEditText = (EditText) v.findViewById(R.id.emailEditText);
        nameEditText.setText(name);
        emailEditText.setText(name+"@gmail.com");
        return v;
    }
}
