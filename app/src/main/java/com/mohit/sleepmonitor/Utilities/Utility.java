package com.mohit.sleepmonitor.Utilities;

/**
 * Created by Mohit on 23-10-2016.
 */
public class Utility {
    public static String getFriedlyDate(String epochTime) {
        long epoch = Long.valueOf(epochTime);
        String date = new java.text.SimpleDateFormat("hh:mm a").format(new java.util.Date(epoch*1000));
        return date;
    }
}
