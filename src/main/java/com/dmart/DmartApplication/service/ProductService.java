package com.dmart.DmartApplication.service;





import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmart.DmartApplication.model.ProductDTO;
import com.dmart.DmartApplication.repository.ProductDAO;




@Service
public class ProductService {

    @Autowired
    private ProductDAO dao;

    public int addProduct(ProductDTO p) {
        return dao.addProduct(p);
    }

    public List<ProductDTO> getAllProducts() {
        return dao.getAllProducts();
    }

    public ProductDTO getProductById(int id) {
        return dao.getProductById(id);
    }

    public int deleteProduct(int id) {
        return dao.deleteProduct(id);
    }

    public ProductDTO searchProductById(int id) {
        return dao.searchProductById(id);
    }
    public int updateProduct(ProductDTO product) {

        return dao.updateProduct(product);

    }
}

