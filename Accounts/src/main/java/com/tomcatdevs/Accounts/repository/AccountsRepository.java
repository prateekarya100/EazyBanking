package com.tomcatdevs.Accounts.repository;

import com.tomcatdevs.Accounts.model.Accounts;
import com.tomcatdevs.Accounts.model.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    Optional<Accounts> findByCustomerId(Long customerId);

    Optional<Accounts> findByAccountNumber(Long accountNumber);

    @Transactional // used when we modify / delete / update into database resources
    @Modifying // used when we modify / delete / update into database resources
    void deleteByCustomerId(Long customerId);
}
