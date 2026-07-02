package com.dmart.DmartApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dmart.DmartApplication.model.CustomerDTO;
import com.dmart.DmartApplication.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	private static final Logger logger =
          LogManager.getLogger(CustomerController.class);
    @Autowired
    private CustomerService service;

    // =============================
    // REGISTER CUSTOMER
    // =============================

    @PostMapping
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customer) {

    	
        CustomerDTO existing = service.searchByMobile(customer.getMobileNumber());

        if (existing != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Mobile number already exists.");
        }

        int result = service.addCustomer(customer);

        if (result > 0) {
        	logger.info("Customer registered. Name={}, Mobile={}",
        	        customer.getCustomerName(),
        	        customer.getMobileNumber());
            return ResponseEntity.ok("Customer Registered Successfully");
        }
        logger.warn("Registration failed");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Registration Failed");
    }
    // =============================
    // GET ALL CUSTOMERS
    // =============================

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {

        return service.getAllCustomers();

    }

    // =============================
    // GET CUSTOMER BY ID
    // =============================

    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(
            @PathVariable("id") int id) {

        return service.getCustomerById(id);

    }

    // =============================
    // SEARCH CUSTOMER BY MOBILE
    // =============================

    @GetMapping("/search/{mobile}")
    public ResponseEntity<CustomerDTO> searchCustomer(
            @PathVariable("mobile") String mobile) {

        CustomerDTO customer = service.searchByMobile(mobile);

        if (customer == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }

        return ResponseEntity.ok(customer);
    }

    // =============================
    // UPDATE CUSTOMER
    // =============================

    @PutMapping("/{id}")
    public String updateCustomer(

            @PathVariable("id") int id,

            @RequestBody CustomerDTO customer){

        customer.setCustomerId(id);

        int result = service.updateCustomer(customer);

        if(result > 0){
        	logger.info("Customer updated. CustomerId={}", id);
            return "Customer Updated Successfully";

        }

        return "Customer Update Failed";
    }

    // =============================
    // DELETE CUSTOMER
    // =============================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(
            @PathVariable("id") int id,
            @RequestParam(name = "force", defaultValue = "false") boolean force) {

        try {

            if(force){

                service.deleteCustomerAndBills(id);
                logger.info("Customer deleted. CustomerId={}", id);

                return ResponseEntity.ok("Customer Deleted Successfully");

            }

            service.deleteCustomer(id);

            return ResponseEntity.ok("Customer Deleted Successfully");

        }
        catch(DataIntegrityViolationException e){

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Purchase History Exists");

        }

    }
}


