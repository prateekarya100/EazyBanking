package com.tomcatdevs.Accounts.client;

import com.tomcatdevs.Accounts.dto.CardsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallBack implements CardsFeignClient{
    /**
     * note--> fetching card detail by passing mobile number
     *
     * @param mobileNumber
     **/
    @Override
    public ResponseEntity<CardsDto> fetchCardByMobileNumber(String mobileNumber) {
        return null;
    }
}
