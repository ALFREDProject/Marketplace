package com.tempos21.market.client.service.input;

public class GetAppsInput implements Input {
    public static final String SORTING_APPS_DATE = "date";
    public static final String SORTING_APPS_RATING = "rating";

    private String sorting = SORTING_APPS_DATE;
    private int start = 0;
    private int elements = 10;
    private String country = null;
    private String category = null;

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getElements() {
        return elements;
    }

    public void setElements(int elements) {
        this.elements = elements;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
