package com.tomcatdevs.Accounts.repository;

import com.tomcatdevs.Accounts.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByMobileNumber(String mobileNumber);

    Optional<Customer> findByCustomerId(Long customerId);
}
