package org.example.medisched.service;

import org.example.medisched.entity.Patients;
import org.example.medisched.entity.User;
import org.example.medisched.entity.Doctor; // Assuming you have a Doctor entity as well
import org.example.medisched.repository.PatientsRepository;
import org.example.medisched.repository.UserRepository;
import org.example.medisched.repository.DoctorRepository; // Assuming you have DoctorRepository
import org.example.medisched.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientsRepository patientsRepository;

    @Autowired(required = false) // required = false if DoctorRepository might not always be present or fully configured
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        // Assuming password field name is 'passwordHash' in User entity
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        newUser.setFullName(request.getFullName());
        newUser.setCell(request.getCell());

        // --- CRITICAL SECURITY FIX: Explicitly whitelist allowed roles for public registration ---
        String requestedRoleString = request.getRole().toUpperCase(Locale.ROOT);
        User.Role assignedRole;

        if ("PATIENT".equals(requestedRoleString)) {
            assignedRole = User.Role.PATIENT;
        } else if ("DOCTOR".equals(requestedRoleString)) {
            // Only allow DOCTOR role if it's explicitly allowed for public registration
            // If doctors need approval, this logic would change (e.g., default to USER, then admin approves)
            assignedRole = User.Role.DOCTOR;
        } else {
            // For any other role (including "ADMIN"), default to PATIENT
            System.err.println("Security Warning: Attempted registration with unsupported role '" + requestedRoleString + "'. Defaulting to PATIENT.");
            assignedRole = User.Role.PATIENT;
        }
        newUser.setRole(assignedRole);
        // --- END CRITICAL SECURITY FIX ---

        User savedUser = userRepository.save(newUser);

        // Create associated entity based on the SAVED role
        if (savedUser.getRole() == User.Role.PATIENT) {
            if (patientsRepository != null) { // Defensive check, though should be autowired
                Patients newPatient = new Patients();
                newPatient.setUser(savedUser);
                newPatient.setPhoneNumber(savedUser.getCell()); // Transfer cell to patient if applicable
                // Set other patient fields to null or default if they are not part of the initial register request
                // e.g., newPatient.setDateOfBirth(null); newPatient.setGender(null); newPatient.setAddress(null);
                patientsRepository.save(newPatient);
            }
        } else if (savedUser.getRole() == User.Role.DOCTOR) {
            if (doctorRepository != null) { // Defensive check, though should be autowired
                Doctor newDoctor = new Doctor();
                newDoctor.setUser(savedUser);
                // Set other doctor-specific fields if available from request or default
                doctorRepository.save(newDoctor);
            }
        }
        return savedUser;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Assuming Patients has a 'user' field that is a reference to the User entity
    // and findByUserId queries based on the User entity's ID
    public Long findPatientIdByUserId(Long userId) {
        return patientsRepository.findByUserId(userId)
                .map(Patients::getPatientId)
                .orElse(null);
    }
}
