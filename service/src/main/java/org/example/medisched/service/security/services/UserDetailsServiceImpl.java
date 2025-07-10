package org.example.medisched.service.security.services;

import org.example.medisched.entity.User;
import org.example.medisched.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // This annotation makes it a Spring-managed component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository; // Inject your UserRepository to fetch user data

    @Override
    @Transactional // Ensures the user loading happens within a transaction
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the User entity from your database using the UserRepository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        // Convert your custom User entity into Spring Security's UserDetails object
        // The build method in UserDetailsImpl handles this mapping
        return UserDetailsImpl.build(user);
    }
}