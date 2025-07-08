package com.cvteques.service;

import com.cvteques.dto.LoginRequest;
import com.cvteques.dto.RegisterRequest;
import com.cvteques.entity.School;
import com.cvteques.entity.User;
import com.cvteques.repository.SchoolRepository;
import com.cvteques.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private SchoolRepository schoolRepository;

  private final PasswordEncoder passwordEncoder;

  public UserService(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public ResponseEntity<Map<String, String>> register(RegisterRequest request) {
    if (userRepository.findByEmail(request.email()).isPresent()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Map.of("customMessage", "Email déjà utilisé."));
    }

    School school = null;

    if ("SCHOOL".equals(request.role().name()) && request.schoolId() != null) {
      Optional<School> optionalSchool = schoolRepository.findById(request.schoolId());

      if (optionalSchool.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("customMessage", "L'école spécifiée est introuvable."));
      }

      school = optionalSchool.get();
    }

    User user = new User();
    user.setFirstname(request.firstname());
    user.setLastname(request.lastname());
    user.setEmail(request.email());
    user.setPasswordHash(passwordEncoder.encode(request.password()));
    user.setRole(request.role());
    user.setSchool(school);

    try {
      userRepository.save(user);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("customMessage", "Erreur lors de l'enregistrement de l'utilisateur."));
    }

    return ResponseEntity.ok(Map.of("customMessage", "Inscription réussie."));
  }

  public ResponseEntity<Map<String, String>> login(LoginRequest request) {
    Optional<User> optionalUser = userRepository.findByEmail(request.email());

    if (optionalUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Map.of("customMessage", "Utilisateur non trouvé."));
    }

    User user = optionalUser.get();

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Map.of("customMessage", "Mot de passe incorrect."));
    }

    return ResponseEntity.ok(Map.of("customMessage", "Connexion réussie."));
  }
}
