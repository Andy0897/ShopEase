package com.example.ShopEase.Cart;

import com.example.ShopEase.ImageEncoder;
import com.example.ShopEase.OrderItem.OrderItem;
import com.example.ShopEase.User.User;
import com.example.ShopEase.User.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartController {
    private UserRepository userRepository;
    private CartService cartService;

    public CartController(UserRepository userRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    @GetMapping
    public String getShowCart(Model model, Principal principal) {
        User user = userRepository.getUserByUsername(principal.getName());
        double totalPrice = user.getCart().getItems().stream().mapToDouble(OrderItem::getTotalPrice).sum();
        model.addAttribute("cart", user.getCart());
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("encoder", new ImageEncoder());
        return "cart/show";
    }

    @PostMapping("/remove-item/{orderItemId}")
    public String getSubmitRemoveItem(@PathVariable("orderItemId") Long orderItemId, Principal principal) {
        return cartService.submitRemoveItemFromCart(orderItemId, principal);
    }
}
