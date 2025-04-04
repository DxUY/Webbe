package com.ecom.service.impl;

import com.ecom.model.Cart;
import com.ecom.model.Product;
import com.ecom.model.User;
import com.ecom.repository.CartRepository;
import com.ecom.repository.ProductRepository;
import com.ecom.repository.UserRepository;
import com.ecom.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart saveCart(Integer productId, Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);

        if (user == null || product == null) {
            return null; // hoặc throw exception
        }

        // price là String => parseDouble
        String priceStr = product.getPrice();
        // Nếu DB có ký tự $, bạn có thể cắt bỏ trước khi parse:
        // priceStr = priceStr.replace("$", "");
        double numericPrice = Double.parseDouble(priceStr);

        // Tính giá (sau discount) tại thời điểm thêm vào giỏ
        double realDiscountPrice = numericPrice
                - (numericPrice * product.getDiscount() / 100.0);

        // Tìm xem cart cho (product, user) đã tồn tại chưa
        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);

        Cart cart;
        if (ObjectUtils.isEmpty(cartStatus)) {
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(1);

            // line total = realDiscountPrice * quantity
            cart.setTotalPrice(realDiscountPrice);
        } else {
            cart = cartStatus;
            int newQty = cart.getQuantity() + 1;
            cart.setQuantity(newQty);
            cart.setTotalPrice(newQty * realDiscountPrice);
        }
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getCartsByUser(Integer userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        // Tính lại totalPrice (line total) cho từng item
        for (Cart c : carts) {
            Product p = c.getProduct();
            // parse price sang double
            String priceStr = p.getPrice();
            double numericPrice = Double.parseDouble(priceStr);

            double realDiscountPrice = numericPrice
                    - (numericPrice * p.getDiscount() / 100.0);

            double lineTotal = realDiscountPrice * c.getQuantity();
            c.setTotalPrice(lineTotal);
        }
        return carts;
    }

    @Override
    public Integer getCountCart(Integer userId) {
        return cartRepository.countByUserId(userId);
    }

    @Override
    public void updateQuantity(String sy, Integer cid) {
        Cart cart = cartRepository.findById(cid).orElse(null);
        if (cart == null) return;

        Product p = cart.getProduct();
        // parse price sang double
        String priceStr = p.getPrice();
        double numericPrice = Double.parseDouble(priceStr);

        double realDiscountPrice = numericPrice
                - (numericPrice * p.getDiscount() / 100.0);

        if (sy.equalsIgnoreCase("de")) {
            int newQty = cart.getQuantity() - 1;
            if (newQty <= 0) {
                cartRepository.delete(cart);
            } else {
                cart.setQuantity(newQty);
                cart.setTotalPrice(newQty * realDiscountPrice);
                cartRepository.save(cart);
            }
        } else {
            int newQty = cart.getQuantity() + 1;
            cart.setQuantity(newQty);
            cart.setTotalPrice(newQty * realDiscountPrice);
            cartRepository.save(cart);
        }
    }
}
