package com.runtracker.service;

import com.runtracker.model.Lap;
import com.runtracker.repository.LapRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LapService {
    private final LapRepository lapRepository;

    public LapService(LapRepository lapRepository) {
        this.lapRepository = lapRepository;
    }

    public int saveLap(Lap lap) {
        // Sicherstellen, dass jede Lap ihrem Run zugeordnet ist, bevor gespeichert wird

        lapRepository.save(lap);
        return lap.getId();
    }
    public Optional<Lap> findByRunIdAndStartTime(Long id, Integer start_time) {
        return lapRepository.findByRunIdAndStartTime(id, (double)start_time);
    }
    public Optional<Lap> findByRunIdAndLapNumber(Long id, Integer labNumber) {
        return lapRepository.findByRunIdAndLapNumber(id, labNumber);
    }

    public Optional<Lap> findById(Long id) {
        return lapRepository.findById(id);
    }
    public Lap save(Lap lap) {
        return lapRepository.save(lap);
    }

//    public Optional<Lap> findByRunIdAndLapNumber(Long runId, int lapNumber) {
//        return lapRepository.findByRunIdAndLapNumber(runId, lapNumber);
//    }
}
