package com.muntaqa.springcruddemo.controllers;

import com.muntaqa.springcruddemo.models.Order;
import com.muntaqa.springcruddemo.models.OrderDto;
import com.muntaqa.springcruddemo.models.OrderItem;
import com.muntaqa.springcruddemo.models.OrderItemDto;
import com.muntaqa.springcruddemo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/create")
    public String showOrderCreationForm(Model model) {
        model.addAttribute("order", new Order());
        return "order/CreateOrder";
    }

//    @PostMapping("/create")
//    public String createOrder(@ModelAttribute("order") Order order, RedirectAttributes redirectAttributes) {
//        Order createdOrder = orderService.createOrder(order);
//        redirectAttributes.addFlashAttribute("message", "Order created successfully!");
//        return "redirect:/orders/" + createdOrder.getId();
//    }

//    @PostMapping("/create")
//    public String createOrder(@ModelAttribute("order") OrderDto orderDto, RedirectAttributes redirectAttributes) {
//        try {
//            Order order = new Order();
//            order.setCustomerName(orderDto.getCustomerName());
//            order.setEmail(orderDto.getEmail());
//
//            List<OrderItem> orderItems = new ArrayList<>();
//
//            // Iterate through the OrderItemDtos and create OrderItem entities
//            for (OrderItem orderItemDto : orderDto.getOrderItems()) {
//                OrderItem orderItem = new OrderItem();
//                orderItem.setProductName(orderItemDto.getProductName());
//                orderItem.setQuantity(orderItemDto.getQuantity());
//                // Add the OrderItem to the list
//                orderItems.add(orderItem);
//            }
//
//            // Set the list of OrderItems to the Order entity
//            order.setOrderItems(orderItems);
//
//
//            Order createdOrder = orderService.createOrder(order);
//            redirectAttributes.addFlashAttribute("message", "Order created successfully!");
//            return "redirect:/orders/" + createdOrder.getId();
//        } catch (Exception ex) {
//            System.out.println("Exception: " + ex.getMessage());
//            redirectAttributes.addFlashAttribute("error", "Failed to create order.");
//            return "redirect:/orders/create";
//        }
//    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("order") OrderDto orderDto, RedirectAttributes redirectAttributes) {
        try {
            // Create a new Order entity from the OrderDto
            Order order = new Order();
            order.setCustomerName(orderDto.getCustomerName());
            order.setEmail(orderDto.getEmail());

            // Create a list to store OrderItems
            List<OrderItem> orderItems = new ArrayList<>();

            // Iterate through the OrderItemDto and create OrderItem entities
//            for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
            for (OrderItem orderItemDto : orderDto.getOrderItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductName(orderItemDto.getProductName());
                orderItem.setQuantity(orderItemDto.getQuantity());
                // Add the OrderItem to the list
                orderItems.add(orderItem);
            }

            // Set the list of OrderItems to the Order entity
            order.setOrderItems(orderItems);

            // Save the Order entity along with its associated OrderItems
            Order createdOrder = orderService.createOrder(order);

            // Add a success message to the redirect attributes
            redirectAttributes.addFlashAttribute("message", "Order created successfully!");

            // Redirect to the details page of the created order
            return "redirect:/orders/" + createdOrder.getId();
        } catch (Exception ex) {
            // Log the exception and add an error message to the redirect attributes
            System.out.println("Exception: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("error", "Failed to create order.");
            // Redirect back to the order creation form
            return "redirect:/orders/create";
        }
    }


    @GetMapping("/{orderId}")
    public String getOrderDetails(@PathVariable Integer orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getId());
            orderDto.setCustomerName(order.getCustomerName());
            orderDto.setEmail(order.getEmail());
            model.addAttribute("order", order);
            return "order/OrderDetails";
        } else {
            return "redirect:/orders";
        }
    }

    @GetMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable Integer orderId, RedirectAttributes redirectAttributes) {
        boolean deleted = orderService.deleteOrder(orderId);
        if (deleted) {
            redirectAttributes.addFlashAttribute("message", "Order deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete order.");
        }
        return "redirect:/orders";
    }
}
