package com.runtracker.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
@Service
public final class UtilsService {

    public static String formatDuration(double totalTimerTime) {
        Duration duration = Duration.ofMillis((long) (totalTimerTime * 1000));
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }
}
