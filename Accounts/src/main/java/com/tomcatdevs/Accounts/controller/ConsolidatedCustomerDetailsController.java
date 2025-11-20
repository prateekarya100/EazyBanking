package com.tomcatdevs.Accounts.controller;

import com.tomcatdevs.Accounts.dto.ConsolidatedCustomerDetailsDTO;
import com.tomcatdevs.Accounts.dto.ErrorResponseDto;
import com.tomcatdevs.Accounts.service.client.IConsolidatedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@Tag(name="rest api for customers in eazybank",description = "fetching consolidated information of accounts,cards and loans in eazybank")
@RestController
@RequestMapping(path = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ConsolidatedCustomerDetailsController {

    @Autowired
    private IConsolidatedService iConsolidatedService;

    @Operation(
            summary = "fetching complete bank account details of existing customers from eazybank",
            description = "EazyBank Accounts microservices restful services documentation"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description="Request Accepted"
                    ),
                    @ApiResponse(
                            responseCode = "417",
                            description = "Expectation Failed"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description="Internal_Server_Error",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
                    )
            }
    )
    @GetMapping(value = "consolidated/CustomerDetails")
    public ResponseEntity<?> fetchConsolidatedCustomerInformation(@RequestParam
                                                                                               @Pattern(regexp = "$|[0-9]{10}",message = "number must be of 10 digit") String mobileNumber){
         Optional<ConsolidatedCustomerDetailsDTO> customerDetailsDTOOptional = iConsolidatedService.fetchConsolidatedInfo(mobileNumber);
         if(customerDetailsDTOOptional.isEmpty()){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                     .body("eazybank causing such issues while fetching consolidated customer information");
         }
         return ResponseEntity
                 .status(HttpStatus.OK)
                 .body(customerDetailsDTOOptional);
    }
}
