package com.runnertracker.service;

import com.runnertracker.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

// Angenommen, du hast ein UserRepository und eine UserEntity
import com.runnertracker.repository.UserRepository;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        debugPrincipalType();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        List<GrantedAuthority> authorities = user.getRoles().stream() // Annahme: getRoles() gibt List<String> oder List<RoleEntity> zurück
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Oder wie deine Rollen heißen
                .collect(Collectors.toList());
        // Konvertiere deine UserEntity in CustomUserDetails
        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(), // Stelle sicher, dass das Passwort verschlüsselt ist
                authorities, // Oder lade die tatsächlichen Rollen/Authorities des Benutzers
                user.getId() // Deine tatsächliche ID-Property in UserEntity
        );
    }
//    @Override
//    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//// Beispiel: Benutzerrollen aus der Datenbank abrufen (angenommen, du hast eine Methode dafür)
////        List<String> userRoles = userRepository.findRolesByUsername((username);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        // Konvertiere die Rollen in GrantedAuthority-Objekte
////        List<GrantedAuthority> grantedAuthorities = userRoles.stream()
////                .map(SimpleGrantedAuthority::new)
////                .collect(Collectors.toList());
//        // Konvertiere deine UserEntity in CustomUserDetails
//        return new CustomUserDetails(
//                user.getId(), // ID muss als erster Parameter übergeben werden
//                user.getUsername(),
//                user.getPassword(), // Stelle sicher, dass das Passwort verschlüsselt ist
//                authorities // Oder lade die tatsächlichen Rollen/Authorities des Benutzers
//        );
//    }
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
