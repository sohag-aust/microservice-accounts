package com.ezybytes.account.service;

import com.ezybytes.account.dto.CustomerDto;

public interface AccountsService {

    /**
     * Creates a new account for the given customer.
     *
     * @param customerDto the customer to create an account for.
     */
    void createAccount(CustomerDto customerDto);


    /**
     * Fetches the account for the given customer.
     *
     * @param mobileNumber the mobile number of the customer.
     * @return the account for the given customer.
     */
    CustomerDto fetchAccount(String mobileNumber);

    /**
     *
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(String mobileNumber);
}
