package org.opernlab.billingservice.web;

import org.opernlab.billingservice.dto.InvoiceRequestDTO;
import org.opernlab.billingservice.dto.InvoiceResponseDTO;
import org.opernlab.billingservice.repositories.InvoiceRepository;
import org.opernlab.billingservice.services.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(path="/invoicesByCustomer/{customerId}")
    public List<InvoiceResponseDTO> getInvoicesByCustomer(@PathVariable(name = "customerId") String id){
        return invoiceService.getInvoicesByCustomer(id);
    }
    @PostMapping(path="/invoices")
    public InvoiceResponseDTO save(@RequestBody InvoiceRequestDTO invoiceRequestDTO){
        return invoiceService.save(invoiceRequestDTO);
    }
    @GetMapping(path="/invoices")
    public List<InvoiceResponseDTO> allInvoices(){
        return invoiceService.allInvoices();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
