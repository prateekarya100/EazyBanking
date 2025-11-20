package com.tomcatdevs.Accounts.service.Impl;

import com.tomcatdevs.Accounts.constants.AccountsConstants;
import com.tomcatdevs.Accounts.dto.AccountsDto;
import com.tomcatdevs.Accounts.dto.CustomerDto;
import com.tomcatdevs.Accounts.exception.CustomerAlreadyExistsException;
import com.tomcatdevs.Accounts.exception.ResourceNotFoundException;
import com.tomcatdevs.Accounts.mapper.AccountsMapper;
import com.tomcatdevs.Accounts.mapper.CustomerMapper;
import com.tomcatdevs.Accounts.model.Accounts;
import com.tomcatdevs.Accounts.model.Customer;
import com.tomcatdevs.Accounts.repository.AccountsRepository;
import com.tomcatdevs.Accounts.repository.CustomerRepository;
import com.tomcatdevs.Accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public boolean deleteCustomerAccount(String mobileNumber) {
//        boolean isDeleted = false;
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("customer", "mobile number", mobileNumber)
        );

        /* data jpa start calling delete query based on
           deleteBy --> delete prefix
           customerId-> unique id of the customer
        */
//        CUSTOM REPOSITORY METHOD CREATED FOR DELETE OPERATION
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
}
