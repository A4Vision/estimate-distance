package com.backtolife.survey.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static final String DEFAULT_FORMAT = "dd/MM HH:mm:ss.SSS";

    public static String prettyTime(double secondsFromEpoch){
        return prettyTime(secondsFromEpoch, DEFAULT_FORMAT);
    }

    public static String prettyTime(double timeFromEpoch, String format){
        Date d = new Date((long)(timeFromEpoch * 1000));

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        return sdf.format(d);
    }
}
