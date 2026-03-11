package com.tomcat.Loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Setter @Getter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "EazyBank restapi endpoints error response"
)
public class ErrorResponseDto {
    @Schema(
            description = "hold api path where error occur"
    )
    private String apiPath;

    @Schema(
            description = "hold error code due to which error occur"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "hold server response message"
    )
    private String serverResponse;

    @Schema(
            description = "hold timestamp when error occur"
    )
    private LocalDateTime errorTime;
}
