package org.opernlab.billingservice.mappers;

import org.mapstruct.Mapper;
import org.opernlab.billingservice.dto.InvoiceRequestDTO;
import org.opernlab.billingservice.dto.InvoiceResponseDTO;
import org.opernlab.billingservice.entities.Invoice;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    Invoice invoiceFromInvoiceRequestDTO(InvoiceRequestDTO invoiceRequestDTO);
    InvoiceResponseDTO invoiceResponseDTOFromInvoice(Invoice invoice);
}
