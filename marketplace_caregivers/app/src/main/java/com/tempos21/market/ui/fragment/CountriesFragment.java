package com.tempos21.market.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tempos21.market.client.bean.Countries;
import com.tempos21.market.client.bean.Country;
import com.tempos21.market.device.Connection;
import com.tempos21.market.ui.CountryAppsActivity;
import com.tempos21.market.ui.adapter.CountriesAdapter;
import com.tempos21.market.ui.view.AlphabetSelector;
import com.tempos21.market.ui.view.AlphabetSelector.OnLetterSelectedListener;
import com.tempos21.market.util.MarketPlaceHelper;
import com.worldline.alfredo.R;

import java.util.Collections;

import eu.alfred.api.market.responses.country.CountryList;
import eu.alfred.api.market.responses.listener.GetCountryListResponseListener;

//import android.annotation.SuppressLint;

//@SuppressLint({ "ValidFragment", "ValidFragment" })
public class CountriesFragment extends Fragment implements OnItemClickListener, OnLetterSelectedListener {

    private View fragmentView;
    private Activity context;
    private ListView countriesList;
    private Countries countries;
    private Countries shownCountries = new Countries();
    private AlphabetSelector countriesAlphabet;
    private ProgressBar countriesProgress;
    private TextView notCountries;


    public CountriesFragment(Activity context) {
        this.context = context;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.countries, null, true);

        findViews();
        setListeners();

        if (Connection.netConnect(context)) {
            notCountries.setVisibility(View.INVISIBLE);
            getCountries();
            loadAdapter();
        } else {
            countriesProgress.setVisibility(View.INVISIBLE);
            countriesAlphabet.setVisibility(View.INVISIBLE);
            countriesList.setVisibility(View.INVISIBLE);
            notCountries.setVisibility(View.VISIBLE);
            notCountries.setText(R.string.not_connection);
        }


        return fragmentView;
    }


    private void findViews() {
        countriesList = (ListView) fragmentView.findViewById(R.id.countryList);
        countriesAlphabet = (AlphabetSelector) fragmentView.findViewById(R.id.countryAlphabet);
        countriesProgress = (ProgressBar) fragmentView.findViewById(R.id.countryProgress);
        notCountries = (TextView) fragmentView.findViewById(R.id.notCountries);
    }


    private void setListeners() {
        countriesList.setOnItemClickListener(this);
    }


    private void getCountries() {
        countriesList.setVisibility(View.INVISIBLE);
        countriesAlphabet.setVisibility(View.INVISIBLE);
        countriesProgress.setVisibility(View.VISIBLE);
        loadCountries();

    }

    private void loadCountries() {
        MarketPlaceHelper.getInstance().marketPlace.getCountryList(new GetCountryListResponseListener() {
            @Override
            public void onSuccess(CountryList countryList) {
                Countries countries = new Countries();
                Country country;
                for (eu.alfred.api.market.responses.country.Country country1 : countryList.countries) {
                    country = new Country();
                    country.setId("" + (country1.id != null ? country1.id : -1));
                    country.setName(country1.name);
                    countries.add(country);
                }
                onGetCountriesResponse(true, countries);
            }

            @Override
            public void onError(Exception e) {
                onGetCountriesResponse(false, new Countries());
            }
        });
        // TODO
        /*
        GetCountriesService getCountries = new GetCountriesService(Constants.GETCOUNTRIES_SERVICE, getActivity());
        getCountries.setOnGetCountriesServiceResponseListener(this);
        getCountries.runService();
        */
    }

    public void onGetCountriesResponse(boolean success, Countries countries) {
        Collections.sort(countries);
        this.countries = countries;

        CountriesAdapter adapter = new CountriesAdapter(getActivity(), countries);
        countriesList.setAdapter(adapter);
        countriesList.setVisibility(View.VISIBLE);
        countriesAlphabet.setVisibility(View.VISIBLE);
        countriesProgress.setVisibility(View.INVISIBLE);
        countriesAlphabet.setAvailableLetters(countries.getLetters());
        countriesAlphabet.setOnLetterSelectedListener(this);

        if (countries.size() < 1) {
            countriesList.setVisibility(View.INVISIBLE);
            countriesAlphabet.setVisibility(View.GONE);
            if (success) {
                notCountries.setVisibility(View.VISIBLE);
            } else {
                notCountries.setVisibility(View.VISIBLE);
                notCountries.setText(R.string.server_error);
            }

        } else {
            notCountries.setVisibility(View.GONE);
        }
        countriesProgress.setVisibility(View.GONE);

    }


    private void loadAdapter() {
        CountriesAdapter countriesAdapter = new CountriesAdapter(context, shownCountries);
        countriesList.setAdapter(countriesAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Country country = countries.get(position);
        String countryId = country.getId();
        Intent i = new Intent(getActivity(), CountryAppsActivity.class);
        i.putExtra(CountryAppsActivity.EXTRA_COUNTRY_ID, countryId);
        startActivity(i);
    }


    @Override
    public void onLetterSelected(String letter) {
        int position = countries.getFirstWithLetter(letter);
        if (position > -1) {
            countriesList.setSelection(position);
        }

    }

}
