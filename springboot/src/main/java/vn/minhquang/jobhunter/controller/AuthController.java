package vn.minhquang.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.minhquang.jobhunter.domain.dto.LoginDTO;
import vn.minhquang.jobhunter.domain.dto.ResLoginDTO;
import vn.minhquang.jobhunter.util.SecurityUtil;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthController {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final SecurityUtil securityUtil;

  AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil) {
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.securityUtil = securityUtil;
  }

  @PostMapping("login")
  public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
    // Nạp username và password vào security
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginDTO.getUsername(), loginDTO.getPassword());

    // Xác thực người dùng. Hệ thống sẽ tự động gọi UserDetailsService để lấy thông
    // tin người dùng từ DB thong qua ham loadUserByUsername
    // chinh vi vay chung ta can phai implement UserDetailsService va override ham
    // loadUserByUsername
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    String token = this.securityUtil.CreateToken(authentication);

    // Tạo đối tượng ResLoginDTO để trả về cho client
    ResLoginDTO resLoginDTO = new ResLoginDTO();
    resLoginDTO.setAccessToken(token);
    return ResponseEntity.ok(resLoginDTO);
  }
}
