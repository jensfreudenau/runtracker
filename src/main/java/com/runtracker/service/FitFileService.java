package com.runtracker.service;

import com.garmin.fit.Decode;
import com.garmin.fit.MesgBroadcaster;
import com.garmin.fit.SessionMesg;
import com.garmin.fit.SessionMesgListener;
import com.runtracker.exceptionHandler.FitException;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FitFileService {

    public FitFileService() {
    }

    public double getTotalTimerTime(InputStream inputStream) throws FitException {
        SummaryListener listener = parseFitFile(inputStream);
        return listener.getTotalTimerTime();
    }

    public double getTotalDistance(InputStream inputStream) throws FitException {
        SummaryListener listener = parseFitFile(inputStream);
        return listener.getTotalDistance() / 1000.0;
    }

    public double getAvgSpeedKmPerHour(InputStream inputStream) throws FitException {
        SummaryListener listener = parseFitFile(inputStream);
        double totalDistance = listener.getTotalDistance();
        double totalTimerTime = listener.getTotalTimerTime();
        if (totalDistance > 0.0 && totalTimerTime > 0.0) {
            double avg = totalDistance / totalTimerTime * 3.6;
            return Math.round(avg * 100.0) / 100.0;
        } else {
            return -1.0;
        }
    }

    private SummaryListener parseFitFile(InputStream inputStream) throws FitException {
        try {
            Decode decode = new Decode();
            MesgBroadcaster broadcaster = new MesgBroadcaster(decode);
            SummaryListener listener = new SummaryListener();
            broadcaster.addListener(listener);
            decode.read(inputStream, broadcaster, broadcaster);
            return listener;
        } catch (Exception e) {
            throw new FitException("Fehler beim Parsen der FIT-Datei", e);
        }
    }

    private static class SummaryListener implements SessionMesgListener {
        private double totalDistance = 0.0;
        private double totalTimerTime = 0.0;

        @Override
        public void onMesg(SessionMesg mesg) {
            if (mesg.getTotalDistance() != null) {
                totalDistance = mesg.getTotalDistance();
            }
            if (mesg.getTotalTimerTime() != null) {
                totalTimerTime = mesg.getTotalTimerTime();
            }
        }

        public double getTotalDistance() {
            return totalDistance;
        }

        public double getTotalTimerTime() {
            return totalTimerTime;
        }
    }
}
