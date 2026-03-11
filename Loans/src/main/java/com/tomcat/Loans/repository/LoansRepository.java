package com.tomcat.Loans.repository;

import com.tomcat.Loans.model.Loans;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoansRepository extends JpaRepository<Loans, Long> {
    Optional<Loans> findByMobileNumber(String mobileNumber);
    Optional<Loans> findByLoanAccountNumber(String loanAccountNumber);
}
