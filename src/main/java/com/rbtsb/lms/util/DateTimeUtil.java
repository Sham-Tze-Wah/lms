package com.rbtsb.lms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    public static String DateToString(Date inputDate) throws ParseException {
        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(inputDate.toString());
        return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSXXX").format(date);
    }

    public static Date yyyyMMddDate(Date inputDate) throws ParseException {
        String dateStr = DateToString(inputDate);
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
    }
}
