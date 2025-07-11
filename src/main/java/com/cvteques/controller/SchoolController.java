package com.cvteques.controller;

import com.cvteques.entity.School;
import com.cvteques.service.SchoolService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {

  @Autowired private SchoolService schoolService;

  @GetMapping
  public ResponseEntity<List<School>> getAllSchools() {
    List<School> schools = schoolService.getAllSchools();
    return ResponseEntity.ok(schools);
  }
}
