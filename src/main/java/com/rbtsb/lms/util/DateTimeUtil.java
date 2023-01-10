package com.rbtsb.lms.util;

import net.bytebuddy.build.Plugin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    public static String DateToString(Date inputDate) {
        try{
            System.out.println(inputDate);
            String inputDateStr = removeMilliSecond(inputDate.toString());
            Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(inputDateStr);
            return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSXXX").format(date);
        }
        catch(ParseException paEx){
            return null;
        }
    }

    public static String removeMilliSecond(String inputDate){
        //remove millisecondIndex
        int millisecondIndex = inputDate.toString().indexOf('.');
        if(millisecondIndex > 0){
            inputDate = inputDate.substring(0, millisecondIndex);
            return inputDate;
        }
        else{
            return inputDate;
        }
    }

    public static Date yyyyMMddDate(Date inputDate) throws ParseException {
        String dateStr = DateToString(inputDate);
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
    }

    public static String yyyyMMddhhmmssDateTime(Date inputDate) throws ParseException {
        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(inputDate.toString());
        return new SimpleDateFormat("yyyy-MM-dd'T'hh-mm-ss").format(date);
    }

    public static String getKLDateTimeNow(){
        ZonedDateTime klDateTime = LocalDateTime.now().atZone(ZoneId.of("Asia/Kuala_Lumpur"));
        String time = "Current time - " + klDateTime.format(DateTimeFormatter
                .ofPattern("EEE MMM dd HH:mm:ss zzz yyyy"));
        return time;
    }

    public static Date stringToDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    //for constructor of verification token only
    public static Date calculateExpirationDate(int expirationTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }

    //public static String get
}
