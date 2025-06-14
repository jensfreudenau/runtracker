package com.runtracker.dto;

import com.runtracker.fit.FitField;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.EnumMap;
import java.util.Map;

public class SummaryDTO {
    private final Map<FitField, Object> fields = new EnumMap<>(FitField.class);

    public void set(FitField field, Object value) {
        fields.put(field, value);
    }

    public Object getRaw(FitField field) {
        return fields.get(field);
    }

    public Double getDouble(FitField field) {
        Object val = fields.get(field);
        return val instanceof Number ? ((Number) val).doubleValue() : null;
    }
    public String getString(FitField field) {
        Object val = fields.get(field);
        return val instanceof String ? ((String) val) : null;
    }

    public Integer getInt(FitField field) {
        Object val = fields.get(field);
        return val instanceof Number ? ((Number) val).intValue() : null;
    }

    public String getFormattedTimestamp() {
        long timestampSeconds = (long) fields.get(FitField.TIMESTAMP);
        timestampSeconds += 631065600L;
        Timestamp currTS = new Timestamp(timestampSeconds * 1000);
        DateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd H:mm:s");
        return formattedDate.format(currTS);
    }

    public Map<FitField, Object> getAllFields() {
        return fields;
    }
}
