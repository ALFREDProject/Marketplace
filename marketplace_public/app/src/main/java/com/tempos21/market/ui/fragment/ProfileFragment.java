package com.tempos21.market.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.tempos21.market.Constants;
import com.tempos21.market.ui.LoginActivity;
import com.tempos21.mymarket.sdk.MyMarketPreferences;
import com.tempos21.rampload.util.Prefs;
import com.worldline.alfredo.R;

/**
 * Created by a576023 on 06/03/2015.
 */
public class ProfileFragment extends Fragment {

    private final int mainView = R.layout.profile_fragment_view;
    private EditText nameEditText;
    private EditText emailEditText;
    private ImageView userProfileImg;
    private ImageButton deleteName;
    private ImageButton deleteEmail;
    private View editProfileBtnRoot;
    private Button buttonLogout;
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
        emailEditText = (EditText) v.findViewById(R.id.emailEditText);
        deleteName = (ImageButton) v.findViewById(R.id.deleteName);
        deleteEmail = (ImageButton) v.findViewById(R.id.deleteEmail);
        editProfileBtnRoot = (View) v.findViewById(R.id.editProfileBtnRoot);
        buttonLogout = (Button) v.findViewById(R.id.buttonLogout);

        setListeners();

        userProfileImg.setImageResource(((MainActivityFragment) getActivity()).getUserImage());
        nameEditText.setText(Prefs.getString(Constants.KEY_PROFILE_NAME, name));
        emailEditText.setText(Prefs.getString(Constants.KEY_PROFILE_EMAIL, name + "@gmail.com"));

        return v;
    }

    private void setListeners() {
        deleteName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEditText.setText("");
            }
        });
        deleteEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEditText.setText("");
            }
        });
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Prefs.setString(Constants.KEY_PROFILE_NAME, nameEditText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Prefs.setString(Constants.KEY_PROFILE_EMAIL, emailEditText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editProfileBtnRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nameEditText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(nameEditText, InputMethodManager.SHOW_IMPLICIT);
                } catch (Exception ignored) {
                }
            }
        });
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs.setString(Constants.MY_MARKET_PREFERENCE_KEY_USER_NAME, "");
                Prefs.setString(Constants.MY_MARKET_PREFERENCE_KEY_USER_PASSWORD, "");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
