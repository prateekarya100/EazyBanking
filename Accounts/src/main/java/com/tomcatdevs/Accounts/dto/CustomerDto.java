package com.tomcatdevs.Accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
@Schema(
        name = "Customer",
        description = "schema to hold customer related information"
)
public class CustomerDto {

    @NotEmpty(message = "name must not be null or empty")
    @Size(min = 3, message = "name must be minimum 3 and maximum of 8 characters")
    @Schema(
            description = "hold name of the customer",example = "varnita makrariya"
    )
    private String name;

    @NotEmpty(message = "email address must not be null or empty")
    @Email(message = "email address must be in valid format")
    @Schema(
            description = "hold email address of the customer",example = "varnita.makrari619@gmail.com"
    )
    private String email;

    @Pattern(regexp = "$|[0-9]{10}",message = "mobile number must be of 10 digits number")
    @Size(min = 10,max = 10, message = "mobile number must be of 10 digit")
    @Schema(
            description = "hold mobile number of the customer",example = "9123456619"
    )
    private String mobileNumber;

   //nested mapping accountsDto details to customerDto
    private AccountsDto accountsDto;
}
