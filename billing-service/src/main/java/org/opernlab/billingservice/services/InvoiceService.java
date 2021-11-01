package org.opernlab.billingservice.services;

import org.opernlab.billingservice.dto.InvoiceRequestDTO;
import org.opernlab.billingservice.dto.InvoiceResponseDTO;

import java.util.List;

public interface InvoiceService {
    InvoiceResponseDTO save(InvoiceRequestDTO invoiceRequestDTO);
    InvoiceResponseDTO getInvoice(String invoiceId);
    List<InvoiceResponseDTO> getInvoicesByCustomer(String customerId);
}
