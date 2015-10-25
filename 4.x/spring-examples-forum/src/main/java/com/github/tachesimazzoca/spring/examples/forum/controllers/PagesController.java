package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PagesController {
    @Autowired
    private Config config;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        return pages("index", model);
    }

    @RequestMapping(
            value = "/pages/{name:[-_0-9a-zA-Z]+}.html",
            method = RequestMethod.GET)
    public String pages(@PathVariable("name") String name, Model model) {
        return "pages/" + name;
    }
}
