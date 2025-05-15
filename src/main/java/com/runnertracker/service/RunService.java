package com.runnertracker.service;

import com.runnertracker.model.Run;
import com.runnertracker.repository.RunRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RunService {
    private final RunRepository runRepository;

    public RunService(RunRepository runRepository) {
        this.runRepository = runRepository;
    }

    public List<Run> findAll() {
        return runRepository.findAll();
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
