package com.github.tachesimazzoca.spring.examples.forum.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/recovery")
public class RecoveryController extends AbstractUserController {
    @RequestMapping(value="/errors/session", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String errorsSession() {
        return "recovery/errors/session";
    }
}