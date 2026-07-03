package com.dmart.DmartApplication.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dmart.DmartApplication.model.DashboardDTO;
import com.dmart.DmartApplication.model.ExpiryProductDTO;
import com.dmart.DmartApplication.model.LowStockDTO;
import com.dmart.DmartApplication.model.ProductDTO;


@Repository
public class DashboardDAO {

	@Autowired
    private JdbcTemplate jdbcTemplate;

    public DashboardDTO getDashboardSummary() {

        DashboardDTO dashboard = new DashboardDTO();

        // Total Products
        Integer totalProducts = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM product",
                Integer.class);

        // Total Customers
        Integer totalCustomers = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM customers",
                Integer.class);

        // Total Bills
        Integer totalBills = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM bills WHERE DATE(bill_date)=CURRENT_DATE",
                Integer.class);

        // Today's Revenue
        Double todayRevenue = jdbcTemplate.queryForObject(
                """
                SELECT COALESCE(SUM(total_amount),0)
                FROM bills
                WHERE DATE(bill_date)=CURRENT_DATE
                """,
                Double.class);

        dashboard.setTotalProducts(totalProducts);
        dashboard.setTotalCustomers(totalCustomers);
        dashboard.setTotalBills(totalBills);
        dashboard.setTodayRevenue(todayRevenue);

        // Recently Sold Products
        dashboard.setLatestProducts(getLatestProducts());

        // Low Stock Products
   dashboard.setLowStockProducts(getLowStockProducts());

        // Expiring Soon
   dashboard.setExpiringProducts(getExpiringProducts());

        return dashboard;
    }

    // ==========================================
    // Recently Added products in stock
    // ==========================================

    public List<ProductDTO> getLatestProducts() {

        String sql = """
                SELECT *
                FROM product
                ORDER BY created_date DESC
                LIMIT 4
                """;

        return jdbcTemplate.query(sql,    
                new BeanPropertyRowMapper<>(ProductDTO.class));
        

    }

    // ==========================================
    // Low Stock Products
    // ==========================================

    public List<LowStockDTO> getLowStockProducts() {

        String sql = """
                SELECT
                    product_name AS productName,
                    category,
                    quantity
                FROM product
                WHERE quantity <= 10
                ORDER BY quantity
                """;

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(LowStockDTO.class));
    }

    // ==========================================
    // Expiring Soon
    // ==========================================

    public List<ExpiryProductDTO> getExpiringProducts() {

        String sql = """
                SELECT
                    product_name AS productName,
                     TO_CHAR(expiry_date,'DD-Mon-YYYY') AS expiryDate
                  
                FROM product
                WHERE expiry_date BETWEEN CURRENT_DATE
                AND CURRENT_DATE + INTERVAL '30 days'
                ORDER BY expiry_date
                """;

        return jdbcTemplate.query(
                sql,
           new BeanPropertyRowMapper<>(ExpiryProductDTO.class));
    }

}
