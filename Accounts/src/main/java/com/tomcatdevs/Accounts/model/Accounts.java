package com.tomcatdevs.Accounts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class Accounts extends BaseEntity{
    private Long customerId;

    @Id
    private Long accountNumber;
    private String accountType;
    private String branchAddress;
}
