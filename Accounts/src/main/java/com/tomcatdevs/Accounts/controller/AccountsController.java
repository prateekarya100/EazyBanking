package com.tomcatdevs.Accounts.controller;

import com.tomcatdevs.Accounts.dto.*;
import com.tomcatdevs.Accounts.model.Accounts;
import com.tomcatdevs.Accounts.service.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
@Tag(
        name = "EazyBank Accounts Service",
        description = "EazyBank Accounts microservices restful services documentation"
)
@EnableConfigurationProperties(value = {ContactInfoAccountsDevTeam.class})
public class AccountsController {

//    @Autowired
    private IAccountsService iAccountsService;

    @Autowired
    private ContactInfoAccountsDevTeam accountsDevTeam;

    @Operation(
            summary = "Create new bank account in eazybank",
            description = "EazyBank Accounts microservices restful services documentation"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Request Processed and Create New Account Successfully"
                    ),
                    @ApiResponse(
                        description = "Expectation Failed",
                            responseCode = "417"
                    ),
                    @ApiResponse(
                            description = "Internal_Server_Error",
                            responseCode = "500",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }
    )
    @PostMapping(value = "/createAccount")
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody CustomerDto  customerDto){
        System.out.println(customerDto);
        iAccountsService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(HttpStatus.CREATED.toString(),"account created successfully"));
    }

    @Operation(
            summary = "fetch bank account details of existing customers from eazybank",
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
    @GetMapping(value = "/fetchAccount")
    public ResponseEntity<CustomerDto> fetch(@RequestParam
                                                 @Pattern(regexp = "$|[0-9]{10}",message = "number must be of 10 digit")
                                                 String mobileNumber){
        CustomerDto customerDto=iAccountsService.fetchAccountDetails(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDto);
    }

    @Operation(
            summary = "update bank account detail of customer in eazybank",
            description = "EazyBank Accounts microservices restful services documentation"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "202",
                            description="HTTP Status ACCEPTED"
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
    @PutMapping(value = "/updateAccount")
    public ResponseEntity<ResponseDto> update(@Valid @RequestBody CustomerDto customerDto){
        boolean isUpdated = iAccountsService.updateCustomerAccountDetails(customerDto);
        if(isUpdated){
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ResponseDto(HttpStatus.ACCEPTED.toString(),"account updated successfully"));
        }else{
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(HttpStatus.EXPECTATION_FAILED.toString(),"customer account details updation failed"));
        }
    }


    @Operation(
            summary = "delete bank account of customer from eazybank",
            description = "EazyBank Accounts microservices restful services documentation"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description="HTTP Status OK"
                    ),
                    @ApiResponse(
                            responseCode = "417",
                            description = "Expectation Failed"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description="Internal_Server_Error",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class))
                    )
            }
    )
    @DeleteMapping(value = "/closureAccount")
    ResponseEntity<ResponseDto> delete(@RequestParam
                                       @Pattern(regexp = "$|[0-9]{10}",message = "number must be of 10 digit")
                                       String mobileNumber){
        boolean isDeleted=iAccountsService.deleteCustomerAccount(mobileNumber);
        if(isDeleted){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(HttpStatus.OK.toString(),"customer account deleted successfully"));
        }else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(HttpStatus.EXPECTATION_FAILED.toString(),"customer account deletion failed"));
        }
    }

    @GetMapping(value = "/contact-info")
    public ResponseEntity<ContactInfoAccountsDevTeam> contactCardDevTeam(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountsDevTeam);
    }

    @GetMapping(value = "/get-all-accounts")
    public List<Accounts> getListOfAllAccounts(){
        return iAccountsService.findAllActiveAccounts();
    }

}
