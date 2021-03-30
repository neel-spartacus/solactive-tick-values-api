package com.solactive.challenge.solactivetickvalues.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    public  static LocalDateTime convertEpochToDate(String timestamp) {

                return LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.valueOf(timestamp)), ZoneId.systemDefault());
    }


}
