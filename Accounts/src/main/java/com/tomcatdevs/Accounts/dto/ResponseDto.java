package com.tomcatdevs.Accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor @NoArgsConstructor
@ToString
@Schema(
        name = "Response",
        description = "schema to hold server response"
)
public class ResponseDto {
    @Schema(
            description = "http status server code response"
    )
    private String statusCode;
    @Schema(
            description = "response received from server"
    )
    private String response;

}
