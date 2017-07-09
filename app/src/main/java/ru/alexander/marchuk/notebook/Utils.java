package ru.alexander.marchuk.notebook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return dateFormat.format(date);
    }

    public static Date parseDate(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date newDate = null;
        try {
            newDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static String getTime(Date time){
        SimpleDateFormat timeFornat = new SimpleDateFormat("HH:mm");
        return timeFornat.format(time);
    }

    public static Date parseTime(String time){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date newTime = null;
        try {
            newTime = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    public static String getFullDate(Date date){
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEEE\nd MMM yyyy");
        return fullDateFormat.format(date);
    }
}
