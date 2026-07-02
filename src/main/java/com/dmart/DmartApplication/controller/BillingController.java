package com.dmart.DmartApplication.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmart.DmartApplication.model.BillingDTO;
import com.dmart.DmartApplication.model.BillingSummaryDTO;
import com.dmart.DmartApplication.model.InvoiceDTO;
import com.dmart.DmartApplication.service.BillingService;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private static final Logger logger =
            LogManager.getLogger(BillingController.class);

    @Autowired
    private BillingService billingService;

    @GetMapping("/{customerId}")
    public BillingDTO getBillDetails(@PathVariable int customerId) {
        return billingService.getBillDetails(customerId);
    }

    @PostMapping("/generate")
    public int generateBill(
            @RequestParam int customerId,
            @RequestParam String paymentType) {

        logger.info("Bill generated. CustomerId={}, PaymentType={}",
                customerId, paymentType);

        return billingService.generateBill(customerId, paymentType);
    }

    @GetMapping("/invoice/{billId}")
    public InvoiceDTO getInvoice(@PathVariable int billId) {
        return billingService.getInvoice(billId);
    }

    @GetMapping("/all")
    public List<BillingSummaryDTO> getAllBills() {
        return billingService.getAllBills();
    }
}