package com.example.ShopEase.User;

import com.example.ShopEase.Cart.Cart;
import com.example.ShopEase.Cart.CartRepository;
import com.example.ShopEase.Order.Order;
import com.example.ShopEase.Order.OrderRepository;
import com.example.ShopEase.OrderItem.OrderItem;
import com.example.ShopEase.OrderItem.OrderItemRepository;
import com.example.ShopEase.Product.Product;
import com.example.ShopEase.Size.ProductSize;
import com.example.ShopEase.Size.ProductSizeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Service
public class
UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private CartRepository cartRepository;
    private OrderItemRepository orderItemRepository;
    private ProductSizeRepository productSizeRepository;
    private OrderRepository orderRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, CartRepository cartRepository, OrderItemRepository orderItemRepository, ProductSizeRepository productSizeRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
        this.productSizeRepository = productSizeRepository;
        this.orderRepository = orderRepository;
    }

    public String submitUser(UserDTO userDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors() || checkIfExistsUserByUsername(userDTO.getUsername()) || checkIfExistsUserByEmail(userDTO.getEmail())) {
            bindingResult.getFieldErrors("username").forEach(System.out::println);
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("existsUserByUsername", checkIfExistsUserByUsername(userDTO.getUsername()));
            model.addAttribute("existsUserByEmail", checkIfExistsUserByEmail(userDTO.getEmail()));
            return "sign-up";
        }
        User user = userMapper.toEntity(userDTO);
        cartRepository.save(user.getCart());
        userRepository.save(user);
        return "redirect:/sign-in";
    }

    private boolean checkIfExistsUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username);
        return user != null;
    }

    private boolean checkIfExistsUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        return user != null;
    }

    @Transactional
    public String submitDeleteUser(Principal principal) {
        User user = userRepository.getUserByUsername(principal.getName());
        Cart cart = user.getCart();

        if (cart != null) {
            if(!cart.getItems().isEmpty()) {
                for (OrderItem orderItem : cart.getItems()) {
                    System.out.println(orderItem != null);
                    Product product = orderItem.getProduct();

                    ProductSize productSize = ((List<ProductSize>)productSizeRepository.findAll()).stream()
                            .filter(size -> size.getProduct().getId().equals(product.getId())
                                    && size.getId().equals(orderItem.getProductSize().getId()))
                            .findFirst()
                            .orElse(null);

                    if (productSize != null) {
                        productSize.setQuantity(productSize.getQuantity() + orderItem.getQuantity());
                        productSizeRepository.save(productSize);
                    }
                }
                orderItemRepository.deleteAll(cart.getItems());
            }
            user.setCart(null);
            userRepository.save(user);

            cartRepository.deleteById(cart.getId());
        }

        if(!orderRepository.findAllByUser(user).isEmpty()) {
            List<Order> userOrders = orderRepository.findAllByUser(user);
            for (Order order : userOrders) {
                System.out.println(order.getOrderItems().stream().anyMatch(Objects::isNull));
                orderItemRepository.deleteAll(order.getOrderItems());
                order.setOrderItems(null);
                orderRepository.save(order);
            }
            orderRepository.deleteAll(userOrders);
        }

        userRepository.delete(user);

        return "redirect:/logout";
    }
}
