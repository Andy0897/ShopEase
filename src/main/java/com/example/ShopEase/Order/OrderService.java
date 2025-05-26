package com.example.ShopEase.Order;

import com.example.ShopEase.Cart.Cart;
import com.example.ShopEase.Cart.CartRepository;
import com.example.ShopEase.User.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.ArrayList;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private CartRepository cartRepository;


    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public String submitOrder(Order order, BindingResult bindingResult, Principal principal, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("order", order);
            return "order/checkout";
        }
        Cart cart = userRepository.getUserByUsername(principal.getName()).getCart();
        cart.setItems(new ArrayList<>());

        cartRepository.save(cart);
        orderRepository.save(order);
        return "redirect:/orders/thank-you";
    }

    public String submitUpdateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).get();
        order.setOrderStatus(status);
        orderRepository.save(order);
        return "redirect:/orders/details/" + orderId;
    }
}
