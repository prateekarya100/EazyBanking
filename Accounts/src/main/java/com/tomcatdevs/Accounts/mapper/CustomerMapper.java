package com.tomcatdevs.Accounts.mapper;

import com.tomcatdevs.Accounts.dto.ConsolidatedCustomerDetailsDTO;
import com.tomcatdevs.Accounts.dto.CustomerDto;
import com.tomcatdevs.Accounts.model.Customer;

public class CustomerMapper {

    public static CustomerDto mapToCustomerDto(Customer customer,CustomerDto customerDto){
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        return customerDto;
    }

    public static Customer mapToCustomer(CustomerDto customerDto, Customer customer){
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        return customer;
    }

    public static ConsolidatedCustomerDetailsDTO mapToConsolidatedInfoDTO(Customer customer, ConsolidatedCustomerDetailsDTO consolidatedDto){
        consolidatedDto.setName(customer.getName());
        consolidatedDto.setEmail(customer.getEmail());
        consolidatedDto.setMobileNumber(customer.getMobileNumber());
        return consolidatedDto;
    }
}
