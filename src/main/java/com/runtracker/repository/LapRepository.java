package com.runtracker.repository;
import com.runtracker.model.Lap;
import com.runtracker.model.Run; // Wenn du nach Läufen filtern möchtest
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LapRepository extends JpaRepository<Lap, Long> {
    // Optional: Finde alle Runden für einen bestimmten Lauf
    List<Lap> findByRunOrderByLapNumberAsc(Run run);
    Optional<Lap> findByRunIdAndStartTime(Long runId, double start_time);
    Optional<Lap> findByRunIdAndLapNumber(Long runId, int lapNumber);

//    Optional<Lap> findByRunIdAndIntensity(Long runId, Double intensity);
    // Optional: Finde eine spezifische Runde nach Lauf und Rundenummer
//     Optional<Lap> findByRunAndLapNumber(Run run, int lapNumber);
}
