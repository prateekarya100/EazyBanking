package com.tomcatdevs.Accounts.service.client;

import com.tomcatdevs.Accounts.dto.ConsolidatedCustomerDetailsDTO;

import java.util.Optional;

public interface IConsolidatedService {
    Optional<ConsolidatedCustomerDetailsDTO> fetchConsolidatedInfo(String mobileNumber);
}
