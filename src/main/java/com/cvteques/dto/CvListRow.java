package com.cvteques.dto;

import java.time.LocalDateTime;

public record CvListRow(
    Long id,
    String firstname,
    String lastname,
    String title,
    LocalDateTime updatedAt,
    String url) {}
