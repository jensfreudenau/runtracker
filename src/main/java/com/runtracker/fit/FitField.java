package com.runtracker.fit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum FitField {
    TOTAL_DISTANCE("total_distance"),
    TOTAL_TIMER_TIME("total_timer_time"),
    AVG_HEART_RATE("avg_heart_rate"),
    MAX_HEART_RATE("max_heart_rate"),
    CALORIES("total_calories"),
    TIMESTAMP("timestamp"),
    START_TIME("start_time");

    private final String fitName;


    public static Optional<FitField> fromFitName(String fitName) {
        return Arrays.stream(values())
                .filter(f -> f.fitName.equalsIgnoreCase(fitName))
                .findFirst();
    }
    FitField(String fitName) {
        this.fitName = fitName;
    }

    public String getFitName() {
        return fitName;
    }

    private static final Map<String, FitField> BY_NAME = new HashMap<>();

    static {
        for (FitField field : values()) {
            BY_NAME.put(field.fitName, field);
        }
    }
}

