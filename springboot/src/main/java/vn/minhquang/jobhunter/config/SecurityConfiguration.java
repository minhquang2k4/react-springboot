package vn.minhquang.jobhunter.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.hibernate.type.descriptor.java.Immutability;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

import vn.minhquang.jobhunter.util.SecurityUtil;

@Configuration // Để đánh dấu lớp này là một lớp cấu hình Spring
@EnableMethodSecurity(securedEnabled = true) // Bật bảo mật cho các phương thức trong ứng dụng
public class SecurityConfiguration {

  @Bean // Đăng ký PasswordEncoder như một bean trong Spring context
  public PasswordEncoder passwordEncoder() {
    // Sử dụng BCryptPasswordEncoder để mã hóa mật khẩu
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            authz -> authz
                .requestMatchers("/").permitAll()
                // .anyRequest().authenticated())
                .anyRequest().permitAll())
        .formLogin(f -> f.disable()) // Tắt form login vì dùng staless
        // Cau hinh section mac dinh la stateful, can phai chinh sua lai thanh stateless
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  @Value("${jwt.base64-secret}")
  private String jwtSecret;
  @Value("${jwt.token-validity-in-seconds}")
  private String jwtExpiration;

  @Bean
  public JwtEncoder jwtEncoder() {
    return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
  }

  private SecretKey getSecretKey() {
    byte[] decodedKey = Base64.from(jwtSecret).decode();
    return new SecretKeySpec(decodedKey, 0, decodedKey.length, SecurityUtil.JWT_ALGORITHM.getName());
  }
}