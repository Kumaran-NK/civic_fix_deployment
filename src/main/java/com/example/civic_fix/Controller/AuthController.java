package com.example.civic_fix.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.civic_fix.dto.LoginRequest;
import com.example.civic_fix.dto.LoginResponse;
import com.example.civic_fix.dto.UserRequestDTO;
import com.example.civic_fix.dto.UserResponseDTO;
import com.example.civic_fix.security.JwtUtil;
import com.example.civic_fix.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody UserRequestDTO request) {
        UserResponseDTO user = userService.registerUser(request);
        String token = jwtUtil.generateToken(user.getUserId(), user.getRole());
        return ResponseEntity.ok(new LoginResponse(token, user.getUserId(), user.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        UserResponseDTO user = userService.login(request);
        String token = jwtUtil.generateToken(user.getUserId(), user.getRole());
        return ResponseEntity.ok(new LoginResponse(token, user.getUserId(), user.getRole()));
    }
}