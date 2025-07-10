package com.cvteques.dto;

import com.cvteques.entity.Role;
import com.cvteques.entity.School;

public class UserDto {
  private Long id;
  private String firstname;
  private String lastname;
  private String email;
  private Role role;
  private School school;
  private CvDto cv;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public School getSchool() {
    return school;
  }

  public void setSchool(School school) {
    this.school = school;
  }

  public CvDto getCv() {
    return cv;
  }

  public void setCv(CvDto cv) {
    this.cv = cv;
  }
}
