package com.runnertracker.repository;

import com.runnertracker.model.Profile;
import com.runnertracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfileRepository  extends JpaRepository<Profile, Long> {
    Profile findByUser(User user);
    Optional<Profile> findByIdAndUser(Long id, User user); // Neu: Finden nach ID und zugehörigem User

//    Optional<Profile> findByUsername(String username);

    Optional<Profile> findByUserId(Long userId);
}

