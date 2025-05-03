package vn.minhquang.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import vn.minhquang.jobhunter.domain.User;
import vn.minhquang.jobhunter.service.UserService;
import vn.minhquang.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
  
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  UserController(UserService userService, PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
  }

  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody User user) {
    // encode password with BCryptPasswordEncoder defined in SecurityConfiguration
    String encodedPassword = this.passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);

    User createdUser = this.userService.handleCreateUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }
  
  @GetMapping("/users")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = this.userService.getAllUsers();
    return ResponseEntity.ok(users);
  }
  
  @GetMapping("/users/{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
    User user = this.userService.getUserById(id);
    return ResponseEntity.ok(user);
  }
  
  @PutMapping("/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
    User updatedUser = this.userService.updateUser(id, user);
    return ResponseEntity.ok(updatedUser);
  }
  
  @DeleteMapping("/users/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) 
  throws IdInvalidException {

    if (id <= 0) {
      throw new IdInvalidException("Id must be greater than 0");
    }

    this.userService.deleteUser(id);
    return ResponseEntity.ok().body("User deleted successfully");
  }
}
