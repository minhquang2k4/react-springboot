package vn.minhquang.jobhunter.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class ResLoginDTO {
  private String accessToken;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
}
