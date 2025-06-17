package com.runtracker.service;

import com.runtracker.exceptionHandler.FitException;
import com.runtracker.fit.FitFileService;
import com.runtracker.model.Lap;
import com.runtracker.model.Run;
import com.runtracker.model.User;
import com.runtracker.repository.LapRepository;
import com.runtracker.repository.RunRepository;
import com.runtracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class RunService {
    private final RunRepository runRepository;
    private final UserRepository userRepository;
    private final LapRepository lapRepository;
    private final FitFileService fitFileService;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
    public RunService(RunRepository runRepository, UserRepository userRepository, LapRepository lapRepository) {
        this.runRepository = runRepository;
        this.userRepository = userRepository;
        this.lapRepository = lapRepository;
        this.fitFileService = new FitFileService();
    }

    public List<Run> findRunsByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.map(runRepository::findByUser).orElse(List.of());
    }
    @Transactional
    public Long saveRun(Run run, String username) {
        // Sicherstellen, dass der Lauf dem eingeloggten Benutzer zugeordnet wird
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Benutzer nicht gefunden: " + username));
        run.setUser(user);
        // Sicherstellen, dass jede Lap ihrem Run zugeordnet ist, bevor gespeichert wird
        if (run.getLaps() != null) {
            run.getLaps().forEach(lap -> lap.setRun(run));
        }
        runRepository.save(run);
        return run.getId();
    }

    public Optional<Run> findRunByIdAndUsername(Long id, String username) {
        // Stellen Sie sicher, dass nur der eigene Lauf bearbeitet/gelöscht werden kann
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return runRepository.findByIdAndUser(id, userOptional.get());
        }
        return Optional.empty();
    }

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
    public void addLapToRun(Long runId, Lap lap, String username) {
        Run run = findRunByIdAndUsername(runId, username)
                .orElseThrow(() -> new IllegalArgumentException("Lauf nicht gefunden oder nicht berechtigt."));
        lap.setRun(run);
        // Optional: Setze die Rundenummer, falls nicht vom Frontend übergeben
        if (lap.getLapNumber() == 0) {
            lap.setLapNumber(run.getLaps().size() + 1);
        }
        run.getLaps().add(lap); // Füge die Runde zur Liste des Laufs hinzu (bidirektional)
        runRepository.save(run); // Speichert den Lauf und kaskadiert die Runde
    }

    // Beispiel: Methode zum Abrufen der Runden eines Laufs
    public List<Lap> getLapsForRun(Long runId, String username) {
        Run run = findRunByIdAndUsername(runId, username)
                .orElseThrow(() -> new IllegalArgumentException("Lauf nicht gefunden oder nicht berechtigt."));
        return lapRepository.findByRunOrderByLapNumberAsc(run);
    }
    public Optional<Run> findById(Long id) {
        return runRepository.findById(id);
    }

    public Run save(Run run) {
        return runRepository.save(run);
    }

    public void deleteById(Long id) {
        runRepository.deleteById(id);
    }

    public List<FitFileService.HeartRatePoint> getHeartRateSeries(@PathVariable Long id) throws FitException {
        Run run = findById(id).orElseThrow();
        Path path = Paths.get(UPLOAD_DIR + run.getUser().getId() + "/activities/" + run.getName());
        try (InputStream fitStream = Files.newInputStream(path)) {
            return fitFileService.extractHeartRateSeries(fitStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<FitFileService.GpsPoint> getGpsSeries(Long id) {
        Run run = findById(id).orElseThrow();
        String filePath = UPLOAD_DIR + run.getUser().getId() + "/activities/" + run.getName();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            return fitFileService.extractGpsSeries(inputStream);
        } catch (IOException | FitException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FitFileService.PcoPoint> getPCOData(Long id) {
        Run run = findById(id).orElseThrow();
        String path = UPLOAD_DIR + run.getUser().getId() + "/activities/" + run.getName();
        try (InputStream fitStream = new FileInputStream(path)) {
            return fitFileService.extractPcoSeries(fitStream);
        } catch (IOException | FitException e) {
            throw new RuntimeException(e);
        }
    }
}
