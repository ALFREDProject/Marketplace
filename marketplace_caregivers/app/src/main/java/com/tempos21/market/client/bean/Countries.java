package com.tempos21.market.client.bean;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Countries extends ArrayList<Country> {
    public Country getCountryById(String id) {
        for (Country country : this) {
            if (id.equals(country.getId())) {
                return country;
            }
        }
        return null;
    }

    public Country getCountryByName(String name) {
        for (Country country : this) {
            if (name.equals(country.getName())) {
                return country;
            }
        }
        return null;
    }

    public int getCountryPosition(String id) {
        int i = 0;
        for (Country country : this) {
            if (country.getId().equals(id)) {
                return i;
            } else {
                i++;
            }
        }
        return -1;
    }

    public String getLetters() {
        StringBuilder letters = new StringBuilder();
        for (Country country : this) {
            letters.append(country.getName().substring(0, 1).toUpperCase());
        }
        return letters.toString();
    }

    public int getFirstWithLetter(String letter) {
        String countryLetter;
        for (int position = 0; position < this.size(); position++) {
            countryLetter = this.get(position).getName().substring(0, 1).toUpperCase();
            if (countryLetter.toUpperCase().equals(letter)) {
                return position;
            }
        }
        return -1;

    }


}
