package com.example.ShopEase.Cart;

import com.example.ShopEase.OrderItem.OrderItem;
import com.example.ShopEase.OrderItem.OrderItemRepository;
import com.example.ShopEase.Size.ProductSize;
import com.example.ShopEase.Size.ProductSizeRepository;
import com.example.ShopEase.User.User;
import com.example.ShopEase.User.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CartService {
    private CartRepository cartRepository;
    private UserRepository userRepository;
    private OrderItemRepository orderItemRepository;
    private ProductSizeRepository productSizeRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, OrderItemRepository orderItemRepository, ProductSizeRepository productSizeRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.productSizeRepository = productSizeRepository;
    }

    public String submitRemoveItemFromCart(Long orderItemId, Principal principal) {
        User user = userRepository.getUserByUsername(principal.getName());
        Cart cart = user.getCart();
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        ProductSize productSize = orderItem.getProductSize();
        productSize.setQuantity(productSize.getQuantity() + orderItem.getQuantity());
        cart.removeItem(orderItem);
        cartRepository.save(cart);
        productSizeRepository.save(productSize);
        return "redirect:/cart";
    }
}
