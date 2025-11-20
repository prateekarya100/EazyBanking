package com.tomcatdevs.Accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
@Schema(
        name = "Loans",
        description = "holds loan related information"
)
public class LoansDto {
    @Schema(
            description = "customer mobile number",example = "91234567891"
    )
    @NotEmpty(message = "mobile number must not be null or blank")
    @Pattern(regexp = "^$|[0-9]{10}",message = "mobile number must be of 10 digit only")
    private String mobileNumber;

    @Schema(
            description = "customer mobile number",example = "1122334455"
    )
    @Pattern(regexp = "^$|[0-9]{10}",message = "account number must be of 10 digits number")
    private String loanAccountNumber;

    @Schema(
            description = "type of the loan",example = "personal loan"
    )
    @NotEmpty(message = "loan type must not be null or empty")
    @Size(min = 4,max = 20,message = "loan type should be at least of 4 characters")
    private String loanType;

    @Schema(
            description = "total loan amount taken by customer",example = "500000"
    )
    @Positive(message = "total loan should be positive, no negative value accepted here")
    private int totalLoan;

    @Schema(
            description = "total amount paid by customer to the bank",example = "120000"
    )
    @PositiveOrZero(message = "amount paid by customer should be zero or more")
    private int amountPaid;

    @Schema(
            description = "total outstanding on active loan",example = "380000"
    )
    @PositiveOrZero(message = "total outstanding amount should be zero or more")
    private int outstandingAmount;
}
