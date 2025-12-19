package com.tomcatdevs.Accounts.client;

import com.tomcatdevs.Accounts.dto.CardsDto;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards",fallback = CardsFallBack.class)
public interface CardsFeignClient {

    /** note--> fetching card detail by passing mobile number **/
    @GetMapping(value = "/api/fetch-card-by-mobileNumber",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardsDto> fetchCardByMobileNumber(
                        @RequestParam String mobileNumber);
}
