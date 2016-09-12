package com.tempos21.market.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtil {


    public static GregorianCalendar milli2Date(String milliseconds) {
        Long milli = Long.valueOf(milliseconds);
        GregorianCalendar result = new GregorianCalendar();
        result.setTimeInMillis(milli);

        return result;

    }

    public static String milli2SpanishDate(String milliseconds) {
        String result;
        GregorianCalendar date = milli2Date(milliseconds);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH) + 1;
        int year = date.get(Calendar.YEAR);
        result = String.format("%02d/%02d/%d", day, month, year);
        return result;
    }
}

