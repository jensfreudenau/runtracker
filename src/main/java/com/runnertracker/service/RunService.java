package com.runnertracker.service;

import com.runnertracker.model.Lap;
import com.runnertracker.model.Run;
import com.runnertracker.model.User;
import com.runnertracker.repository.LapRepository;
import com.runnertracker.repository.RunRepository;
import com.runnertracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RunService {
    private final RunRepository runRepository;
    private final UserRepository userRepository;
    private final LapRepository lapRepository; // Injizieren

    public RunService(RunRepository runRepository, UserRepository userRepository, LapRepository lapRepository) {
        this.runRepository = runRepository;
        this.userRepository = userRepository;
        this.lapRepository = lapRepository;
    }

    public List<Run> findRunsByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.map(runRepository::findByUser).orElse(List.of());
    }
    // Neue Methode: Lauf speichern oder aktualisieren
    @Transactional
    public void saveRun(Run run, String username) {
        // Sicherstellen, dass der Lauf dem eingeloggten Benutzer zugeordnet wird
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Benutzer nicht gefunden: " + username));
        run.setUser(user);
        // Sicherstellen, dass jede Lap ihrem Run zugeordnet ist, bevor gespeichert wird
        if (run.getLaps() != null) {
            run.getLaps().forEach(lap -> lap.setRun(run));
        }
        runRepository.save(run);
    }

    // Neue Methode: Lauf anhand der ID finden
    public Optional<Run> findRunByIdAndUsername(Long id, String username) {
        // Stellen Sie sicher, dass nur der eigene Lauf bearbeitet/gelöscht werden kann
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return runRepository.findByIdAndUser(id, userOptional.get());
        }
        return Optional.empty();
    }

    // Neue Methode: Lauf löschen
    @Transactional
    public void deleteRun(Long id, String username) {
        Optional<Run> runOptional = findRunByIdAndUsername(id, username);
        if (runOptional.isPresent()) {
            runRepository.delete(runOptional.get());
        } else {
            throw new IllegalArgumentException("Lauf mit ID " + id + " nicht gefunden oder gehört nicht zum Benutzer " + username);
        }
    }
    @Transactional
    public Lap addLapToRun(Long runId, Lap lap, String username) {
        Run run = findRunByIdAndUsername(runId, username)
                .orElseThrow(() -> new IllegalArgumentException("Lauf nicht gefunden oder nicht berechtigt."));

        lap.setRun(run);
        // Optional: Setze die Rundenummer, falls nicht vom Frontend übergeben
        if (lap.getLapNumber() == 0) {
            lap.setLapNumber(run.getLaps().size() + 1);
        }

        run.getLaps().add(lap); // Füge die Runde zur Liste des Laufs hinzu (bidirektional)
        runRepository.save(run); // Speichert den Lauf und kaskadiert die Runde
        return lap; // Oder return lapRepository.save(lap); wenn lapRepository für sichere Speicherung verwendet werden soll.
    }

    // Beispiel: Methode zum Abrufen der Runden eines Laufs
    public List<Lap> getLapsForRun(Long runId, String username) {
        Run run = findRunByIdAndUsername(runId, username)
                .orElseThrow(() -> new IllegalArgumentException("Lauf nicht gefunden oder nicht berechtigt."));
        return lapRepository.findByRunOrderByLapNumberAsc(run);
    }
    public Run findById(Long id) {
        return runRepository.findById(id).orElseThrow();
    }

    public Run save(Run run) {
        return runRepository.save(run);
    }

    public void deleteById(Long id) {
        runRepository.deleteById(id);
    }
}
