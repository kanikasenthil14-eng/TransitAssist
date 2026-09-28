package com.college.transit_assist.Controller;

import com.college.transit_assist.entity.User;
import com.college.transit_assist.service.UserService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService; // final = no change after initialisation

    public UserController(UserService userService) { // dependency injection
        this.userService = userService;
    }

    @GetMapping("/")
    public String getApiStatus() {
        return "Transit Assist API is running. Use /users to access user records.";
    }

    @PostMapping("/users")
    // @RequestBody helps Spring convert the incoming JSON into a User object.
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get user by ID
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Update user
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {

        User existingUser = userService.getUserById(id);

        if (existingUser == null) {
            return null;
        }  

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setPassword(user.getPassword());
        existingUser.setRole(user.getRole());

        return userService.saveUser(existingUser);
    }  
    // Delete user
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }
}