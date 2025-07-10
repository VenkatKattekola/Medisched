package org.example.medisched.web;

import org.example.medisched.dto.LoginRequest;
import org.example.medisched.dto.RegisterRequest; // Import RegisterRequest
import org.example.medisched.web.security.jwt.JwtUtils;
import org.example.medisched.service.security.services.UserDetailsImpl;
import org.example.medisched.service.AuthService;
import org.example.medisched.entity.User;
import org.example.medisched.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth") // Base mapping for all auth-related endpoints
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthService authService; // Your custom service for auth logic

    @Autowired
    private UserRepository userRepository;

    /**
     * Handles user login/authentication.
     * Authenticates the user and returns a JWT token upon successful login.
     *
     * @param loginRequest DTO containing username and password.
     * @return ResponseEntity with JwtResponse on success, or MessageResponse on failure.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        System.out.println("Attempting login for username: " + loginRequest.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            Long patientId = null;
            String userFullName = null;

            User userEntity = userRepository.findById(userDetails.getId()).orElse(null);
            if (userEntity != null) {
                userFullName = userEntity.getFullName();
            }

            if (roles.contains("ROLE_PATIENT")) {
                patientId = authService.findPatientIdByUserId(userDetails.getId());
            }

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles,
                    patientId,
                    userFullName
            ));

        } catch (Exception e) {
            System.err.println("Authentication failed for " + loginRequest.getUsername() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: Invalid username or password!"));
        }
    }

    /**
     * Handles new user registration.
     * Creates a new user account with the specified role.
     *
     * @param registerRequest DTO containing registration details.
     * @return ResponseEntity with a success message or error message.
     */
    @PostMapping("/register") // <--- NEW REGISTRATION ENDPOINT
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        System.out.println("Attempting registration for username: " + registerRequest.getUsername() + " with role: " + registerRequest.getRole());
        try {
            // Call your AuthService to handle the registration logic
            authService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("User registered successfully!", null)); // null for userId in MessageResponse
        } catch (Exception e) {
            System.err.println("Registration failed for " + registerRequest.getUsername() + ": " + e.getMessage());
            // Return a more specific error if needed from AuthService
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: " + e.getMessage()));
        }
    }


    // --- Inner classes for DTOs ---

    // JwtResponse (remains the same)
    private static class JwtResponse {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String username;
        private String email;
        private Long patientId;
        private String fullName;
        private List<String> roles;

        public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles, Long patientId, String fullName) {
            this.token = accessToken;
            this.id = id;
            this.username = username;
            this.email = email;
            this.roles = roles;
            this.patientId = patientId;
            this.fullName = fullName;
        }

        // Getters and Setters for all fields
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Long getPatientId() { return patientId; }
        public void setPatientId(Long patientId) { this.patientId = patientId; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles; }
    }

    // MessageResponse (remains the same)
    private static class MessageResponse {
        public String message;
        public Long userId; // Include userId if you want to return it on success or in some error cases

        public MessageResponse(String message) {
            this.message = message;
            this.userId = null; // Default to null for userId
        }

        public MessageResponse(String message, Long userId) {
            this.message = message;
            this.userId = userId;
        }

        public String getMessage() {
            return message;
        }
    }
}
