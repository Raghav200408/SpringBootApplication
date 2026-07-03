package com.dmart.DmartApplication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmart.DmartApplication.model.CartDTO;
import com.dmart.DmartApplication.repository.CartDAO;

@Service
public class CartService {

    @Autowired
    private CartDAO dao;

    // =============================
    // ADD PRODUCT TO CART
    // =============================

    public int addToCart(CartDTO cart) {

        CartDTO existing =
                dao.getCartItem(
                        cart.getCustomerId(),
                        cart.getProductId());

        if(existing != null){

            existing.setQuantity(
                    existing.getQuantity()
                    + cart.getQuantity());

            existing.setTotal(
                    existing.getPrice()
                    * existing.getQuantity());

            return dao.updateCart(existing);

        }

        return dao.addToCart(cart);

    }

    // =============================
    // GET CUSTOMER CART
    // =============================

    public List<CartDTO> getCartByCustomer(
            int customerId){

        return dao.getCartByCustomer(customerId);

    }

    // =============================
    // UPDATE CART
    // =============================

    public int updateCart(CartDTO cart){

        cart.setTotal(
                cart.getPrice()
                * cart.getQuantity());

        return dao.updateCart(cart);

    }

    // =============================
    // DELETE CART ITEM
    // =============================

    public int deleteCartItem(int cartId){

        return dao.deleteCartItem(cartId);

    }

    // =============================
    // CLEAR CART
    // =============================

    public int clearCart(int customerId){

        return dao.clearCart(customerId);

    }

}