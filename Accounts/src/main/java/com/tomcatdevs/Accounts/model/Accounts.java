package com.tomcatdevs.Accounts.model;

import com.tomcatdevs.Accounts.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class Accounts extends BaseEntity {
    private Long customerId;

    @Id
    private Long accountNumber;
    private String accountType;
    private String branchAddress;

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    private String freezeReason;
    private LocalDateTime frozenAt;
    private String frozenBy;

}
