package com.example.ShopEase.OrderItem;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/order-items")
public class OrderItemController {
    private OrderItemService orderItemService;
    private OrderItemRepository orderItemRepository;

    public OrderItemController(OrderItemService orderItemService, OrderItemRepository orderItemRepository) {
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
    }

    @PostMapping("/submit/{productId}")
    public String getSubmitOrderItem(@PathVariable("productId") Long productId, @RequestParam("size") Long productSizeId, Model model, Principal principal) {
        return orderItemService.submitOrderItem(productId, productSizeId, model, principal);
    }
}
