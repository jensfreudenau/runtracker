package com.runnertracker.service;

import com.runnertracker.model.Profile;
import com.runnertracker.model.User;
import com.runnertracker.repository.ProfileRepository;
import com.runnertracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public Optional<Profile> findProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    @Transactional
    public Profile saveProfile(Profile profile, String username) {
        // Sicherstellen, dass der Lauf dem eingeloggten Benutzer zugeordnet wird
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Profil nicht gefunden: " + username));
        profile.setUser(user);
        return profileRepository.save(profile);
    }

    @Transactional
    public void deleteProfile(Long userId) {
        Optional<Profile> profileOptional = profileRepository.findByUserId(userId);
        if (profileOptional.isPresent()) {
            profileRepository.delete(profileOptional.get());
        } else {
            throw new IllegalArgumentException("Profil nicht gefunden oder gehört nicht zum Benutzer " + userId);
        }
    }
}
