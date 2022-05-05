package com.dojinyou.devcourse.gccoffeerestapi.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class JdbcUtil {
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
