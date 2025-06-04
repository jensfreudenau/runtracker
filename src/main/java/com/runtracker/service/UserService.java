package com.runtracker.service;

import com.runtracker.model.Role;
import com.runtracker.model.User;
import com.runtracker.repository.RoleRepository;
import com.runtracker.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Benutzername bereits vergeben.");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Role userRole = roleRepository.findByName("USER").orElseGet(() -> {
            Role newUserRole = new Role();
            newUserRole.setName("USER");
            return roleRepository.save(newUserRole);
        });
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

    public void displayUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                System.out.println("Rolle: " + authority.getAuthority());
            }
        } else {
            System.out.println("Benutzer ist nicht authentifiziert.");
        }
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        debugPrincipalType();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUserId();
        }
        return null; // Oder werfe eine Ausnahme, wenn kein Benutzer angemeldet ist oder der Typ nicht stimmt
    }
    // Neue Methode: Benutzer anhand der ID finden
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    // Neue Methode: Benutzer löschen
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    // Neue Methode: Benutzer speichern oder aktualisieren
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void debugPrincipalType() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            System.out.println("Actual principal type: " + principal.getClass().getName());

            if (principal instanceof CustomUserDetails) {
                System.out.println("Principal is CustomUserDetails!");
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                // ... dein Code
            } else if (principal instanceof UserDetails) { // Prüfe auf die generische UserDetails
                System.out.println("Principal is UserDetails (not CustomUserDetails)!");
                UserDetails userDetails = (UserDetails) principal;
                System.out.println("Username from UserDetails: " + userDetails.getUsername());
                // Wenn du hier bist, dann liefert dein UserDetailsService wahrscheinlich org.springframework.security.core.userdetails.User
            } else if (principal instanceof String) {
                System.out.println("Principal is a String (username): " + principal);
            } else {
                System.out.println("Principal is an unknown type.");
            }
        } else {
            System.out.println("Benutzer ist nicht authentifiziert.");
        }
    }
}
