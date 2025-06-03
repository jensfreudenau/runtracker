package com.runnertracker.repository;
import com.runnertracker.model.Lap;
import com.runnertracker.model.Run; // Wenn du nach Läufen filtern möchtest
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LapRepository extends JpaRepository<Lap, Long> {
    // Optional: Finde alle Runden für einen bestimmten Lauf
    List<Lap> findByRunOrderByLapNumberAsc(Run run);

    // Optional: Finde eine spezifische Runde nach Lauf und Rundenummer
    // Optional<Lap> findByRunAndLapNumber(Run run, int lapNumber);
}
