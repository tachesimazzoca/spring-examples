package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Map;

public class FormValidator implements Validator {
    private Map<String, List<Rule>> ruleMap;

    public FormValidator(Map<String, List<Rule>> ruleMap) {
        this.ruleMap = ruleMap;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(FormValueConverter.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        FormValueConverter form = (FormValueConverter) o;
        MultiValueMap<String, String> mvm = form.toMultiValueMap();
        for (Map.Entry<String, List<String>> entry : mvm.entrySet()) {
            List<Rule> ruleList = ruleMap.get(entry.getKey());
            for (Rule rule : ruleList) {
                rule.check(entry.getKey(), entry.getValue(), form, errors);
            }
        }
    }
}
