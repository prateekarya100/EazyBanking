package com.tomcatdevs.Accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
@Schema(
        name = "Accounts",
        description = "schema to hold accounts related information"
)
public class AccountsDto {
    @Pattern(regexp = "$|[0-9]{10}",message = "bank account number must be of 10 digit")
    @NotEmpty(message = "bank account number must not be null or empty")
    @Schema(
            description = "hold bank account number of customer",example = "1234567890"
    )
    private Long accountNumber;

    @NotEmpty(message = "bank account type must not be null or empty")
    @Schema(
            description = "hold bank account type of customer",example = "savings"
    )
    private String accountType;

    @NotEmpty(message = "bank address must not be null or empty")
    @Size(min = 10,message = "bank address must be atleast of 10 digits")
    @Schema(
            description = "hold bank address of customer",example = "candor techspace 135 sector-135 noida"
    )
    private String branchAddress;
}
