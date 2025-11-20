package com.tomcatdevs.Accounts.mapper;

import com.tomcatdevs.Accounts.dto.AccountsDto;
import com.tomcatdevs.Accounts.model.Accounts;

public class AccountsMapper {

    public static AccountsDto mapAccountsToDto(Accounts accounts, AccountsDto accountsDto) {
        accountsDto.setAccountNumber(accounts.getAccountNumber());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setBranchAddress(accounts.getBranchAddress());
        return accountsDto;
    }

    public static Accounts mapDtoToAccounts(AccountsDto accountsDto,Accounts accounts) {
        accounts.setAccountNumber(accountsDto.getAccountNumber());
        accounts.setAccountType(accountsDto.getAccountType());
        accounts.setBranchAddress(accountsDto.getBranchAddress());
        return accounts;
    }
}
