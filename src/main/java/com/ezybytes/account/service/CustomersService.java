package com.ezybytes.account.service;

import com.ezybytes.account.dto.CustomerDetailsDto;

public interface CustomersService {

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}