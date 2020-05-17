package com.complexible.pinto;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

public class UTCChecker {
    static boolean isSystemUTCPlusOne()
    {
        Calendar now= Calendar.getInstance();
        TimeZone timeZone=now.getTimeZone();
        return timeZone.getRawOffset() == (60*60*1000);
    }
}
