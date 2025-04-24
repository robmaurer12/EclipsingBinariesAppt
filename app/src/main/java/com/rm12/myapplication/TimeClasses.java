package com.rm12.myapplication;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeClasses {
    public static String convertSecondsToTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        totalSeconds %= 3600;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return "Time after Start " + hours + " hrs " + minutes + " min " + seconds + " sec";
    }
    public static String addtimetodate(String originalDateStr, String addtime) throws Exception {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a MMM dd yyyy");
        Date originalDate = dateFormat.parse(originalDateStr);
        Pattern pattern = Pattern.compile("(\\d+)\\s*(hrs?|min|sec)");
        Matcher matcher = pattern.matcher(addtime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(originalDate);
        while (matcher.find()) {
            int value = Integer.parseInt(Objects.requireNonNull(matcher.group(1)));
            switch (Objects.requireNonNull(matcher.group(2))) {
                case "hrs": calendar.add(Calendar.HOUR_OF_DAY, value); break;
                case "min": calendar.add(Calendar.MINUTE, value); break;
                case "sec": calendar.add(Calendar.SECOND, value); break;
            }
        }
        return dateFormat.format(calendar.getTime());
    }
    @SuppressLint("DefaultLocale")
    public static String makeDateString(int day, int month, int year) {
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return String.format("%s %02d %d", months[month - 1], day, year);
    }
}

