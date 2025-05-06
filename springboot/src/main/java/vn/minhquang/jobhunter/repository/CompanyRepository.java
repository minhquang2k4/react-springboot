package vn.minhquang.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.minhquang.jobhunter.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
  // Custom query methods can be defined here if needed
  // For example, findByName(String name) or findByAddress(String address)
}
