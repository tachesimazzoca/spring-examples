package com.github.tachesimazzoca.spring.examples.forum.domain.shared;

public interface ValueObject<T> {
    boolean sameValueAs(T other);
}
