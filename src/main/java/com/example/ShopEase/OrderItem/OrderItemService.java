package com.example.ShopEase.OrderItem;

import com.example.ShopEase.Cart.Cart;
import com.example.ShopEase.Cart.CartRepository;
import com.example.ShopEase.Product.Product;
import com.example.ShopEase.Product.ProductRepository;
import com.example.ShopEase.User.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    private OrderItemRepository orderItemRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public String submitOrderItem(Long productId, Model model, Principal principal) {
        Product product = productRepository.findById(productId).get();
        Cart cart = userRepository.getUserByUsername(principal.getName()).getCart();

        if (cart.getItems().stream().anyMatch(orderItem -> orderItem.getProduct() == product)) {
            OrderItem productOrderItem = cart.getItems().stream().filter(orderItem -> orderItem.getProduct() == product).collect(Collectors.toUnmodifiableList()).get(0);
            productOrderItem.setQuantity(productOrderItem.getQuantity() + 1);
            productOrderItem.setTotalPrice(productOrderItem.getTotalPrice() + product.getPrice());
            orderItemRepository.save(productOrderItem);
        } else {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(1);
            orderItem.setTotalPrice(product.getPrice());
            cart.addItem(orderItem);
            cartRepository.save(cart);
        }

        product.setQuantity(product.getQuantity() - 1);
        productRepository.save(product);
        return "redirect:/products/" + productId;
    }
}