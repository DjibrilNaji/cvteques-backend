package com.cvteques.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mail_audit_trail")
public class MailAuditTrail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "event_type", nullable = false, length = 50)
  @Enumerated(EnumType.STRING)
  private MailEventType eventType;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "sent_at", nullable = false)
  private LocalDateTime sentAt = LocalDateTime.now();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MailEventType getEventType() {
    return eventType;
  }

  public void setEventType(MailEventType eventType) {
    this.eventType = eventType;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public LocalDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(LocalDateTime sentAt) {
    this.sentAt = sentAt;
  }
}
