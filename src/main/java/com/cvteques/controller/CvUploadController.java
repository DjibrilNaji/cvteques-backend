package ton.packagename.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cv")
public class CvUploadController {

    private static final String UPLOAD_DIR = "CVs/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fichier vide");
        }

        if (!"application/pdf".equals(file.getContentType())) {
            return ResponseEntity.badRequest().body("Seuls les fichiers PDF sont autorisés");
        }

        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path filepath = Paths.get(UPLOAD_DIR, filename);

            Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("Fichier uploadé avec succès : " + filename);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur serveur lors de l'upload");
        }
    }
}
