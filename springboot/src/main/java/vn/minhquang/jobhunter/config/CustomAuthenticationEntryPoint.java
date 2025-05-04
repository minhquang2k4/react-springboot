package vn.minhquang.jobhunter.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.minhquang.jobhunter.domain.RestResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

  private final ObjectMapper mapper;

  public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    // Mặc định sử lý
    this.delegate.commence(request, response, authException);
    // sau đó mới đến xử lý của mình

    response.setContentType("application/json");

    RestResponse<Object> restResponse = new RestResponse<>();
    restResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
    restResponse.setError(authException.getCause().getMessage());
    restResponse.setMessage("Unauthorized");

    mapper.writeValue(response.getWriter(), restResponse);

  }

}
