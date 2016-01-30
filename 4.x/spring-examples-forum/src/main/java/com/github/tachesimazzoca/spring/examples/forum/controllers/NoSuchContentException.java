package com.github.tachesimazzoca.spring.examples.forum.controllers;

public class NoSuchContentException extends RuntimeException {
    private final String navigateTo;

    public NoSuchContentException() {
        this.navigateTo = null;
    }

    public NoSuchContentException(String navigateTo) {
        this.navigateTo = navigateTo;
    }

    public String getNavigateTo() {
        return navigateTo;
    }
}
