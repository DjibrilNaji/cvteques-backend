package com.cvteques.dto;

import java.time.LocalDateTime;

public class CvDto {
  private Long id;
  private String title;
  private String url;
  private Long intervenantId;
  private LocalDateTime updatedAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Long getIntervenantId() {
    return intervenantId;
  }

  public void setIntervenantId(Long intervenantId) {
    this.intervenantId = intervenantId;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
