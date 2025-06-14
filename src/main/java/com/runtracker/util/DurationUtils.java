package com.runtracker.util;

import org.springframework.stereotype.Component;

import java.time.Duration;

@Component("durationUtils")
public class DurationUtils {

    public String format(Duration duration) {
        if (duration == null) return "";

        long seconds = duration.getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        if (secs > 0 || sb.length() == 0) sb.append(secs).append(" sec");

        return sb.toString().trim();
    }
}
