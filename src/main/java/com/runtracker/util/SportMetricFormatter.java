package com.runtracker.util;

import org.springframework.stereotype.Component;

@Component("sportMetricFormatter") // Bean-Name für Thymeleaf
public class SportMetricFormatter {

    // Sport-Konstanten (angenommen, das sind deine Werte)
    public static final int SPORT_RUNNING = 1;
    public static final int SPORT_CYCLING = 2;

    /**
     * Berechnet und formatiert die passende Metrik (Pace für Laufen, Geschwindigkeit für Radfahren).
     *
     * @param sportType Der Sport-Typ (1 für Laufen, 2 für Radfahren).
     * @param distanceMeters Die Distanz in Metern.
     * @param totalTimerTimeSeconds Die Zeit in Sekunden.
     * @return Der formatierte String der Pace oder Geschwindigkeit.
     */
    public String formatMetric(int sportType, double distanceMeters, double totalTimerTimeSeconds) {
        if (distanceMeters <= 0 || totalTimerTimeSeconds <= 0) {
            return "N/A"; // Keine gültigen Daten
        }

        if (sportType == SPORT_RUNNING) {
            // Pace (Minuten pro Kilometer)
            return formatPace(distanceMeters, totalTimerTimeSeconds);
        } else if (sportType == SPORT_CYCLING) {
            // Geschwindigkeit (km/h)
            return formatSpeed(distanceMeters, totalTimerTimeSeconds);
        } else {
            return "Unbekannter Sport";
        }
    }

    /**
     * Berechnet und formatiert die Pace in Minuten pro Kilometer.
     * Formate: "MM:SS /km"
     *
     * @param distanceMeters Die Distanz in Metern.
     * @param totalTimerTimeSeconds Die Zeit in Sekunden.
     * @return Die formatierte Pace.
     */
    public String formatPace(double distanceMeters, double totalTimerTimeSeconds) {
        if (distanceMeters <= 0 || totalTimerTimeSeconds <= 0) {
            return "00:00"; // Oder "N/A"
        }

        // Zeit pro Meter (Sekunden pro Meter)
        double secondsPerMeter = totalTimerTimeSeconds / distanceMeters;
        // Zeit pro Kilometer (Sekunden pro Kilometer)
        double secondsPerKm = secondsPerMeter * 1000.0;

        long minutes = (long) (secondsPerKm / 60);
        long seconds = (long) (secondsPerKm % 60);

        return String.format("%01d:%02d", minutes, seconds);
    }

    /**
     * Berechnet und formatiert die Geschwindigkeit in Kilometern pro Stunde.
     * Formate: "XX.X km/h" (eine Nachkommastelle)
     *
     * @param distanceMeters Die Distanz in Metern.
     * @param totalTimerTimeSeconds Die Zeit in Sekunden.
     * @return Die formatierte Geschwindigkeit.
     */
    public String formatSpeed(double distanceMeters, double totalTimerTimeSeconds) {
        if (distanceMeters <= 0 || totalTimerTimeSeconds <= 0) {
            return "0.0 km/h"; // Oder "N/A"
        }

        // Geschwindigkeit in Metern pro Sekunde
        double metersPerSecond = distanceMeters / totalTimerTimeSeconds;
        // Umrechnung in Kilometer pro Stunde (m/s * 3.6 = km/h)
        double kmPerHour = metersPerSecond * 3.6;

        // Runden auf eine Nachkommastelle und Formatierung mit Komma
        return String.format(java.util.Locale.GERMAN, "%.1f km/h", kmPerHour);
    }
}
