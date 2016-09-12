package com.tempos21.market.client.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class App implements Comparable<App> {

    // JSONObject fields

    public final static int SORT_BY_DATE = 0;
    public final static int SORT_BY_RATE = 1;
    private static int sortType = SORT_BY_DATE;
    private String id = "";
    private String name = "";
    private String author = "";
    private int version_number = 0;
    private String version_string = "";
    private String date = "";
    private String icon_url = "";
    private double rating = 0;
    private String promo_url = "";
    private ArrayList<String> screenshots = new ArrayList<String>();
    private boolean externalBinary = false;
    private String externalUrl = "";
    private boolean allowed = false;
    private Platforms platform = new Platforms();
    private String version_id = "";
    // DB fields
    private double size = 0;
    private String packageName = "";
    private int categoryId = 0;
    private String description = "";
    private int target = 0;

    // Other Variables and Constants
    private String supportUrl = "";
    private String supportEmail = "";
    private String notificationEmails = "";
    private int daysLeft = 0;
//	private Bitmap promo;
//	private Bitmap icon;

    //status

    private boolean firstShown = true;

    public App() {

    }

    public App(String name, double d) {
        this.name = name;
        this.rating = d;
    }

    public static int getSortType() {
        return sortType;
    }

    public static void setSortType(int sortType) {
        App.sortType = sortType;
    }

    @Override
    public int compareTo(App another) {
        switch (sortType) {
            case SORT_BY_DATE:
                if (getDaysLeft() == another.getDaysLeft())
                    return 0;
                else if (getDaysLeft() > another.getDaysLeft())
                    return -1;
                else
                    return 1;
            case SORT_BY_RATE:
                if (getRating() == another.getRating())
                    return 0;
                else if (getRating() > another.getRating())
                    return -1;
                else
                    return 1;
        }
        return 0;
    }

    private void udpateDaysLeft() {
        if (date.length() > 0) {
            Calendar calToday = Calendar.getInstance();
            Date today = calToday.getTime();

            Calendar cal = calendarizeString(date);
            java.util.Date end = cal.getTime();
            daysLeft = (int) ((end.getTime() - today.getTime()) / (24 * 60 * 60 * 1000));
        }
    }

    private Calendar calendarizeString(String dateString) {
        int dateStringYear = Integer.parseInt(dateString.substring(0, 4));
        int dateStringMonth = Integer.parseInt(dateString.substring(5, 7));
        int dateStringDay = Integer.parseInt(dateString.substring(8, 10));
//		int dateStringHour = Integer.parseInt(dateString.substring(11,13));
//		int dateStringMinute = Integer.parseInt(dateString.substring(14,16));
//		int dateStringSecond = Integer.parseInt(dateString.substring(17,19));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, dateStringYear);
        cal.set(Calendar.MONTH, dateStringMonth - 1);
        cal.set(Calendar.DAY_OF_MONTH, dateStringDay);
//		cal.set(Calendar.HOUR_OF_DAY, dateStringHour);
//		cal.set(Calendar.MINUTE, dateStringMinute);
//		cal.set(Calendar.SECOND, dateStringSecond);
        return cal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getVersion_number() {
        return version_number;
    }

    public void setVersion_number(int version_number) {
        this.version_number = version_number;
    }

    public String getVersion_string() {
        return version_string;
    }

    public void setVersion_string(String version_string) {
        this.version_string = version_string;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        udpateDaysLeft();
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPromo_url() {
        return promo_url;
    }

    public void setPromo_url(String promo_url) {
        this.promo_url = promo_url;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTarget() {
        return target;
    }
//	public Bitmap getIcon() {
//		return icon;
//	}
//
//	public void setIcon(Bitmap icon) {
//		this.icon = icon;
//	}

    public void setTarget(int target) {
        this.target = target;
    }

    public String getSupportUrl() {
        return supportUrl;
    }

    public void setSupportUrl(String supportUrl) {
        this.supportUrl = supportUrl;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }

    public String getNotificationEmails() {
        return notificationEmails;
    }

    public void setNotificationEmails(String notificationEmails) {
        this.notificationEmails = notificationEmails;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public boolean isFirstShown() {
        return firstShown;
    }

    public void setFirstShown(boolean firstShown) {
        this.firstShown = firstShown;
    }

//	public Bitmap getPromo() {
//		return promo;
//	}
//
//	public void setPromo(Bitmap promo) {
//		this.promo = promo;
//	}

    public ArrayList<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(ArrayList<String> screenshots) {
        this.screenshots = screenshots;
    }

    public boolean isExternalBinary() {
        return externalBinary;
    }

    public void setExternalBinary(boolean externalBinary) {
        this.externalBinary = externalBinary;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public Platforms getPlatform() {
        return platform;
    }

    public void setPlatform(Platforms platform) {
        this.platform = platform;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion_id() {
        return version_id;
    }

    public void setVersion_id(String version_id) {
        this.version_id = version_id;
    }

}
