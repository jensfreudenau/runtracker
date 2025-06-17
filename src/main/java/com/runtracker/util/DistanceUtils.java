package com.runtracker.util;

import org.springframework.stereotype.Component;

@Component("distanceUtils") // Wichtig: Der Bean-Name für Thymeleaf
public class DistanceUtils {

    /**
     * Formatiert eine Distanz in Metern in "X km Y m" oder "Y m".
     *
     * @param meters Die Distanz in Metern.
     * @return Der formatierte String.
     */
    public String formatToKmAndMeters(double meters) {
        if (meters < 0) {
            return "Ungültige Distanz"; // Oder du könntest eine leere Zeichenkette zurückgeben
        }

        long km = (long) (meters / 1000);
        long remainingMeters = (long) (meters % 1000); // Nur die "ganzen" Meter nach dem Komma

        StringBuilder sb = new StringBuilder();
        if (km > 0) {
            sb.append(km).append(" km ");
        }
        sb.append(remainingMeters).append(" m");

        return sb.toString().trim();
    }

    /**
     * Formatiert eine Distanz in Metern in "X,Y km".
     *
     * @param meters Die Distanz in Metern.
     * @return Der formatierte String (z.B. "4,8 km").
     */
    public String formatToKilometersDecimal(double meters) {
        if (meters <= 0) {
            return "";
        }
        // Konvertiere Meter in Kilometer
        double kilometers = meters / 1000.0;

        // Verwende String.format für eine präzise Formatierung mit Komma als Dezimaltrennzeichen
        // %.1f bedeutet 1 Nachkommastelle, f ist für float/double
        // Locale.GERMAN sorgt dafür, dass das Komma als Dezimaltrennzeichen verwendet wird
        return String.format(java.util.Locale.GERMAN, "%.2f", kilometers);
    }

    /**
     * Formatiert eine Distanz in Metern in "X.Y km".
     * Dies ist eine alternative Methode für englische/internationale Dezimaltrennzeichen (Punkt).
     *
     * @param meters Die Distanz in Metern.
     * @return Der formatierte String (z.B. "4.8 km").
     */
    public String formatToKilometersDecimalEnglish(double meters) {
        if (meters < 0) {
            return "Ungültige Distanz";
        }

        double kilometers = meters / 1000.0;
        // Locale.US oder Locale.ROOT für Punkt als Dezimaltrennzeichen
        return String.format(java.util.Locale.US, "%.2f km", kilometers);
    }
}
