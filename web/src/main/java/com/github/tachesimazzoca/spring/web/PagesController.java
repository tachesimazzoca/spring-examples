package com.github.tachesimazzoca.spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/pages")
public class PagesController {
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String help(@PathVariable String name, Model model) {
        model.addAttribute("message", "Hello JstlView");
        return "pages/" + name;
    }
}
