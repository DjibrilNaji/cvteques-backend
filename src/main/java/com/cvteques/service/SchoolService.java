package com.cvteques.service;

import com.cvteques.entity.School;
import com.cvteques.repository.SchoolRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolService {

  @Autowired private SchoolRepository schoolRepository;

  public List<School> getAllSchools() {
    return schoolRepository.findAll();
  }
}
