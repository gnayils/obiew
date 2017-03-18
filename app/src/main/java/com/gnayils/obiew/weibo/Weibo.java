package com.gnayils.obiew.weibo;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Gnayils on 11/03/2017.
 */

public class Weibo {


    public static class Date {

        public static final String TAG = Date.class.getSimpleName();

        public static final DateFormat DISPLAY_FORMAT = new SimpleDateFormat("yy-MM-dd HH:mm", Locale.ENGLISH);

        public static final DateFormat SOURCE_FORMAT = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

        public static String format(String source) {
            try {
                java.util.Date sourceDate = SOURCE_FORMAT.parse(source);
                return DISPLAY_FORMAT.format(sourceDate);
            } catch (ParseException e) {
                Log.e(TAG, "date format failed: " + source, e);
                return source;
            }
        }

    }

}
