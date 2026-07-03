package com.dmart.DmartApplication.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dmart.DmartApplication.model.*;

@Repository
public class CustomerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // =============================
    // ADD CUSTOMER
    // =============================

    public int addCustomer(CustomerDTO customer) {

        String sql = """
                INSERT INTO customers
                (customer_name, mobile_number)
                VALUES (?,?)
                """;

        return jdbcTemplate.update(
                sql,
                customer.getCustomerName(),
                customer.getMobileNumber());

    }

    // =============================
    // GET ALL CUSTOMERS
    // =============================

    public List<CustomerDTO> getAllCustomers() {

        String sql =
                "SELECT * FROM customers ORDER BY customer_id DESC";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(CustomerDTO.class));

    }

    // =============================
    // SEARCH CUSTOMER BY MOBILE
    // =============================

    public CustomerDTO searchByMobile(String mobile) {

        String sql =
                "SELECT * FROM customers WHERE mobile_number=?";

        List<CustomerDTO> list = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(CustomerDTO.class),
                mobile);

        return list.isEmpty() ? null : list.get(0);

    }

    // =============================
    // GET CUSTOMER BY ID
    // =============================

    public CustomerDTO getCustomerById(int id) {

        String sql = """
                SELECT 
                customer_id AS customerId,
                customer_name AS customerName,
                mobile_number AS mobileNumber
                FROM customers
                WHERE customer_id=?
                """;

        return jdbcTemplate.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(CustomerDTO.class),
                id
        );

    }

    // =============================
    // UPDATE CUSTOMER
    // =============================

    public int updateCustomer(CustomerDTO customer) {

        String sql = """
                UPDATE customers
                SET customer_name=?,
                    mobile_number=?
                WHERE customer_id=?
                """;

        return jdbcTemplate.update(
                sql,
                customer.getCustomerName(),
                customer.getMobileNumber(),
                customer.getCustomerId());

    }

    // =============================
    // DELETE CUSTOMER
    // =============================
 public int deleteCustomer(int id) {

     String sql = "DELETE FROM customers WHERE customer_id=?";

     return jdbcTemplate.update(sql, id);

 }

//=============================
//DELETE CUSTOMER WITH BILLS
//=============================
    public void deleteCustomerAndBills(int customerId) {

        jdbcTemplate.update(
            "DELETE FROM bill_items WHERE bill_id IN (SELECT bill_id FROM bills WHERE customer_id=?)",
            customerId
        );

        jdbcTemplate.update(
            "DELETE FROM bills WHERE customer_id=?",
            customerId
        );

        jdbcTemplate.update(
            "DELETE FROM customers WHERE customer_id=?",
            customerId
        );

    }

}

