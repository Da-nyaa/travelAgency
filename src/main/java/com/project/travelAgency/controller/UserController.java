package com.project.travelAgency.controller;

import com.project.travelAgency.dto.UserDto;
import com.project.travelAgency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    final static Logger logger = Logger.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping
    public String userList(Model model) {
        logger.info("Get UserList");
        model.addAttribute("users", userService.getAll());
        return "userList";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        logger.info("Create a new User");
        model.addAttribute("user", new UserDto());
        return "user";
    }

    @PostMapping("/new")
    public String saveUser(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        logger.info("Save a new User");
        if (bindingResult.hasErrors()) {
            return "error";
        }
        if (userService.save(userDto)) {
            return "redirect:/users";
        } else {
            model.addAttribute("user", userDto);
        }
        return "user";
    }
}
