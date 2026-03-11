package com.tomcat.Cards.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
@Schema(
        name = "Cards",
        description = "schema to hold card related information"
)
public class CardsDto {
    @Schema(
            description = "mobile number of the customer",example = "9123456789"
    )
    @NotEmpty(message = "mobile number must not be null or empty")
    @Pattern(regexp = "$|[0-9]{10}",message = "mobile number must be of 10 digit only")
    private String mobileNumber;

    @Schema(
            description = "customers bank card number",example = "1122334455"
    )
    @NotEmpty(message = "card number must not be null or empty")
    @Pattern(regexp = "$|[0-9]{10}",message = "card number must be of 10 digit only")
    @Positive(message = "card number should be positive sequence of numbers")
    private String cardNumber;

    @Schema(
            description = "card type of the customer",example = "credit_card"
    )
    @NotEmpty(message = "card type must not be null or empty")
    @Size(min = 4, max = 16,message = "card type should be minimum of 4 character")
    private String cardType;

    @Schema(
            description = "2024-11-04T14:15:19.405+05:30",example = "9123456789"
    )
//    @NotEmpty(message = "card expiry date must not be null or empty")
    private LocalDateTime cardExpiryDate;

    @Schema(
            description = "card 3 digit cvv number",example = "123"
    )
//    @NotEmpty(message = "card cvv number must not be null or empty")
//    @Size(min = 3,max = 3,message = "card cvv number must be of 3 digit only")
    @Min(value = 100, message = "card cvv number should be of 3 digit")
    @Max(value = 999, message = "card cvv number should be of 3 digit")
    private long cardCVV;

    @Schema(
            description = "name of the card",example = "coral"
    )
    private String cardName;

    @Schema(
            description = "status of card whether active or freeze",example = "active"
    )
    private String cardStatus;

    @Schema(
            description = "name of the card issuer bank",example = "sbi credit cards"
    )
    private String cardIssuerBank;

    @Schema(
            description = "total limit of the card",example = "500000"
    )
    private long totalLimit;

    @Schema(
            description = "available limit of the card",example = "500000"
    )
    private long availableLimit;
    @Schema(
            description = "card amount utilized by customer",example = "10000"
    )
    private long amountUsed;
}
