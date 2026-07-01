package com.dmart.DmartApplication.repository;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dmart.DmartApplication.model.ProductDTO;





@Repository
public class ProductDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addProduct(ProductDTO p) {

        String sql = """
        INSERT INTO product(
        product_name,
        category,
        price,
        quantity,
        manufacturer_name,
        manufacture_date,
        expiry_date,
        image_path,
        created_by,
        updated_by)
        VALUES(?,?,?,?,?,?,?,?,?,?)
        """;

        return jdbcTemplate.update(
                sql,
                p.getProductName(),
                p.getCategory(),
                p.getPrice(),
                p.getQuantity(),
                p.getManufacturerName(),
                p.getManufactureDate(),
                p.getExpiryDate(),
                p.getImagePath(),
                p.getCreatedBy(),
                p.getUpdatedBy());
    }

    public List<ProductDTO> getAllProducts() {

        String sql =
                "SELECT * FROM product ORDER BY product_id DESC";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(ProductDTO.class));
    }

    public ProductDTO getProductById(int id) {

        String sql =
                "SELECT * FROM product WHERE product_id=?";

        return jdbcTemplate.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(ProductDTO.class),
                id);
    }

    public int deleteProduct(int id) {

        String sql =
                "DELETE FROM product WHERE product_id=?";

        return jdbcTemplate.update(sql, id);
    }

    public ProductDTO searchProductById(int id) {

        String sql = "SELECT * FROM product WHERE product_id=?";

        return jdbcTemplate.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(ProductDTO.class),
                id);
    }
    public int updateProduct(ProductDTO p) {

        String sql = """
            UPDATE product
            SET
                product_name=?,
                category=?,
                price=?,
                quantity=?,
                manufacturer_name=?,
                manufacture_date=?,
                expiry_date=?,
                image_path=?,
                updated_by=?,
                updated_date=CURRENT_TIMESTAMP
            WHERE product_id=?
            """;

        return jdbcTemplate.update(

                sql,

                p.getProductName(),

                p.getCategory(),

                p.getPrice(),

                p.getQuantity(),

                p.getManufacturerName(),

                p.getManufactureDate(),

                p.getExpiryDate(),

                p.getImagePath(),

                p.getUpdatedBy(),

                p.getProductId()

        );

    }
}
