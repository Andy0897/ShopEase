package com.example.ShopEase.OrderItem;

import com.example.ShopEase.Cart.Cart;
import com.example.ShopEase.Cart.CartRepository;
import com.example.ShopEase.Product.Product;
import com.example.ShopEase.Product.ProductRepository;
import com.example.ShopEase.Size.ProductSize;
import com.example.ShopEase.Size.ProductSizeRepository;
import com.example.ShopEase.User.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Objects;

@Service
public class OrderItemService {
    private OrderItemRepository orderItemRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private ProductSizeRepository productSizeRepository;
    private UserRepository userRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, CartRepository cartRepository, ProductRepository productRepository, ProductSizeRepository productSizeRepository, UserRepository userRepository) {
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
        this.userRepository = userRepository;
    }

    public String submitOrderItem(Long productId, Long productSizeId, Model model, Principal principal) {
        Product product = productRepository.findById(productId).get();
        ProductSize productSize = productSizeRepository.findById(productSizeId).get();
        Cart cart = userRepository.getUserByUsername(principal.getName()).getCart();

        if(orderItemRepository.findOrderItemInCartByProductIdAndSize(cart.getId(), product.getId(), productSize.getId()) != null &&
                Objects.equals(orderItemRepository.findOrderItemInCartByProductIdAndSize(cart.getId(), product.getId(), productSize.getId()).getProductSize().getId(), productSizeId)) {
            OrderItem orderItem = orderItemRepository.findOrderItemInCartByProductIdAndSize(cart.getId(), product.getId(), productSize.getId());
            orderItem.setQuantity(orderItem.getQuantity() + 1);
            orderItem.setTotalPrice(orderItem.getTotalPrice() + product.getPrice());
            orderItemRepository.save(orderItem);
        }
        else {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setProductSize(productSize);
            orderItem.setQuantity(1);
            orderItem.setTotalPrice(product.getPrice());
            cart.addItem(orderItem);
            cartRepository.save(cart);
        }

        productSize.setQuantity(productSize.getQuantity() - 1);
        productSizeRepository.save(productSize);
        return "redirect:/products/" + productId;
    }
}