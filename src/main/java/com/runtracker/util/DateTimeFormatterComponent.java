package com.runtracker.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@Component("dateTimeFormatterComponent")
public class DateTimeFormatterComponent {
    public String formatDateTime(String dateTime, String inputFormat, String outputFormat) {

        if (dateTime == null || dateTime.isEmpty() || inputFormat == null || outputFormat == null || inputFormat.isEmpty() || outputFormat.isEmpty())
        {
            return "";
        }
        try {
            DateTimeFormatter inFormatter = DateTimeFormatter.ofPattern(inputFormat);
            LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime, inFormatter);
            DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern(outputFormat);
            return parsedDateTime.format(outFormatter);

        } catch (DateTimeParseException e) {
            System.err.println("Error parsing or formatting date/time: " + e.getMessage());
            return e.getMessage();
        } catch (IllegalArgumentException e) {
            System.err.println("Error with date/time format pattern: " + e.getMessage());
            return "";
        }
    }
}
