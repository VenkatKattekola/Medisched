package org.example.medisched.service;

import org.example.medisched.entity.User;
import org.example.medisched.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);

    @Service
    class UserServiceImpl implements UserService {

        private final UserRepository userRepository;

        @Autowired
        public UserServiceImpl(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public List<User> getAllUsers() {
            return userRepository.findAll();
        }

        @Override
        public Optional<User> getUserById(Long id) {
            return userRepository.findById(id);
        }

        @Override
        public Optional<User> getUserByUsername(String username) {
            return userRepository.findByUsername(username);
        }

        @Override
        public User createUser(User user) {
            user.setCreatedAt(LocalDateTime.now());
            // updatedAt can stay null on creation
            return userRepository.save(user);
        }
        @Override
        public User updateUser(Long id, User updatedUser) {
            return userRepository.findById(id).map(user -> {
                user.setUsername(updatedUser.getUsername());
                user.setPasswordHash(updatedUser.getPasswordHash());
                user.setFullName(updatedUser.getFullName());
                user.setEmail(updatedUser.getEmail());
                user.setRole(updatedUser.getRole());
                user.setUpdatedAt(LocalDateTime.now());
                return userRepository.save(user);
            }).orElse(null);
        }
        @Override
        public void deleteUser(Long id) {
            userRepository.deleteById(id);
        }
    }
}
