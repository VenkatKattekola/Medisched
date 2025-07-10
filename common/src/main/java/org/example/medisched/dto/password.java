package org.example.medisched.dto; // You can put it anywhere, e.g., in a new 'util' package

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class password{
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Venkat@272005"; // <--- CHOOSE A STRONG PASSWORD HERE
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded password for '" + rawPassword + "': " + encodedPassword);

    }
}