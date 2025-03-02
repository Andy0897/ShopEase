package com.example.ShopEase.Cart;

import com.example.ShopEase.OrderItem.OrderItem;
import com.example.ShopEase.OrderItem.OrderItemRepository;
import com.example.ShopEase.Product.Product;
import com.example.ShopEase.Product.ProductRepository;
import com.example.ShopEase.User.User;
import com.example.ShopEase.User.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CartService {
    private CartRepository cartRepository;
    private UserRepository userRepository;
    private OrderItemRepository orderItemRepository;
    private ProductRepository productRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public String submitRemoveItemFromCart(Long orderItemId, Principal principal) {
        User user = userRepository.getUserByUsername(principal.getName());
        Cart cart = user.getCart();
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        Product product = orderItem.getProduct();
        product.setQuantity(product.getQuantity() + orderItem.getQuantity());
        cart.removeItem(orderItem);
        cartRepository.save(cart);
        orderItemRepository.delete(orderItem);
        productRepository.save(product);
        return "redirect:/cart";
    }
}
