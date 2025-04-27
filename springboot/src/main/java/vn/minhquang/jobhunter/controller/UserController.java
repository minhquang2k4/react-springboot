package vn.minhquang.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

import vn.minhquang.jobhunter.domain.User;
import vn.minhquang.jobhunter.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
  
  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody User user) {
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
  public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
    this.userService.deleteUser(id);
    return ResponseEntity.ok().body("User deleted successfully");
  }
}
