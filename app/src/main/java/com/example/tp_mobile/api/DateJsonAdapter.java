package com.example.tp_mobile.api;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateJsonAdapter {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @ToJson
    String toJson(Date date) {
        return dateFormat.format(date);
    }

    @FromJson
    Date fromJson(String dateString) throws ParseException {
        return dateFormat.parse(dateString);
    }
}