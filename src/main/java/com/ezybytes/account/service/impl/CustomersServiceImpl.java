package com.ezybytes.account.service.impl;

import com.ezybytes.account.dto.AccountsDto;
import com.ezybytes.account.dto.CustomerDetailsDto;
import com.ezybytes.account.dto.cardsservice.CardsDto;
import com.ezybytes.account.dto.loansservice.LoansDto;
import com.ezybytes.account.entity.Accounts;
import com.ezybytes.account.entity.Customer;
import com.ezybytes.account.exception.ResourceNotFoundException;
import com.ezybytes.account.mapper.AccountsMapper;
import com.ezybytes.account.mapper.CustomerMapper;
import com.ezybytes.account.repository.AccountsRepository;
import com.ezybytes.account.repository.CustomerRepository;
import com.ezybytes.account.service.CustomersService;
import com.ezybytes.account.service.client.CardsFeignClient;
import com.ezybytes.account.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements CustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);

        if(null != loansDtoResponseEntity) { // putting this condition for handle loansFallBack null response checks
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);

        if(null!= cardsDtoResponseEntity) { // putting this condition for handle cardsFallBack null response checks
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }

        return customerDetailsDto;

    }
}