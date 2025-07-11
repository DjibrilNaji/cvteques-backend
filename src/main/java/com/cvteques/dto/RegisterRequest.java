package com.cvteques.dto;

import com.cvteques.entity.Role;

public record RegisterRequest(
    String firstname, String lastname, String email, String password, Role role, Long school) {}
