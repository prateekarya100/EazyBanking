package com.tomcatdevs.Accounts.dto;

import com.tomcatdevs.Accounts.model.Accounts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatusResponse {
    private String accountNumber;
    private String status;
    private String message;
    private LocalDateTime timestamp;

    private AccountDetails details;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountDetails {
        private String freezeReason;
        private LocalDateTime frozenAt;
        private String frozenBy;
        private String accountType;
        private String branchAddress;
        private Long customerId;
    }

    public static AccountStatusResponse fromAccount(Accounts account, String message) {
        AccountStatusResponse response = new AccountStatusResponse();
        response.setAccountNumber(account.getAccountNumber().toString());
        response.setStatus(account.getStatus().name());
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());

        AccountDetails details = new AccountDetails();
        details.setFreezeReason(account.getFreezeReason());
        details.setFrozenAt(account.getFrozenAt());
        details.setFrozenBy(account.getFrozenBy());
        details.setAccountType(account.getAccountType());
        details.setBranchAddress(account.getBranchAddress());
        details.setCustomerId(account.getCustomerId());

        response.setDetails(details);
        return response;
    }
}
