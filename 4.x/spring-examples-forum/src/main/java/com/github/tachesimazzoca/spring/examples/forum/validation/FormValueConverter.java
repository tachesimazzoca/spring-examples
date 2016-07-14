package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.springframework.util.MultiValueMap;

public interface FormValueConverter {
    MultiValueMap<String, String> toMultiValueMap();
}
