package com.tomcat.Loans.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Schema(
        name = "Loans",
        description = "schema to hold loans related information"
)
public class Loans  extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long loanId;

    private String mobileNumber;

    private String loanAccountNumber;

    private String loanType;

    private int totalLoan;

    private int amountPaid;

    private int outstandingAmount;
}
