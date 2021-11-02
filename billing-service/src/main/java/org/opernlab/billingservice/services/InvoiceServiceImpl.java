package org.opernlab.billingservice.services;

import org.opernlab.billingservice.dto.InvoiceRequestDTO;
import org.opernlab.billingservice.dto.InvoiceResponseDTO;
import org.opernlab.billingservice.entities.Customer;
import org.opernlab.billingservice.entities.Invoice;
import org.opernlab.billingservice.exceptions.CustomerNotFoundException;
import org.opernlab.billingservice.mappers.InvoiceMapper;
import org.opernlab.billingservice.openfeign.CustomerRestClient;
import org.opernlab.billingservice.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
    private InvoiceRepository invoiceRepository;
    private InvoiceMapper invoiceMapper;
    private CustomerRestClient customerRestClient;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, CustomerRestClient customerRestClient) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.customerRestClient = customerRestClient;
    }

    @Override
    public InvoiceResponseDTO save(InvoiceRequestDTO invoiceRequestDTO) {

        Invoice invoice = invoiceMapper.invoiceFromInvoiceRequestDTO(invoiceRequestDTO);
        invoice.setId(UUID.randomUUID().toString());
        invoice.setDate(new Date());
        //Verification de l'integrité referentiel : est ce que le customer existe?
        Customer customer;
        try {
            customer = customerRestClient.getCustomer(invoiceRequestDTO.getCustomerID());
        }catch(Exception e){
            throw new CustomerNotFoundException("Customer not found in customer_db");
        }
        Invoice savedInvoice = invoiceRepository.save(invoice);
        savedInvoice.setCustomer(customer);
        InvoiceResponseDTO invoiceResponseDTO = invoiceMapper.invoiceResponseDTOFromInvoice(savedInvoice);
        return invoiceResponseDTO;
    }

    @Override
    public InvoiceResponseDTO getInvoice(String invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).get();
        Customer customer = customerRestClient.getCustomer(invoice.getCustomerID());
        invoice.setCustomer(customer);
        return invoiceMapper.invoiceResponseDTOFromInvoice(invoice);
    }

    @Override
    public List<InvoiceResponseDTO> getInvoicesByCustomer(String customerId) {
        List<Invoice> invoices = invoiceRepository.findByCustomerID(customerId);
        invoices.forEach(invoice -> {
            Customer customer = customerRestClient.getCustomer(invoice.getCustomerID());
            invoice.setCustomer(customer);
        });
        return invoices.stream()
                .map(invoice -> invoiceMapper.invoiceResponseDTOFromInvoice(invoice))
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponseDTO> allInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        for(Invoice invoice : invoices){
            Customer customer = customerRestClient.getCustomer(invoice.getCustomerID());
            invoice.setCustomer(customer);
        }
        return invoices.stream().map(invoice -> invoiceMapper.invoiceResponseDTOFromInvoice(invoice))
                .collect(Collectors.toList());
    }
}
