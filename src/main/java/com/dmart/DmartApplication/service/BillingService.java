package com.dmart.DmartApplication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmart.DmartApplication.model.BillingDTO;
import com.dmart.DmartApplication.model.BillingSummaryDTO;
import com.dmart.DmartApplication.model.InvoiceDTO;
import com.dmart.DmartApplication.repository.BillingDAO;

@Service
public class BillingService {

    @Autowired
    private BillingDAO billingDAO;

    public BillingDTO getBillDetails(int customerId) {

        return billingDAO.getBillDetails(customerId);

    }

    // ===============================
    // Generate Bill
    // ===============================

    public int generateBill(int customerId, String paymentType) {

    	return billingDAO.generateBill(customerId, paymentType);

    }
    public InvoiceDTO getInvoice(int billId) {

        return billingDAO.getInvoice(billId);

    }
    
    public List<BillingSummaryDTO> getAllBills(){

        return billingDAO.getAllBills();

    }

}