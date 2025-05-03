package vn.minhquang.jobhunter.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetailsService")
// Ghi đè lại với cùng tên bean của Spring Security để sử dụng cho việc xác thực
// người dùng (đây là cách làm nâng cao, ngoài ra có thể sử dụng trong
// SecurityConfig)
public class UserDetailsServiceCustom implements UserDetailsService {

  private final UserService userService;

  public UserDetailsServiceCustom(UserService userService) {
    this.userService = userService;
  }

  // Mặc định sẽ load qua memory, nếu muốn load qua DB thì cần phải implement
  // UserDetailsService và override hàm loadUserByUsername
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    vn.minhquang.jobhunter.domain.User user = userService.handleFindUserByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("username/password is invalid");
    }

    return new User(
        user.getEmail(),
        user.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

  }

}
