package com.dmart.DmartApplication.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dmart.DmartApplication.model.BillingDTO;
import com.dmart.DmartApplication.model.BillingSummaryDTO;
import com.dmart.DmartApplication.model.CartDTO;
import com.dmart.DmartApplication.model.InvoiceDTO;

@Repository
public class BillingDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public BillingDTO getBillDetails(int customerId) {

        BillingDTO bill = new BillingDTO();

        // Customer Details
        String customerSql = """
            SELECT customer_id,
                   customer_name,
                   mobile_number
            FROM customers
            WHERE customer_id = ?
        """;


        BillingDTO customer = jdbcTemplate.queryForObject(
                customerSql,
                new BeanPropertyRowMapper<>(BillingDTO.class),
                customerId);

        bill.setCustomerId(customer.getCustomerId());
        bill.setCustomerName(customer.getCustomerName());
        bill.setMobileNumber(customer.getMobileNumber());

        // Cart Items

        String cartSql = """
            SELECT
                c.cart_id,
                c.product_id,
                p.product_name,
                c.quantity,
                c.price,
                5 AS gst,
                c.total
            FROM cart c
            JOIN product p
            ON c.product_id = p.product_id
            WHERE c.customer_id = ?
        """;

        List<CartDTO> items = jdbcTemplate.query(
                cartSql,
                new BeanPropertyRowMapper<>(CartDTO.class),
                customerId);

        bill.setItems(items);

        double subtotal = 0;

        for (CartDTO item : items) {
            subtotal += item.getTotal();
        }

        double gst = subtotal * 0.05;
        double grand = subtotal + gst;

        bill.setSubtotal(subtotal);
        bill.setGst(gst);
        bill.setGrandTotal(grand);

        return bill;
    }
    
    public int generateBill(int customerId, String paymentType) {

        // Calculate total amount
    	String totalSql = "SELECT COALESCE(SUM(total),0) FROM cart WHERE customer_id=?";

    	Double subTotal = jdbcTemplate.queryForObject(
    	        totalSql,
    	        Double.class,
    	        customerId);

    	// Calculate GST (5%)
    	Double gst = subTotal * 0.05;

    	// Grand Total
    	Double totalAmount = subTotal + gst;

        // Insert into bills table
        String insertBill = """
            INSERT INTO bills
            (customer_id,bill_date,total_amount,payment_type)
            VALUES
            (?,CURRENT_TIMESTAMP,?,?)
            RETURNING bill_id
            """;

        Integer billId = jdbcTemplate.queryForObject(
                insertBill,
                Integer.class,
                customerId,
                totalAmount,
                paymentType);

        // Insert bill items
        String insertItems = """
            INSERT INTO bill_items
            (bill_id, product_id, quantity, price, gst, total)
            SELECT
                ?,
                product_id,
                quantity,
                price,
                5,
                total
            FROM cart
            WHERE customer_id=?
            """;

        jdbcTemplate.update(insertItems, billId, customerId);

        // Reduce stock
        String updateStock = """
            UPDATE product p
            SET quantity = p.quantity - c.quantity
            FROM cart c
            WHERE p.product_id = c.product_id
            AND c.customer_id = ?
            """;

        jdbcTemplate.update(updateStock, customerId);

        // Clear cart
        jdbcTemplate.update(
                "DELETE FROM cart WHERE customer_id=?",
                customerId);

        return billId;
    }
    public InvoiceDTO getInvoice(int billId) {

        InvoiceDTO invoice = new InvoiceDTO();

        // Bill + Customer Details
        String billSql = """
            SELECT
                b.bill_id,
                c.customer_name,
                c.mobile_number,
                b.payment_type,
                TO_CHAR(b.bill_date,'DD-Mon-YYYY HH24:MI') AS bill_date,
                b.total_amount
            FROM bills b
            JOIN customers c
            ON b.customer_id = c.customer_id
            WHERE b.bill_id = ?
        """;

        InvoiceDTO details = jdbcTemplate.queryForObject(
                billSql,
                new BeanPropertyRowMapper<>(InvoiceDTO.class),
                billId);

        invoice.setBillId(details.getBillId());
        invoice.setCustomerName(details.getCustomerName());
        invoice.setMobileNumber(details.getMobileNumber());
        invoice.setPaymentType(details.getPaymentType());
        invoice.setBillDate(details.getBillDate());

        // Bill Items
        String itemsSql = """
            SELECT
                bi.product_id,
                p.product_name,
                bi.quantity,
                bi.price,
                bi.gst,
                bi.total
            FROM bill_items bi
            JOIN product p
            ON bi.product_id = p.product_id
            WHERE bi.bill_id = ?
        """;

        List<CartDTO> items = jdbcTemplate.query(
                itemsSql,
                new BeanPropertyRowMapper<>(CartDTO.class),
                billId);

        invoice.setItems(items);

        double subtotal = 0;

        for(CartDTO item : items){
            subtotal += item.getTotal();
        }

        invoice.setSubtotal(subtotal);
        invoice.setGst(subtotal * 0.05);
        invoice.setGrandTotal(subtotal + (subtotal * 0.05));

        return invoice;
    }
    
    public List<BillingSummaryDTO> getAllBills() {

        String sql = """
            SELECT
                b.bill_id,
                c.customer_name,
                c.mobile_number,
                TO_CHAR(b.bill_date,'DD-Mon-YYYY HH24:MI') AS bill_date,
                b.total_amount AS grand_total,
                b.payment_type
            FROM bills b
            JOIN customers c
            ON b.customer_id = c.customer_id
            ORDER BY b.bill_id DESC
            """;

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(BillingSummaryDTO.class));
    }

}