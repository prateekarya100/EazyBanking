package com.tomcatdevs.Accounts.service;

import com.tomcatdevs.Accounts.dto.AccountStatusRequest;
import com.tomcatdevs.Accounts.dto.AccountStatusResponse;
import com.tomcatdevs.Accounts.dto.CustomerDto;
import com.tomcatdevs.Accounts.model.Accounts;

import java.util.List;

public interface IAccountsService {
    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccountDetails(String mobileNumber);

    boolean updateCustomerAccountDetails(CustomerDto customerDto);

    boolean deleteCustomerAccount(String mobileNumber);

    List<Accounts> findAllActiveAccounts();

    AccountStatusResponse freezeAccount(AccountStatusRequest request);
    AccountStatusResponse unfreezeAccount(AccountStatusRequest request);
    AccountStatusResponse getAccountStatus(String accountNumber);
}
