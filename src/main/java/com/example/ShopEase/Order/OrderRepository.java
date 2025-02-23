package com.example.ShopEase.Order;

import com.example.ShopEase.User.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    public List<Order> findAllByUser(User user);
}
