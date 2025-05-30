package vn.minhquang.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.minhquang.jobhunter.domain.Company;
import vn.minhquang.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

  private final CompanyRepository companyRepository;

  public CompanyService(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  public Company handleCreateCompany(Company company) {
    return this.companyRepository.save(company);
  }
}
