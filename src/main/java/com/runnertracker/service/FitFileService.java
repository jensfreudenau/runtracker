package com.runnertracker.service;

import com.garmin.fit.*;
import com.runnertracker.exceptionHandler.FitException;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service // Markiert diese Klasse als Spring Service-Komponente
public class FitFileService {

    /**
     * Parst die FIT-Datei aus dem gegebenen InputStream und extrahiert die Durchschnittsgeschwindigkeit (in m/s).
     *
     * @param inputStream Der InputStream der FIT-Datei.
     * @return Die Durchschnittsgeschwindigkeit in m/s, oder -1.0, wenn keine Geschwindigkeit gefunden werden kann.
     * @throws FitException Wenn ein Fehler beim Parsen der FIT-Datei auftritt.
     * @throws Exception    Für andere allgemeine I/O-Fehler.
     */
    public double getAverageSpeedFromFitFile(InputStream inputStream) throws Exception {
        final double[] averageSpeed = {-1.0}; // Array, um Wert in anonymer Klasse zu ändern

        Decode decode = new Decode();
        MesgBroadcaster mesgBroadcaster = new MesgBroadcaster(decode);

        // Listener für Session-Nachrichten registrieren
        mesgBroadcaster.addListener((SessionMesgListener) mesg -> {
            // Die Durchschnittsgeschwindigkeit wird in m/s (Meter pro Sekunde) gespeichert
            if (mesg.getAvgSpeed() != null) {
                averageSpeed[0] = mesg.getAvgSpeed();
            } else if (mesg.getTotalDistance() != null && mesg.getTotalTimerTime() != null && mesg.getTotalTimerTime() > 0) {
                // Falls avg_speed nicht direkt verfügbar ist, kann sie aus Distanz und Zeit berechnet werden
                averageSpeed[0] = mesg.getTotalDistance() / mesg.getTotalTimerTime();
            }
        });

        // Die Datei dekodieren und die Listener aktivieren
        try {
            decode.read(inputStream);
        } catch (FitRuntimeException e) {
            // Dies tritt auf, wenn ein Problem beim Dekodieren der Datei auftritt (z.B. beschädigte Datei)
            throw new FitException("FitRuntimeException beim Dekodieren der Datei: " + e.getMessage(), e);
        }

        return averageSpeed[0];
    }
}
