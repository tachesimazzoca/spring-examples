package com.github.tachesimazzoca.spring.examples.forum.controllers;

public class UserSessionException extends RuntimeException {
    private final String returnTo;

    public UserSessionException() {
        this.returnTo = null;
    }

    public UserSessionException(String returnTo) {
        this.returnTo = returnTo;
    }

    public String getReturnTo() {
        return returnTo;
    }
}
