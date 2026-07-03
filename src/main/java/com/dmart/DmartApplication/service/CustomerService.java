package com.dmart.DmartApplication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmart.DmartApplication.model.*;
import com.dmart.DmartApplication.repository.*;

@Service
public class CustomerService {

    @Autowired
    private CustomerDAO dao;

    // =============================
    // ADD CUSTOMER
    // =============================

    public int addCustomer(CustomerDTO customer) {

        return dao.addCustomer(customer);

    }

    // =============================
    // GET ALL CUSTOMERS
    // =============================

    public List<CustomerDTO> getAllCustomers() {

        return dao.getAllCustomers();

    }

    // =============================
    // SEARCH CUSTOMER BY MOBILE
    // =============================

    public CustomerDTO searchByMobile(String mobile) {

        return dao.searchByMobile(mobile);

    }

    // =============================
    // GET CUSTOMER BY ID
    // =============================

    public CustomerDTO getCustomerById(int id) {

        return dao.getCustomerById(id);

    }

    // =============================
    // UPDATE CUSTOMER
    // =============================

    public int updateCustomer(CustomerDTO customer) {

        return dao.updateCustomer(customer);

    }

    // =============================
    // DELETE CUSTOMER
    // =============================

    public int deleteCustomer(int id) {

        return dao.deleteCustomer(id);

    }

    @Transactional
    public void deleteCustomerAndBills(int customerId){

        dao.deleteCustomerAndBills(customerId);

    }

}