package vn.minhquang.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.minhquang.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String email); // Tìm kiếm người dùng theo email
}
