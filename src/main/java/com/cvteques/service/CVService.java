package com.cvteques.service;

import com.cvteques.dto.CvListRow;
import com.cvteques.entity.Cv;
import com.cvteques.entity.MailAuditTrail;
import com.cvteques.entity.MailEventType;
import com.cvteques.entity.User;
import com.cvteques.repository.CvRepository;
import com.cvteques.repository.MailAuditTrailRepository;
import com.cvteques.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.io.File;
import java.nio.file.*;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
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

  @Autowired private MailAuditTrailRepository mailAuditTrailRepository;

  @Transactional
  public ResponseEntity<Map<String, String>> saveCV(MultipartFile file, Long intervenantId) {
    try {
      if (file.isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("customMessage", "Le fichier est vide"));
      }
      if (file.getSize() > 5 * 1024 * 1024) {
        return ResponseEntity.badRequest()
            .body(Map.of("customMessage", "La taille maximale autorisée est de 5 Mo."));
      }
      if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
        return ResponseEntity.badRequest()
            .body(Map.of("customMessage", "Seuls les fichiers PDF sont autorisés."));
      }

      Optional<User> utilisateur = userRepository.findById(intervenantId);

      if (utilisateur.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("customMessage", "Utilisateur non trouvé"));
      }

      var user = utilisateur.get();

      File directory = new File(cvDirectory);
      if (!directory.exists()) directory.mkdirs();

      String sanitizeFilenam = sanitizeFilename(file.getOriginalFilename());
      Path filePath = Paths.get(cvDirectory, sanitizeFilenam);
      Files.write(filePath, file.getBytes());

      String publicUrl = "cvs/" + sanitizeFilenam;

      Cv cv = cvRepository.findByIntervenantId(intervenantId).orElseGet(Cv::new);
      cv.setIntervenantId(intervenantId);
      cv.setTitle(file.getOriginalFilename());
      cv.setUrl(publicUrl);
      cv.setUpdatedAt(LocalDateTime.now());
      cvRepository.save(cv);

      emailService.sendCVUploadEmail(user.getEmail(), user.getFirstname(), filePath.toFile());

      MailAuditTrail entry = new MailAuditTrail();
      entry.setEventType(MailEventType.CV_SAVED);
      entry.setUserId(user.getId());
      mailAuditTrailRepository.save(entry);

      return ResponseEntity.ok()
          .body(
              Map.of(
                  "customMessage",
                  "CV enregistré avec succès sous le nom : " + file.getOriginalFilename()));

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
        Files.deleteIfExists(Paths.get("uploads", existingCv.getUrl()));
      } catch (Exception ioe) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                Map.of(
                    "customMessage",
                    "Erreur lors de la suppression du fichier: " + ioe.getMessage()));
      }

      cvRepository.delete(existingCv);

      return ResponseEntity.ok().body(Map.of("customMessage", "CV supprimé avec succès"));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("customMessage", "Une erreur est survenue lors de la suppression du CV"));
    }
  }

  public List<CvListRow> getAllCVs() {
    return cvRepository.findAllWithUser();
  }

  private String sanitizeFilename(String originalName) {
    String baseName = originalName.replaceAll("\\.[^.]+$", ""); // sans extension
    String extension = originalName.replaceAll("^.+\\.", ""); // extension

    String normalized =
        Normalizer.normalize(baseName, Normalizer.Form.NFD)
            .replaceAll("[\\p{InCombiningDiacriticalMarks}]", ""); // suppr. accents

    String clean = normalized.replaceAll("[^a-zA-Z0-9-_]", "-"); // alphanum only

    return clean + "-" + System.currentTimeMillis() + "." + extension;
  }
}
