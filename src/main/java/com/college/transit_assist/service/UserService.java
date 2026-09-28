package com.college.transit_assist.service;

import org.springframework.stereotype.Service;

import com.college.transit_assist.repository.UserRepository;
import com.college.transit_assist.entity.User;
import java.util.List;

@Service 
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
}
