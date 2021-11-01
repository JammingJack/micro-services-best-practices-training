package org.opernlab.billingservice;

import org.opernlab.billingservice.dto.InvoiceRequestDTO;
import org.opernlab.billingservice.services.InvoiceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.math.BigDecimal;

@EnableFeignClients
@SpringBootApplication
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    CommandLineRunner start(InvoiceService invoiceService) {
        return args -> {
            invoiceService.save(new InvoiceRequestDTO(BigDecimal.valueOf(78888), "C01"));
            invoiceService.save(new InvoiceRequestDTO(BigDecimal.valueOf(46643), "C01"));
            invoiceService.save(new InvoiceRequestDTO(BigDecimal.valueOf(36788), "C02"));
        };
    }

}
