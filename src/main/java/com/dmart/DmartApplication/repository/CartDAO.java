package com.dmart.DmartApplication.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dmart.DmartApplication.model.CartDTO;

@Repository
public class CartDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // =============================
    // ADD PRODUCT TO CART
    // =============================

    public int addToCart(CartDTO cart) {

        String sql = """
            INSERT INTO cart
            (
                customer_id,
                product_id,
                quantity,
                price,
                total
            )
            VALUES (?,?,?,?,?)
            """;

        return jdbcTemplate.update(
                sql,
                cart.getCustomerId(),
                cart.getProductId(),
                cart.getQuantity(),
                cart.getPrice(),
                cart.getTotal());

    }

    // =============================
    // GET CART BY CUSTOMER
    // =============================

    public List<CartDTO> getCartByCustomer(int customerId) {

        String sql = """
            SELECT
                c.cart_id,
                c.customer_id,
                c.product_id,
                p.product_name,
                c.price,
                c.quantity,
                c.total,
                c.added_date
            FROM cart c
            JOIN product p
            ON c.product_id = p.product_id
            WHERE c.customer_id = ?
            ORDER BY c.cart_id
            """;

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(CartDTO.class),
                customerId);

    }

    // =============================
    // UPDATE CART QUANTITY
    // =============================

    public int updateCart(CartDTO cart) {

        String sql = """
            UPDATE cart
            SET
                quantity=?,
                total=?
            WHERE cart_id=?
            """;

        return jdbcTemplate.update(
                sql,
                cart.getQuantity(),
                cart.getTotal(),
                cart.getCartId());

    }

    // =============================
    // DELETE CART ITEM
    // =============================

    public int deleteCartItem(int cartId) {

        String sql =
                "DELETE FROM cart WHERE cart_id=?";

        return jdbcTemplate.update(sql, cartId);

    }

    // =============================
    // CLEAR CUSTOMER CART
    // =============================

    public int clearCart(int customerId) {

        String sql =
                "DELETE FROM cart WHERE customer_id=?";

        return jdbcTemplate.update(sql, customerId);

    }

    // =============================
    // CHECK PRODUCT EXISTS
    // =============================

    public CartDTO getCartItem(int customerId, int productId) {

        String sql = """
            SELECT *
            FROM cart
            WHERE customer_id=?
            AND product_id=?
            """;

        List<CartDTO> list = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(CartDTO.class),
                customerId,
                productId);

        if (list.isEmpty()) {

            return null;

        }

        return list.get(0);

    }

}
