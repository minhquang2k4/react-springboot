package vn.minhquang.jobhunter.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.Base64;

@Service
public class SecurityUtil {

  public final JwtEncoder jwtEncoder;
  public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

  public SecurityUtil(JwtEncoder jwtEncoder) {
    this.jwtEncoder = jwtEncoder;
  }

  @Value("${jwt.base64-secret}")
  private String jwtSecret;
  @Value("${jwt.token-validity-in-seconds}")
  private Long jwtExpiration;

  public String CreateToken(Authentication authentication) {
    // Tạo thời gian hiện tại và + thời gian hết hạn cho token
    Instant now = Instant.now();
    Instant validity = now.plus(this.jwtExpiration, ChronoUnit.SECONDS);

    // Tạo payload
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .subject(authentication.getName())
        .issuedAt(now)
        .expiresAt(validity)
        .claim("authorities", authentication)
        .build();

    // Tạo header với thuật toán đã chọn
    JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
    return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

  }
}
