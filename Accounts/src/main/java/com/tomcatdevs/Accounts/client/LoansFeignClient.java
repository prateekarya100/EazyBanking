package com.tomcatdevs.Accounts.client;

import com.tomcatdevs.Accounts.dto.CardsDto;
import com.tomcatdevs.Accounts.dto.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loans",fallback = LoansFallBack.class)
public interface LoansFeignClient {

    /** note--> fetching loan detail by passing mobile number **/
    @GetMapping(value = "/api/fetchLoan",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoansDto> fetchLoans(
            @RequestParam String mobileNumber);
}
