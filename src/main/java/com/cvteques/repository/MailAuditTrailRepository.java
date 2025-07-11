package com.cvteques.repository;

import com.cvteques.entity.MailAuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailAuditTrailRepository extends JpaRepository<MailAuditTrail, Long> {}
