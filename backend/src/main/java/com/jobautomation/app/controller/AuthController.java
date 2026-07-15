package com.jobautomation.app.controller;

import com.jobautomation.app.dto.AuthRequest;
import com.jobautomation.app.dto.AuthResponse;
import com.jobautomation.app.model.AppUser;
import com.jobautomation.app.repository.AppUserRepository;
import com.jobautomation.app.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
@RequiredArgsConstructor @Tag(name = "Auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final AppUserRepository userRepo;
    private final PasswordEncoder encoder;

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        return ResponseEntity.ok(new AuthResponse(jwtUtils.generate(auth), req.getUsername()));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user (first-time setup)")
    public ResponseEntity<String> register(@RequestBody AuthRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent())
            return ResponseEntity.badRequest().body("Username already exists");
        userRepo.save(AppUser.builder()
            .username(req.getUsername())
            .password(encoder.encode(req.getPassword()))
            .role("ROLE_USER").build());
        return ResponseEntity.ok("User registered");
    }
}
