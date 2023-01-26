package com.dukeai.manageloads.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormatter {

    private String READDATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private String READDATEFORMATWITHHYPHEN = "yyyy-MM-dd'T'HH-mm-ss'Z'";
    private String WRITEDATEFORMAT = "dd MMM ''yy";
    private String TIMEZONE = "GMT";

    public String getFormattedDate(String date) {
        DateFormat readFormat;
        if(date.contains(":")) {
            readFormat = new SimpleDateFormat(READDATEFORMAT);
        } else {
            readFormat = new SimpleDateFormat(READDATEFORMATWITHHYPHEN);
        }

        DateFormat writeFormat = new SimpleDateFormat(WRITEDATEFORMAT);
        writeFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
        Date fdate = null;
        try {
            readFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
            fdate = readFormat.parse(date);
            Log.e("FDate ",fdate+"");
        } catch (Exception e) {
            Log.d("Date format exeception", e.getLocalizedMessage());
        }
        return writeFormat.format(fdate);
    }
}
