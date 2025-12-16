package com.tomcatdevs.Accounts.dto;

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
}
