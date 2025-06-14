package com.runtracker.fit;

import com.garmin.fit.*;
import com.runtracker.dto.LapDTO;
import com.runtracker.dto.SummaryDTO;
import com.runtracker.exceptionHandler.FitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FitFileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FitFileService.class);

    public FitFileService() {
    }

    public List<LapDTO> getLaps(InputStream inputStream) throws FitException {
        try {
            Decode decode = new Decode();
            MesgBroadcaster broadcaster = new MesgBroadcaster(decode);
            LapListener lapListener = new LapListener();
            broadcaster.addListener(lapListener);
            decode.read(inputStream, broadcaster, broadcaster);
            return lapListener.getLaps(); // jetzt Liste statt nur eine Lap
        } catch (Exception e) {
            throw new FitException("Fehler beim Parsen der FIT-Datei");
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
            throw new FitException("Fehler beim Parsen der FIT-Datei");
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
//                String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
//                lap.setDistance(lapDTO.getDouble(FitField.TOTAL_DISTANCE));
//                LOGGER.warn("Names: {}", cap);
//                LOGGER.warn("Names: {}", name.toUpperCase());

//                LOGGER.warn("set" + cap + "(lapDTO.getDouble(FitField." +name.toUpperCase() + "))");
//                LOGGER.warn("Value: {}", value);
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
//                LOGGER.warn("Name: {}", name.toUpperCase());
//                LOGGER.warn("Value: {}", value);
//                LOGGER.warn("Wert: {}, Datentyp (Einfacher Name): {}", value, value.getClass().getSimpleName());
//                if ("start_time".equals(name) || "timestamp".equals(name)) {
//                    Object values = mesg.getTimestamp();
//                    FitField.fromFitName(name).ifPresent(f -> summary.set(f, values));
//                }

//                FitField.fromFitName(name).ifPresentOrElse(
//                        vae -> System.out.println("Found: " + vae),
//                        () -> LOGGER.warn("Not Found: {}", name)
//                );
                FitField.fromFitName(name).ifPresent(fe -> summaryDTO.set(fe, value));
            }
        }

        public SummaryDTO toSummary() {
            return summaryDTO;
        }
    }
}

