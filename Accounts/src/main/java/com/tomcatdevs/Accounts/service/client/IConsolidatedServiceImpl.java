package com.tomcatdevs.Accounts.service.client;

import com.tomcatdevs.Accounts.client.CardsFeignClient;
import com.tomcatdevs.Accounts.client.LoansFeignClient;
import com.tomcatdevs.Accounts.dto.AccountsDto;
import com.tomcatdevs.Accounts.dto.CardsDto;
import com.tomcatdevs.Accounts.dto.ConsolidatedCustomerDetailsDTO;
import com.tomcatdevs.Accounts.dto.LoansDto;
import com.tomcatdevs.Accounts.exception.ResourceNotFoundException;
import com.tomcatdevs.Accounts.mapper.AccountsMapper;
import com.tomcatdevs.Accounts.mapper.CustomerMapper;
import com.tomcatdevs.Accounts.model.Accounts;
import com.tomcatdevs.Accounts.model.Customer;
import com.tomcatdevs.Accounts.repository.AccountsRepository;
import com.tomcatdevs.Accounts.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class IConsolidatedServiceImpl implements IConsolidatedService{

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private CustomerRepository customerRepository;


    // calling to get data from cards,loans microservices
    @Autowired
    private CardsFeignClient cardsFeignClient;

    @Autowired
    public LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public Optional<ConsolidatedCustomerDetailsDTO> fetchConsolidatedInfo(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()-> new ResourceNotFoundException("customer","mobile number",mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(()-> new ResourceNotFoundException("accounts","customerId",customer.getCustomerId().toString()));

        ConsolidatedCustomerDetailsDTO customerDetailsDTO = CustomerMapper.mapToConsolidatedInfoDTO(customer,new ConsolidatedCustomerDetailsDTO());
        customerDetailsDTO.setAccountsDto(AccountsMapper.mapAccountsToDto(accounts,new AccountsDto()));

        // get information from cards and loans microservices
       ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardByMobileNumber(mobileNumber);
       ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoans(mobileNumber);

       // check if the response is not null
        if(null != cardsDtoResponseEntity){
            customerDetailsDTO.setCardsDto(cardsDtoResponseEntity.getBody());
        }
        if(null != loansDtoResponseEntity){
            customerDetailsDTO.setLoansDto(loansDtoResponseEntity.getBody());
        }
       return Optional.of(customerDetailsDTO);
    }
}
