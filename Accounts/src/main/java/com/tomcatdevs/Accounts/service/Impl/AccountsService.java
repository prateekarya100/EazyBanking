package com.tomcatdevs.Accounts.service.Impl;

import com.tomcatdevs.Accounts.constants.AccountsConstants;
import com.tomcatdevs.Accounts.dto.AccountStatusRequest;
import com.tomcatdevs.Accounts.dto.AccountStatusResponse;
import com.tomcatdevs.Accounts.dto.AccountsDto;
import com.tomcatdevs.Accounts.dto.CustomerDto;
import com.tomcatdevs.Accounts.enums.AccountStatus;
import com.tomcatdevs.Accounts.exception.AccountOperationException;
import com.tomcatdevs.Accounts.exception.CustomerAlreadyExistsException;
import com.tomcatdevs.Accounts.exception.ResourceNotFoundException;
import com.tomcatdevs.Accounts.mapper.AccountsMapper;
import com.tomcatdevs.Accounts.mapper.CustomerMapper;
import com.tomcatdevs.Accounts.model.Accounts;
import com.tomcatdevs.Accounts.model.Customer;
import com.tomcatdevs.Accounts.repository.AccountsRepository;
import com.tomcatdevs.Accounts.repository.CustomerRepository;
import com.tomcatdevs.Accounts.service.IAccountsService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class AccountsService implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;



    /**
     * @param customerDto
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer= CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());

        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("customer already exists with mobile number "+customer.getMobileNumber());
        }else {
            customer.setCreatedAt(LocalDateTime.now());
            customer.setCreatedBy(AccountsConstants.EXECUTIVE);
            Customer savedCustomer = customerRepository.save(customer);
            Accounts accountDetails=accountsRepository.save(createNewCustomerAccount(savedCustomer));
        }
    }

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public CustomerDto fetchAccountDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("customer", "mobile number", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()->new ResourceNotFoundException("customer", "mobile number", mobileNumber)
        );

        validateAccountNotFrozen(accounts);
        // accountsDto Nested inside CustomerDTO using MapperClasses
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer,new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapAccountsToDto(accounts,new AccountsDto()));
        return customerDto;
    }

    /**
     * @param customerDto
     * @return
     */
    @Override
    public boolean updateCustomerAccountDetails(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto!=null) {
            Accounts accounts = accountsRepository.findByAccountNumber(accountsDto.getAccountNumber()).orElseThrow(
                    ()->new ResourceNotFoundException("account","account number",accountsDto.getAccountNumber().toString())
            );

            // Add status validation
            validateAccountNotFrozen(accounts);

            Customer customer = customerRepository.findByCustomerId(accounts.getCustomerId()).orElseThrow(
                    ()->new ResourceNotFoundException("customer", "mobile number", customerDto.getMobileNumber())
            );
            AccountsMapper.mapDtoToAccounts(accountsDto,accounts);
            CustomerMapper.mapToCustomer(customerDto,customer);

            customerRepository.save(customer);
            accountsRepository.save(accounts);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteCustomerAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("customer", "mobile number", mobileNumber)
        );

        Accounts account = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("account", "customer id",
                        customer.getCustomerId().toString()));

        // Prevent deletion of frozen accounts
        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new AccountOperationException(
                    "Cannot delete a frozen account. Please unfreeze the account before deletion. " +
                            "Reason: " + account.getFreezeReason()
            );
        }

        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    /**
     * @return
     */
    @Override
    public List<Accounts> findAllActiveAccounts() {
        return accountsRepository.findAll();
    }



    private Accounts createNewCustomerAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long newAccountNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(newAccountNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy(AccountsConstants.EXECUTIVE);
        return newAccount;
    }

    // account freeze, unfreeze, get account status

    @Transactional
    @Override
    public AccountStatusResponse freezeAccount(AccountStatusRequest request) {
        Accounts account = accountsRepository.findByAccountNumber(Long.parseLong(request.getAccountNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("Account", "account number", request.getAccountNumber()));

        validateAccountNotFrozen(account);

        account.setStatus(AccountStatus.FROZEN);
        account.setFreezeReason(request.getReason());
        account.setFrozenAt(LocalDateTime.now());
        account.setFrozenBy("SYSTEM"); // In real app, get from security context

        accountsRepository.save(account);

        // Publish event for notifications if you've implemented the event system
        // publishAccountStatusChangedEvent(account, "Account frozen: " + request.getReason());

        return new AccountStatusResponse(account.getAccountNumber().toString(), account.getStatus().name(), request.getReason(), LocalDateTime.now());
    }

    @Transactional
    @Override
    public AccountStatusResponse unfreezeAccount(AccountStatusRequest request) {
        Accounts account = accountsRepository.findByAccountNumber(Long.parseLong(request.getAccountNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("Account", "account number", request.getAccountNumber()));

//        validateAccountNotFrozen(account);
//        account.setStatus(AccountStatus.ACTIVE);

        if (account.getStatus() == AccountStatus.FROZEN){
            account.setStatus(AccountStatus.ACTIVE);
            account.setFreezeReason(null);
            account.setFrozenAt(null);
            account.setFrozenBy(null);
        }
        accountsRepository.save(account);

        // Publish event for notifications if you've implemented the event system
        // publishAccountStatusChangedEvent(account, "Account unfrozen");

       return new AccountStatusResponse(account.getAccountNumber().toString(), account.getStatus().name(), request.getReason(), LocalDateTime.now());
    }

    @Override
    public AccountStatusResponse getAccountStatus(String accountNumber) {
        Accounts account = accountsRepository.findByAccountNumber(Long.parseLong(accountNumber))
                .orElseThrow(() -> new ResourceNotFoundException("Account", "account number", accountNumber));

        String message = account.getStatus() == AccountStatus.FROZEN ?
                "Account is frozen. Reason: " + account.getFreezeReason() :
                "Account is " + account.getStatus().name().toLowerCase();

        return new AccountStatusResponse(account.getAccountNumber().toString(), account.getStatus().name(), message, LocalDateTime.now());
    }

    private void validateAccountNotFrozen(Accounts account) {
        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new AccountOperationException(
                    "Account is frozen. Reason: " + account.getFreezeReason() +
                            ". Please contact customer support for assistance."
            );
        }
    }

}
