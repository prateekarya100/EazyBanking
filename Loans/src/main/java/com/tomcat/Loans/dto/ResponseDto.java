package com.tomcat.Loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

@Setter @Getter @ToString @AllArgsConstructor @NoArgsConstructor
@Schema(
        name = "Response",
        description = "holds server response details"
)
public class ResponseDto {
    @Schema(
            description = "status code received from server"
    )
    private HttpStatus status;

    @Schema(
            description = "response message received from server"
    )
    private String response;
}
