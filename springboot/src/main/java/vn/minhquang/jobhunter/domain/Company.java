package vn.minhquang.jobhunter.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.minhquang.jobhunter.util.SecurityUtil;

@Entity
@Table(name = "companies")
@Getter // lombok.Getter
@Setter
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank(message = "Name is required")
  private String name;

  @Column(columnDefinition = "MEDIUMTEXT")
  private String description;

  private String address;

  private String logo;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
  // Mặc định là GMT+0, lưu tại DB là GMT+0
  // Khi lấy ra trả cho client thì sẽ tự động chuyển về GMT+7 và định dạng theo
  // pattern
  private Instant createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
  private Instant updatedAt;

  private String createBy;

  private String updateBy;

  @PrePersist
  public void prePersist() {
    this.createdAt = Instant.now();
    this.createBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
        ? SecurityUtil.getCurrentUserLogin().get()
        : "";
  }
}
