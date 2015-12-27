package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PagesController extends AbstractUserController {
    @Autowired
    private Config config;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return pages("index");
    }

    @RequestMapping(
            value = "/pages/{name:[-_0-9a-zA-Z]+}.html",
            method = RequestMethod.GET)
    public String pages(@PathVariable("name") String name) {
        return "pages/" + name;
    }
}
