package com.tomcat.Loans.controller;

import com.tomcat.Loans.dto.ContactInfoLoansDevTeam;
import com.tomcat.Loans.dto.ErrorResponseDto;
import com.tomcat.Loans.dto.LoansDto;
import com.tomcat.Loans.dto.ResponseDto;
import com.tomcat.Loans.service.ILoansService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "EazyBank Loans Service",
        description = "EazyBank Loans Microservices Restful WebServices Documentation"
)
@RestController
@RequestMapping(path = "/api/loans",produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
//@AllArgsConstructor
@EnableConfigurationProperties(value = {ContactInfoLoansDevTeam.class})
public class LoansController {

    private final ILoansService loansService;

    @Autowired
    private ContactInfoLoansDevTeam contactDevTeamInfo;

    public LoansController(ILoansService loansService) {
        this.loansService = loansService;
    }

    @Value("${build.version}")
    private String buildVersion;

    @Operation(
            summary = "Grant new loan to the bank customer",
            description = "EazyBank new loan create restful web services documentation"
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
    @PostMapping(value = "/createLoan")
    public ResponseEntity<ResponseDto> createLoan(
            @RequestParam
            @Pattern(regexp = "^$|[0-9]{10}",message = "mobile number must be of 10 digit only")
            String mobileNumber){
        boolean isCreated = loansService.createNewLoan(mobileNumber);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto(HttpStatus.CREATED,
                            "loan has been granted to customer successfully"));
        }
        else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(
                            HttpStatus.EXPECTATION_FAILED,
                            "error occur while creating loan account,please contact to development team"));
        }
    }

    @Operation(
            summary = "Fetch customer loan account related information",
            description = "EazyBank retrieving loan restful web services documentation"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Request Processed Successfully"
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
    @GetMapping(value = "/fetchLoan")
    public ResponseEntity<LoansDto> fetchLoans(
            @RequestParam
            @Pattern(regexp = "^$|[0-9]{10}",message = "mobile number must be of 10 digit only")
            String mobileNumber) {
        LoansDto loansDto = loansService.fetchLoanDetails(mobileNumber);
        if (loansDto != null){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(loansDto);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            summary = "Update customer loan related information",
            description = "EazyBank loan details update restful web services documentation"
    )
    @PutMapping(value = "/updateLoan")
    public ResponseEntity<ResponseDto> updateLoan(@Valid @RequestBody LoansDto loansDto){
       boolean isUpdated = loansService.updateLoanDetails(loansDto);
       if (isUpdated){
           return ResponseEntity.status(HttpStatus.OK)
                   .body(new ResponseDto(HttpStatus.OK,"customer loan details updated successfully"));
       }else {
           return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                   .body(new ResponseDto(HttpStatus.EXPECTATION_FAILED,
                           "something went wrong, during loan details update please contact to development team"));
       }
    }


    @Operation(
            summary = "Closing customer loan account information from eazybank",
            description = "EazyBank closing existing loan restful web services documentation"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Request Accepted Successfully"
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
    @DeleteMapping(value = "/loanClosure")
    public ResponseEntity<ResponseDto> loanClosure(@RequestParam
                                                       @Pattern(regexp = "^$|[0-9]{10}",message = "mobile number must be of 10 digit only")
                                                       String mobileNumber){
        boolean isloanClosed = loansService.loanClosure(mobileNumber);
        if (isloanClosed){
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new ResponseDto(HttpStatus.ACCEPTED,
                            "loan closed successfully,now there is no data exist for this customer"));
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(HttpStatus.BAD_REQUEST,
                            "oops, something went wrong, please contact to development team"));
        }
    }

    @GetMapping(value = "/contact-info")
    public ResponseEntity<ContactInfoLoansDevTeam> contactCardDevTeam(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(contactDevTeamInfo);
    }

    @GetMapping(value = "/version")
    public String getBuildVersion(){
        return buildVersion;
    }


}
