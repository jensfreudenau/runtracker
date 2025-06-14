package com.runtracker.service;

import com.runtracker.exceptionHandler.FitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FitFileServiceIntegrationTest {
//
//    @Autowired
//    private FitFileService fitFileService;
//
//    private static final String VALID_FIT_FILE_PATH_WITH_3_LAPS = "test-data/Fahrt_am_Nachmittag.fit";
//    private static final String VALID_FIT_FILE_PATH_NO_AVG_SPEED = "test-data/ohne_avg.fit";
//    private static final String VALID_FIT_FILE_PATH_TRUNCATED = "test-data/exception.fit";
//
//    @BeforeEach
//    void setUp() {}
//
//    @Test
//    @DisplayName("Sollte die Durchschnittsgeschwindigkeit aus einer gültigen FIT-Datei lesen, wenn avg_speed vorhanden ist")
//    void shouldReadAverageSpeedFromValidFitFileWithAvgSpeed() throws Exception {
//        try (InputStream inputStream = new ClassPathResource(VALID_FIT_FILE_PATH_NO_AVG_SPEED).getInputStream()) {
//            double averageSpeed = fitFileService.getAvgSpeedKmPerHour(inputStream);
//            assertEquals(21.22, averageSpeed, 0.01, "Die Durchschnittsgeschwindigkeit sollte korrekt ausgelesen werden.");
//        }
//    }
//
//    @Test
//    @DisplayName("Sollte die Durchschnittsgeschwindigkeit aus einer gültigen FIT-Datei berechnen, wenn avg_speed fehlt")
//    void shouldCalculateAverageSpeedIfAvgSpeedIsMissing() throws Exception {
//        try (InputStream inputStream = new ClassPathResource(VALID_FIT_FILE_PATH_NO_AVG_SPEED).getInputStream()) {
//            double totalDistance = fitFileService.getTotalDistance(inputStream);
//            assertEquals(8.043419921875, totalDistance, 0.01, "Die Durchschnittsgeschwindigkeit sollte korrekt berechnet werden.");
//        }
//    }
//
//    @Test
//    @DisplayName("Sollte die gesamte Länge ausgeben")
//    void shouldDisplayTotalDistance() throws Exception {
//        try (InputStream inputStream = new ClassPathResource(VALID_FIT_FILE_PATH_NO_AVG_SPEED).getInputStream()) {
//            double totalTimerTime = fitFileService.getTotalTimerTime(inputStream);
//            assertEquals(1364.3719482421875, totalTimerTime, 0.01, "Die Gesamtzeit sollte korrekt angezeigt werden.");
//        }
//    }
//
//    @Test
//    @DisplayName("Sollte -1.0 zurückgeben, wenn keine relevante Geschwindigkeitsdaten gefunden werden")
//    void shouldReturnNegativeOneIfNoSpeedDataFound() throws Exception {
//        try (InputStream inputStream = new ClassPathResource(VALID_FIT_FILE_PATH_WITH_3_LAPS).getInputStream()) {
//            double averageSpeed = fitFileService.getAvgSpeedKmPerHour(inputStream);
//            assertEquals(28.63, averageSpeed, "Sollte -1.0 zurückgeben, wenn keine Geschwindigkeitsdaten gefunden werden.");
//        }
//    }
//
//    @Test
//    @DisplayName("Sollte FitException werfen für eine fehlerhafte FIT-Datei")
//    void shouldThrowFitExceptionForMalformedFile() {
//        // Eine bewusst beschädigte oder unvollständige .fit-Datei
//        try (InputStream inputStream = new ClassPathResource(VALID_FIT_FILE_PATH_TRUNCATED).getInputStream()) {
//            // Erwarte, dass die Methode eine FitException wirft
//            assertThrows(FitException.class, () -> fitFileService.getAvgSpeedKmPerHour(inputStream),
//                    "Sollte eine FitException werfen, wenn die Datei fehlerhaft ist.");
//        } catch (IOException e) {
//            fail("Failed to load malformed test file: " + e.getMessage());
//        }
//    }

//    @Test
//    @DisplayName("Sollte IOException werfen, wenn InputStream ungültig ist")
//    void shouldThrowIOExceptionForInvalidInputStream() {
//        // Ein geschlossener InputStream oder ein null InputStream
//        // (decode.read() wirft IOException bei einem geschlossenen Stream)
//        InputStream closedInputStream = new InputStream() {
//            @Override
//            public int read() throws IOException {
//                throw new IOException("Stream closed");
//            }
//        };
//
//
//        assertThrows(IOException.class, () -> fitFileService.getAvgSpeedKmPerHour(closedInputStream),
//                "Sollte IOException werfen, wenn der InputStream ungültig ist.");
//    }
}
