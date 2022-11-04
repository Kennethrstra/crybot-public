package com.spring.crybot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContactViewController {

    @RequestMapping(value = "/contact.html")
    public ModelAndView contactPage() {
        return new ModelAndView("contact");
    }
}
