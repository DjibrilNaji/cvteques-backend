package com.cvteques.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IntervenantCVService {

  private final String cvDirectory = "CVs/";

  public String saveCV(MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      throw new IOException("Le fichier est vide");
    }

    File directory = new File(cvDirectory);
    if (!directory.exists()) {
      directory.mkdirs();
    }

    Path filePath = Paths.get(cvDirectory + file.getOriginalFilename());
    Files.write(filePath, file.getBytes());

    return "CV enregistré avec succès sous : " + filePath.toString();
  }
}
