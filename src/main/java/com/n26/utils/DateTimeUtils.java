package com.n26.utils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static final ZoneId UTC = ZoneId.of("UTC");
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    public static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT).withZone(UTC);

    public static final long ONE_MINUTE_MILLIS = 60000;

}
