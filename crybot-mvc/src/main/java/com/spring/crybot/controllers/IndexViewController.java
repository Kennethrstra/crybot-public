package com.spring.crybot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexViewController {

    @RequestMapping(value = "/index.html")
    public ModelAndView indexPage() {
        return new ModelAndView("index");
    }
}
