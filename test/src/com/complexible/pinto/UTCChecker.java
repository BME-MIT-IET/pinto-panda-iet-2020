package com.complexible.pinto;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class UTCChecker {
    static boolean isSystemUTC()
    {
        return ZonedDateTime.now().getOffset()==ZoneOffset.UTC;
    }
}
