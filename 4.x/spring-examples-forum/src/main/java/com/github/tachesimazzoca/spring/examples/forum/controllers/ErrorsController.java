package com.github.tachesimazzoca.spring.examples.forum.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/errors")
public class ErrorsController extends AbstractUserController {
    @RequestMapping(
            value = "/{name:session}",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String forbidden(@PathVariable String name) {
        return "errors/" + name;
    }
}
