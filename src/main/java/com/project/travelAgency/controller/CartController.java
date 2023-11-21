package com.project.travelAgency.controller;


import com.project.travelAgency.dto.CartDto;
import com.project.travelAgency.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/cart")
    public String forOrder(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("cart", new CartDto());
        } else {
            CartDto cardDto = cartService.getCartByUser(principal.getName());
            model.addAttribute("cart", cardDto);
        }
        return "cart";
    }

    @GetMapping("/cart/{id}/deleteTourFromCart")
    private String deleteTourFromCart(@PathVariable Long id, Principal principal) {
        cartService.deleteTourByUser(principal.getName(), id);
        return "redirect:/cart";
    }
}
