package com.project.travelAgency.controller;

import com.project.travelAgency.model.entity.Order;
import com.project.travelAgency.model.repository.OrderRepository;
import com.project.travelAgency.service.CartService;
import com.project.travelAgency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final CartService cartService;
    private final UserService userService;
    private final OrderRepository orderRepository;

    @GetMapping
    public String listOrders(Model model,Principal principal) {
        List<Order> orders = orderRepository.findAllByUserName(principal.getName());
        model.addAttribute("orders", orders);
        return "order";
    }

    @PostMapping
    public String commitCart(Principal principal, Model model) {
        if (principal != null) {
            cartService.commitCartToOrder(principal.getName());
            userService.getDiscount(principal.getName());
            model.addAttribute("orders", orderRepository.findAllByUserName(principal.getName()));
        }
        return "order";
    }
}
