package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;

import java.util.Map;

import static com.github.tachesimazzoca.spring.examples.forum.util.ParameterUtils.params;

@Data
public class QuestionsEditForm {
    private Question question;
    private String subject;
    private String body;
    private Integer status;

    private static final Map<String, Object> statusMap = params(
            String.valueOf(Question.Status.PUBLISHED.getValue()),
            Question.Status.PUBLISHED.getLabel(),
            String.valueOf(Question.Status.DRAFT.getValue()),
            Question.Status.DRAFT.getLabel());

    public Map<String, Object> getStatusMap() {
        return statusMap;
    }

    public static String[] getAllowedFields() {
        return new String[]{"subject", "body", "status"};
    }
}
