package com.runnertracker.repository;

import com.runnertracker.model.Run;
import com.runnertracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RunRepository extends JpaRepository<Run, Long> {
    List<Run> findByUser(User user);
    Optional<Run> findByIdAndUser(Long id, User user); // Neu: Finden nach ID und zugehörigem User
}
