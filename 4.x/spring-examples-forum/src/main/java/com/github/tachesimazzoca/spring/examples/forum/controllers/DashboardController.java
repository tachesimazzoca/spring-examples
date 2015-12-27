package com.github.tachesimazzoca.spring.examples.forum.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DashboardController extends AbstractUserController {
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String index() {
        return "dashboard/index";
    }
}
