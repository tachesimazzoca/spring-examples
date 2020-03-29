package com.github.tachesimazzoca.spring.examples.forum.domain.shared;

public interface Identifiable<T> {
    boolean sameIdentityAs(T other);
}
