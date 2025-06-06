package com.runtracker.fit;

import com.garmin.fit.*;
import com.runtracker.exceptionHandler.FitException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Service
public class FitFileService {

    public FitFileService() {
    }

    public Summary getSummary(InputStream inputStream) throws FitException {
        try {
            Decode decode = new Decode();
            MesgBroadcaster broadcaster = new MesgBroadcaster(decode);
            SummaryListener listener = new SummaryListener();
            broadcaster.addListener(listener);
            decode.read(inputStream, broadcaster, broadcaster);
            return listener.toSummary();
        } catch (Exception e) {
            throw new FitException("Fehler beim Parsen der FIT-Datei");
        }
    }

    public static class Summary {
        private final Map<FitField, Object> data;

        public Summary(Map<FitField, Object> data) {
            this.data = data;
        }

        public double getTotalDistanceKm() {
            return getDouble(FitField.TOTAL_DISTANCE) / 1000.0;
        }

        public double getTotalTimerTime() {
            return getDouble(FitField.TOTAL_TIMER_TIME);
        }

        public int getAvgHeartRate() {
            return getInt(FitField.AVG_HEART_RATE);
        }

        public int getMaxHeartRate() {
            return getInt(FitField.MAX_HEART_RATE);
        }

        public int getCalories() {
            return getInt(FitField.CALORIES);
        }

        public String getFormattedTimestamp() {
            Object ts = data.get(FitField.TIMESTAMP);
            if (ts instanceof DateTime) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(((DateTime) ts).getDate());
            }
            return null;
        }

        public String getFormattedStartTime() {
            Object ts = data.get(FitField.START_TIME);
            if (ts instanceof DateTime) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(((DateTime) ts).getDate());
            }
            return null;
        }

        public double getAvgSpeedKmPerHour() {
            double dist = getDouble(FitField.TOTAL_DISTANCE);
            double time = getDouble(FitField.TOTAL_TIMER_TIME);
            return (dist > 0 && time > 0) ? Math.round((dist / time * 3.6) * 100.0) / 100.0 : -1.0;
        }

        private double getDouble(FitField field) {
            Object value = data.get(field);
            return value instanceof Number ? ((Number) value).doubleValue() : 0.0;
        }

        private int getInt(FitField field) {
            Object value = data.get(field);
            return value instanceof Number ? ((Number) value).intValue() : 0;
        }

        public Set<FitField> getAvailableFields() {
            return data.keySet();
        }

        public Object getRaw(FitField field) {
            return data.get(field);
        }
    }

    private static class SummaryListener implements SessionMesgListener {

        private final Map<FitField, Object> fields = new EnumMap<>(FitField.class);

        @Override
        public void onMesg(SessionMesg mesg) {
            // Direkt bekannte Timestamp-Felder verarbeiten
            if (mesg.getTimestamp() != null) {
                fields.put(FitField.TIMESTAMP, mesg.getTimestamp());
            }

            // Alle Felder durchgehen
            for (Field field : mesg.getFields()) {
                String name = field.getName();
                Object value = field.getValue();

                if ("timestamp".equals(name)) continue; // bereits verarbeitet

                FitField.fromFitName(name).ifPresent(fitField -> fields.put(fitField, value));
            }
        }

        public Summary toSummary() {
            return new Summary(fields);
        }
    }
}

