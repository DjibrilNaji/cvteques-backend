package com.cvteques.controller;

import com.cvteques.dto.LoginRequest;
import com.cvteques.dto.RegisterRequest;
import com.cvteques.service.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
  @Autowired private UserService userService;

  @PostMapping("/register")
  public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
    return userService.register(request);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
    return userService.login(request);
  }

  @GetMapping("/{email}")
  public ResponseEntity<Map<String, Object>> getUser(@PathVariable String email) {
    return userService.getUserDtoByEmail(email);
  }
}
