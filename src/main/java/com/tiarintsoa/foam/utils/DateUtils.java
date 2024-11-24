package com.tiarintsoa.foam.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class DateUtils {
    public static LocalDateTime convertToLocalDateTime(String dateStr) {
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDateTime.parse(dateStr + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static LocalDateTime generateRandomDate(LocalDate startDate, LocalDate endDate) {

        // Generate a random number of days between the start and end date
        long startEpoch = startDate.toEpochDay();
        long endEpoch = endDate.toEpochDay();
        long randomEpochDay = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch);

        // Convert the random epoch day to a LocalDate
        LocalDate randomDate = LocalDate.ofEpochDay(randomEpochDay);

        // Generate a random time within the day (for example, between 00:00 and 23:59)
        int hour = ThreadLocalRandom.current().nextInt(0, 24);
        int minute = ThreadLocalRandom.current().nextInt(0, 60);
        int second = ThreadLocalRandom.current().nextInt(0, 60);

        // Convert to LocalDateTime with random time
        return randomDate.atTime(hour, minute, second);
    }

}

