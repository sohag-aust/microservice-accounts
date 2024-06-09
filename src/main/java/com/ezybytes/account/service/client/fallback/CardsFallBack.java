package com.ezybytes.account.service.client.fallback;

import com.ezybytes.account.dto.cardsservice.CardsDto;
import com.ezybytes.account.service.client.CardsFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallBack implements CardsFeignClient {
    @Override
    public ResponseEntity<CardsDto> fetchCardDetails(String correlationId, String mobileNumber) {
        return null;
    }
}
