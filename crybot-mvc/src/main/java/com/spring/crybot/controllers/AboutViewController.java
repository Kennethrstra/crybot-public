package com.spring.crybot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AboutViewController {

    @RequestMapping(value = "/about.html")
    public ModelAndView aboutPage() {
        return new ModelAndView("about");
    }
}
