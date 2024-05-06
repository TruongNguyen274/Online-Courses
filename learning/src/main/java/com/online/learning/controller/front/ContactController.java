package com.online.learning.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/contact"})
public class ContactController {

    @GetMapping(value = {"", "/"})
    public String showAboutPage() {
        return "front/contact";
    }

}
