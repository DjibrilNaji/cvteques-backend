package com.cvteques.controller;

import com.cvteques.dto.CvListRow;
import com.cvteques.service.CVService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/cv")
public class CvController {

  @Autowired private CVService CVService;

  @PostMapping("/upload/{intervenantId}")
  public ResponseEntity<Map<String, String>> uploadCV(
      @RequestParam("file") MultipartFile file, @PathVariable Long intervenantId) {
    try {
      return CVService.saveCV(file, intervenantId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("customMessage", "Erreur lors de l'upload"));
    }
  }

  @DeleteMapping("/{intervenantId}")
  public ResponseEntity<Map<String, String>> deleteCv(@PathVariable Long intervenantId) {
    return CVService.deleteCV(intervenantId);
  }

  @GetMapping
  public ResponseEntity<List<CvListRow>> listCvs() {
    List<CvListRow> result = CVService.getAllCVs();
    return ResponseEntity.ok(result);
  }
}
