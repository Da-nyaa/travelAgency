package com.project.travelAgency.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StartController {

    final static Logger logger = Logger.getLogger(StartController.class);

    @RequestMapping({"", "/"})
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        logger.info("Redirect in 404-page in case of wrong login or password ");
        model.addAttribute("loginError", true);
        return "login";
    }
}
