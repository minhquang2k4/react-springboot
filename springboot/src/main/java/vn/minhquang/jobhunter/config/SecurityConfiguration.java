package vn.minhquang.jobhunter.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

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
  public SecurityFilterChain filterChain(HttpSecurity http,
      CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(
            authz -> authz
                .requestMatchers("/", "/login").permitAll()
                .anyRequest().authenticated())
        // mac dinh them 1 filter ten la BearerTokenAuthenticationFilter. Bay gio can
        // phai tao beans giai thich decode cho filter nay
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())
            .authenticationEntryPoint(customAuthenticationEntryPoint))
        // .exceptionHandling(
        //     exceptions -> exceptions
        //         .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()) // 401
        //         .accessDeniedHandler(new BearerTokenAccessDeniedHandler())) // 403

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

  @Bean
  public JwtDecoder jwtDecoder() {
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
        .macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
    // Đây là cách viết lamda để override lại phương thức decode của JwtDecoder (1
    // functional interface). Có thể dùng cách khác.
    return token -> {
      try {
        return jwtDecoder.decode(token);
      } catch (Exception e) {
        System.out.println("Error decoding JWT: " + e.getMessage());
        throw e;
      }
    };
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); // Không thêm prefix ROLE_ vào các quyền
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities"); // Tên claim chứa quyền trong JWT

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
  
    return jwtAuthenticationConverter;
  }

  private SecretKey getSecretKey() {
    byte[] decodedKey = Base64.from(jwtSecret).decode();
    return new SecretKeySpec(decodedKey, 0, decodedKey.length, SecurityUtil.JWT_ALGORITHM.getName());
  }
}