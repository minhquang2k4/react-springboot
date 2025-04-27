package vn.minhquang.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.minhquang.jobhunter.domain.User;
import vn.minhquang.jobhunter.repository.UserRepository;

@Service
public class UserService {
  
  private final UserRepository userRepository;

  UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User handleCreateUser(User user) {
    return this.userRepository.save(user);
  }

  public User getUserById(Long id) {
    return this.userRepository.findById(id).orElse(null);
  }

  public User updateUser(Long id, User user) {
    if (this.userRepository.existsById(id)) {
      user.setId(id);
      return this.userRepository.save(user);
    }
    return null;
  }

  public void deleteUser(Long id) {
    this.userRepository.deleteById(id);
  }

  public List<User> getAllUsers() {
    return this.userRepository.findAll();
  }

}
