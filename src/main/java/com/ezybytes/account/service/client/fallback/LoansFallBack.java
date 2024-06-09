package com.ezybytes.account.service.client.fallback;

import com.ezybytes.account.dto.loansservice.LoansDto;
import com.ezybytes.account.service.client.LoansFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFallBack implements LoansFeignClient {
    @Override
    public ResponseEntity<LoansDto> fetchLoanDetails(String correlationId, String mobileNumber) {
        return null;
    }
}
