package com.runtracker.fit;

import com.garmin.fit.*;
import com.runtracker.dto.LapDTO;
import com.runtracker.dto.SummaryDTO;
import com.runtracker.exceptionHandler.FitException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FitFileService {
//    private static final Logger LOGGER = LoggerFactory.getLogger(FitFileService.class);

    public FitFileService() {
    }

    public record HeartRatePoint(Date timestamp, int heartRate) {
    }

    public record GpsPoint(double latitude, double longitude) {
    }

    public record PcoPoint(Date timestamp, Integer leftPco, Integer rightPco) {
    }

    public List<LapDTO> getLaps(InputStream inputStream) throws FitException {
        try {
            Decode decode = new Decode();
            MesgBroadcaster broadcaster = new MesgBroadcaster(decode);
            LapListener lapListener = new LapListener();
            broadcaster.addListener(lapListener);
            decode.read(inputStream, broadcaster, broadcaster);
            return lapListener.getLaps();
        } catch (Exception e) {
            throw new FitException("Fehler beim Parsen der FIT-Datei", e);
        }
    }

    public SummaryDTO getSummary(InputStream inputStream) throws FitException {
        try {
            Decode decode = new Decode();
            MesgBroadcaster broadcaster = new MesgBroadcaster(decode);
            SummaryListener listener = new SummaryListener();
            broadcaster.addListener(listener);
            decode.read(inputStream, broadcaster, broadcaster);
            return listener.toSummary();
        } catch (Exception e) {
            throw new FitException("Fehler beim Parsen der FIT-Datei", e);
        }
    }

    public static class HeartRateRecordListener implements RecordMesgListener {
        private final List<HeartRatePoint> heartRateSeries = new ArrayList<>();

        @Override
        public void onMesg(RecordMesg mesg) {
            if (mesg.getHeartRate() != null && mesg.getTimestamp() != null) {
                int heartRate = mesg.getHeartRate(); // bpm
                Date timestamp = mesg.getTimestamp().getDate(); // java.util.Date
                heartRateSeries.add(new HeartRatePoint(timestamp, heartRate));
            }
        }

        public List<HeartRatePoint> getHeartRateSeries() {
            return heartRateSeries;
        }
    }
    public static class PcoRecordListener implements RecordMesgListener {

        private final List<FitFileService.PcoPoint> pcoPoints = new ArrayList<>();

        @Override
        public void onMesg(RecordMesg mesg) {
            Date timestamp = mesg.getTimestamp() != null
                    ? Date.from(mesg.getTimestamp().getDate().toInstant())
                    : null;
            Byte leftPcoByte = mesg.getLeftPco();
            Integer leftPco = leftPcoByte != null ? leftPcoByte.intValue() : null;

            Byte rightPcoByte = mesg.getRightPco();
            Integer rightPco = rightPcoByte != null ? rightPcoByte.intValue() : null;

            // Null-Werte abfangen
            if (timestamp != null && (leftPco != null || rightPco != null)) {
                pcoPoints.add(new FitFileService.PcoPoint(timestamp,
                        leftPco != null ? leftPco : null,
                        rightPco != null ? rightPco : null));
            }
        }

        public List<FitFileService.PcoPoint> getPcoPoints() {
            return pcoPoints;
        }
    }
    public List<PcoPoint> extractPcoSeries(InputStream inputStream) throws FitException {
        try {
            Decode decode = new Decode();
            MesgBroadcaster broadcaster = new MesgBroadcaster(decode);
            PcoRecordListener pcoListener = new PcoRecordListener();
            broadcaster.addListener(pcoListener);

            decode.read(inputStream, broadcaster, broadcaster);

            return pcoListener.getPcoPoints();
        } catch (Exception e) {
            throw new FitException("Fehler beim Lesen der PCO-Daten", e);
        }
    }
    public List<HeartRatePoint> extractHeartRateSeries(InputStream inputStream) throws FitException {
        try {
            Decode decode = new Decode();
            MesgBroadcaster broadcaster = new MesgBroadcaster(decode);
            HeartRateRecordListener hrListener = new HeartRateRecordListener();
            broadcaster.addListener(hrListener);
            decode.read(inputStream, broadcaster, broadcaster);
            return hrListener.getHeartRateSeries();
        } catch (Exception e) {
            throw new FitException("Fehler beim Lesen der Herzfrequenzdaten", e);
        }
    }

    public List<GpsPoint> extractGpsSeries(InputStream inputStream) throws FitException {
        try {
            Decode decode = new Decode();
            MesgBroadcaster broadcaster = new MesgBroadcaster(decode);
            GpsRecordListener listener = new GpsRecordListener();
            broadcaster.addListener(listener);
            decode.read(inputStream, broadcaster, broadcaster);
            return listener.getPoints();
        } catch (Exception e) {
            throw new FitException("Fehler beim Lesen der GPS-Daten", e);
        }
    }

    private static class GpsRecordListener implements RecordMesgListener {
        private final List<GpsPoint> points = new ArrayList<>();

        @Override
        public void onMesg(RecordMesg mesg) {
            if (mesg.getPositionLat() != null && mesg.getPositionLong() != null) {
                double lat = mesg.getPositionLat() * (180.0 / Math.pow(2, 31));
                double lon = mesg.getPositionLong() * (180.0 / Math.pow(2, 31));
                points.add(new GpsPoint(lat, lon));
            }
        }

        public List<GpsPoint> getPoints() {
            return points;
        }
    }

    private static class LapListener implements LapMesgListener {
        private final List<LapDTO> lapDTOS = new ArrayList<>();

        @Override
        public void onMesg(LapMesg lapMesg) {
            LapDTO lapDTO = new LapDTO();
            for (Field field : lapMesg.getFields()) {
                String name = field.getName();
                Object value = field.getValue();
                FitField.fromFitName(name).ifPresent(fe -> lapDTO.set(fe, value));
            }
            lapDTOS.add(lapDTO);
        }

        public List<LapDTO> getLaps() {
            return lapDTOS;
        }
    }

    private static class SummaryListener implements SessionMesgListener {
        private final SummaryDTO summaryDTO = new SummaryDTO();

        @Override
        public void onMesg(SessionMesg mesg) {
            for (Field field : mesg.getFields()) {
                String name = field.getName();
                Object value = field.getValue();
                FitField.fromFitName(name).ifPresent(fe -> summaryDTO.set(fe, value));
            }
        }

        public SummaryDTO toSummary() {
            return summaryDTO;
        }
    }
}

