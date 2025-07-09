package com.cvteques.controller;

import com.cvteques.service.IntervenantCVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/cv")
public class IntervenantCVController {

  @Autowired private IntervenantCVService intervenantCVService;

  @PostMapping("/upload")
  public ResponseEntity<String> uploadCV(@RequestParam("file") MultipartFile file) {
    try {
      String result = intervenantCVService.saveCV(file);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erreur lors de l'upload : " + e.getMessage());
    }
  }
}
