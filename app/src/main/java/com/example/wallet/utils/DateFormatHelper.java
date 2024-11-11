package com.example.wallet.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatHelper {
    public static String format(Date date, String template){
        SimpleDateFormat simple = new SimpleDateFormat(template, Locale.getDefault());
        return simple.format(date);
    }

    public static SimpleDateFormat formatAt(Date date, String template){
        return new SimpleDateFormat(template, Locale.getDefault());
    }
}
