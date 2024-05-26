package com.ezybytes.account.service.client;

import com.ezybytes.account.dto.cardsservice.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("cards")
// this name cards is used, so that feignClient can get all cards microservice related info like : url, port from eureka server
public interface CardsFeignClient {

    @GetMapping(value = "/api/fetch", consumes = "application/json")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestParam String mobileNumber);
}
