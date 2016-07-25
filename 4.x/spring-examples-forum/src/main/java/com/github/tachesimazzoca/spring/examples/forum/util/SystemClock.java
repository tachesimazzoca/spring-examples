package com.github.tachesimazzoca.spring.examples.forum.util;

public class SystemClock implements Clock {
    @Override
    public Long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
