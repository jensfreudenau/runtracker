package com.runnertracker.service;

import com.runnertracker.model.Run;
import com.runnertracker.model.User;
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

    public RunService(RunRepository runRepository, UserRepository userRepository) {
        this.runRepository = runRepository;
        this.userRepository = userRepository;
    }

    public List<Run> findRunsByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.map(runRepository::findByUser).orElse(List.of());
    }
    // Neue Methode: Lauf speichern oder aktualisieren
    @Transactional
    public Run saveRun(Run run, String username) {
        // Sicherstellen, dass der Lauf dem eingeloggten Benutzer zugeordnet wird
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Benutzer nicht gefunden: " + username));
        run.setUser(user);
        return runRepository.save(run);
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
