package com.dmart.DmartApplication.controller;



import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dmart.DmartApplication.model.ProductDTO;
import com.dmart.DmartApplication.service.ProductService;




@RestController
@RequestMapping("/api/products")
public class ProductController {
	 private static final Logger logger =
	            LogManager.getLogger(ProductController.class);
    @Autowired
    private ProductService service;
  

    @PostMapping(consumes = "multipart/form-data")
    public String addProduct(

    		 @RequestPart("product") ProductDTO p,
    	     @RequestPart("image") MultipartFile image) {

        logger.info("Add Product request received.");

        try {

            String uploadDir = "C:/DmartUploads/";

            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName =
                    UUID.randomUUID() + "_" + image.getOriginalFilename();

            Path path = Paths.get(uploadDir + fileName);

            Files.write(path, image.getBytes());

            p.setImagePath(fileName);

            service.addProduct(p);

            logger.info(
                    "Product added successfully. Name={}, Category={}, CreatedBy={}",
                    p.getProductName(),
                    p.getCategory(),
                    p.getCreatedBy());

            return "Product Added Successfully";

        } catch (Exception e) {

            logger.error("Failed to add product.", e);

            return "Failed";
        }
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {

        logger.info("Fetching all products.");

        List<ProductDTO> products = service.getAllProducts();

        logger.info("Fetched {} products.", products.size());

        return products;
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable("id") int id) {

        logger.info("Fetching product. ProductId={}", id);

        ProductDTO product = service.getProductById(id);

        if (product != null) {
            logger.info("Product found. ProductId={}", id);
        } else {
            logger.warn("Product not found. ProductId={}", id);
        }

        return product;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable("id") int id) {

        try {

            int result = service.deleteProduct(id);

            if (result > 0) {
            	logger.info("Product deleted successfully. ProductId={}", id);
                return ResponseEntity.ok("Product Deleted Successfully");
            }
            logger.warn("Delete failed. Product not found. ProductId={}", id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Product Not Found");

        }
        catch (DataIntegrityViolationException e) {

            logger.error(
                    "Cannot delete product. ProductId={} is used in billing.",
                    id,
                    e);

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Cannot delete product because it is already used in billing.");
        }
    }

    @GetMapping("/search/{id}")
    public ProductDTO searchProductById(@PathVariable("id") int id) {

        logger.info("Searching product. ProductId={}", id);

        ProductDTO product = service.searchProductById(id);

        if (product != null) {
            logger.info("Search successful. ProductId={}", id);
        } else {
            logger.warn("Search failed. Product not found. ProductId={}", id);
        }

        return product;
    }
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public String updateProduct(
    		  @PathVariable("id") int id,
    		  @RequestPart("product") ProductDTO p,
    	        @RequestPart(value = "image", required = false) MultipartFile image) {

        logger.info("Update Product request received. ProductId={}", id);

        try {

            ProductDTO oldProduct = service.getProductById(id);

            if (oldProduct == null) {
                logger.warn("Update failed. Product not found. ProductId={}", id);
                return "Product Not Found";
            }

            String fileName = oldProduct.getImagePath();

            if (image != null && !image.isEmpty()) {

                String uploadDir = "C:/DmartUploads/";

                File dir = new File(uploadDir);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

                Path path = Paths.get(uploadDir + fileName);

                Files.write(path, image.getBytes());
            }

            p.setProductId(id);
            p.setImagePath(fileName);

            service.updateProduct(p);

            logger.info(
                    "Product updated successfully. ProductId={}, UpdatedBy={}",
                    id,
                    p.getUpdatedBy());

            return "Product Updated Successfully";

        } catch (Exception e) {

            logger.error("Failed to update product. ProductId={}", id, e);

            return "Update Failed";
        }
    }
}
