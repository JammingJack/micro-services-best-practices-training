package org.opernlab.billingservice.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String customer_not_found_in_customer_db) {
        super(customer_not_found_in_customer_db);
    }
}
