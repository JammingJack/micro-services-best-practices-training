package org.opernlab.billingservice.web;

import org.opernlab.billingservice.dto.InvoiceRequestDTO;
import org.opernlab.billingservice.dto.InvoiceResponseDTO;
import org.opernlab.billingservice.repositories.InvoiceRepository;
import org.opernlab.billingservice.services.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api")
public class InvoiceRestController {
    private InvoiceService invoiceService;

    public InvoiceRestController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping(path="/invoices/{id}")
    public InvoiceResponseDTO getInvoice(@PathVariable String id){
        return invoiceService.getInvoice(id);
    }

    @GetMapping(path="/invoices/{customerId}")
    public List<InvoiceResponseDTO> getInvoicesByCustomer(@PathVariable(name = "customerId") String id){
        return invoiceService.getInvoicesByCustomer(id);
    }
    @PostMapping(path="/invoices")
    public InvoiceResponseDTO save(@RequestBody InvoiceRequestDTO invoiceRequestDTO){
        return invoiceService.save(invoiceRequestDTO);
    }
}
