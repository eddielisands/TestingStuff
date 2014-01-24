package com.conference.app.lib.util;

public interface TimeUtil {
    long SECONDS_PER_MINUTE = 60;
    long MINUTES_PER_HOUR = 60;
    long HOURS_PER_DAY = 24;
    long DAYS_PER_WEEK = 7;

    long SECOND_IN_MS = 1000;
    long MINUTE_IN_MS = 60 * SECOND_IN_MS;
    long HOUR_IN_MS = 60 * MINUTE_IN_MS;
    long DAY_IN_MS = 24 * HOUR_IN_MS;
    long WEEK_IN_MS = 7 * DAY_IN_MS;
}
