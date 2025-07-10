package com.cvteques.service;

import com.cvteques.entity.Cv;
import com.cvteques.entity.User;
import com.cvteques.repository.CvRepository;
import com.cvteques.repository.UserRepository;
import java.io.File;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CVService {

  @Autowired private CvRepository cvRepository;

  @Autowired private EmailService emailService;

  @Autowired private UserRepository userRepository;

  private final String cvDirectory = "uploads/cvs/";

  public ResponseEntity<Map<String, String>> saveCV(MultipartFile file, Long intervenantId) {
    try {
      if (file.isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("customMessage", "Le fichier est vide"));
      }

      Optional<User> utilisateurOpt = userRepository.findById(intervenantId);
      if (utilisateurOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("customMessage", "Utilisateur non trouvé pour l'ID: " + intervenantId));
      }

      User utilisateur = utilisateurOpt.get();
      File directory = new File(cvDirectory);
      if (!directory.exists()) {
        directory.mkdirs();
      }

      String filename = file.getOriginalFilename();
      Path filePath = Paths.get(cvDirectory + filename);
      Files.write(filePath, file.getBytes());

      Optional<Cv> existingOpt = cvRepository.findByIntervenantId(intervenantId);

      if (existingOpt.isPresent()) {
        Cv existingCv = existingOpt.get();
        existingCv.setTitle(filename);
        existingCv.setUrl(filePath.toString());
        existingCv.setUpdatedAt(LocalDateTime.now());
        cvRepository.save(existingCv);
        return ResponseEntity.ok()
            .body(Map.of("customMessage", "CV mis à jour avec succès sous le nom: " + filename));
      }

      Cv cv = new Cv();
      cv.setIntervenantId(intervenantId);
      cv.setTitle(filename);
      cv.setUrl(filePath.toString());
      cv.setUpdatedAt(LocalDateTime.now());

      cvRepository.save(cv);

      emailService.sendCVUploadEmail(
          utilisateur.getEmail(), utilisateur.getFirstname(), filePath.toFile());

      return ResponseEntity.ok()
          .body(Map.of("customMessage", "CV enregistré avec succès sous le nom: " + filename));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("customMessage", "Une erreur est survenue lors de l'enregistrement du CV"));
    }
  }

  public ResponseEntity<Map<String, String>> deleteCV(Long intervenantId) {
    try {
      Optional<Cv> existingOpt = cvRepository.findByIntervenantId(intervenantId);

      if (existingOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                Map.of(
                    "customMessage",
                    "Aucun CV n’a été trouvé pour l’intervenant " + intervenantId));
      }

      Cv existingCv = existingOpt.get();

      try {
        Files.deleteIfExists(Paths.get(existingCv.getUrl()));
      } catch (Exception ioe) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                Map.of(
                    "customMessage",
                    "Erreur lors de la suppression du fichier: " + ioe.getMessage()));
      }

      cvRepository.delete(existingCv);

      return ResponseEntity.ok()
          .body(
              Map.of(
                  "customMessage", "CV supprimé avec succès pour l’intervenant " + intervenantId));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("customMessage", "Une erreur est survenue lors de la suppression du CV"));
    }
  }
}
