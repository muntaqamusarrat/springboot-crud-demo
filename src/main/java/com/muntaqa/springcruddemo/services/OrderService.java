package com.muntaqa.springcruddemo.services;

import com.muntaqa.springcruddemo.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

//    public Order updateOrder(Integer orderId, Order updatedOrder) {
//        Order existingOrder = orderRepository.findById(orderId).orElse(null);
//        if (existingOrder != null) {
//            return orderRepository.save(existingOrder);
//        }
//        return null;
//    }

    public Order updateOrder(Integer orderId, Order updatedOrder) {
        Order existingOrder = orderRepository.findById(orderId).orElse(null);
        if (existingOrder != null) {
            existingOrder.setCustomerName(updatedOrder.getCustomerName());
            existingOrder.setEmail(updatedOrder.getEmail());
            existingOrder.setOrderItems(updatedOrder.getOrderItems());

            return orderRepository.save(existingOrder);
        }
        return null;
    }
    public boolean deleteOrder(Integer orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }

}
