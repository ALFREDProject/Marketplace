package com.tempos21.market.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.worldline.alfredo.R;

import java.util.Locale;

/**
 * Created by a576023 on 06/03/2015.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener, View.OnFocusChangeListener {

    private final int mainView = R.layout.search_fragment_view;
    private EditText editTextSearch;
    private View buttons;
    private View send;
    private View cancel;
    private View keyboardButton;
    private View microButton;
    private View searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(mainView, null, true);
        editTextSearch = (EditText) v.findViewById(R.id.search_edit_txt);
        keyboardButton = v.findViewById(R.id.keyboardButton);
        searchView = v.findViewById(R.id.search_view);
        microButton = v.findViewById(R.id.microButton);
        buttons = v.findViewById(R.id.search_buttons);
        send = v.findViewById(R.id.send_button);
        cancel = v.findViewById(R.id.cancel_button);

        keyboardButton.setOnClickListener(this);
        send.setOnClickListener(this);
        cancel.setOnClickListener(this);
        editTextSearch.setOnClickListener(this);
        editTextSearch.setOnEditorActionListener(this);
        editTextSearch.setOnFocusChangeListener(this);
        microButton.setOnClickListener(this);

        buttons.setVisibility(View.INVISIBLE);

        return v;
    }


    public void search() {
        Fragment newFragment = new SearchAppsFragment(getActivity(), editTextSearch.getText().toString());
        ((MainActivityFragment) getActivity()).replaceFragment(R.id.bottomFragment, newFragment, true);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            KeyboardUtils.hideSoftKeyBoard(getActivity(), editTextSearch);
            search();
            handled = true;
        }
        return handled;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:
                search();
                break;

            case R.id.cancel_button:
                showSearchMainView();
                break;

            case R.id.keyboardButton:
                keyboardButtonAction();
                break;

            case R.id.microButton:
                voiceRecognition();
                break;

            default:
                break;
        }
    }


    private void showSearchMainView() {
        buttons.setVisibility(View.INVISIBLE);
        keyboardButton.setVisibility(View.VISIBLE);
        microButton.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);
    }


    private void keyboardButtonAction() {
        buttons.setVisibility(View.VISIBLE);
        keyboardButton.setVisibility(View.INVISIBLE);
        microButton.setVisibility(View.INVISIBLE);
        searchView.setVisibility(View.GONE);
    }


    /**
     * Showing google speech input dialog
     * */
    public void voiceRecognition(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            getActivity().startActivityForResult(intent, MainActivityFragment.REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(), "NOT SUPPORTED", Toast.LENGTH_SHORT).show();
        }
    }


    public void sendSearchTextToServer(String searchText){
        Fragment newFragment = new SearchAppsFragment(getActivity(), searchText);
        ((MainActivityFragment) getActivity()).replaceFragment(R.id.bottomFragment, newFragment, true);
    }


    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b) {
            KeyboardUtils.hideSoftKeyBoard(getActivity(), view);
        }
    }

}
