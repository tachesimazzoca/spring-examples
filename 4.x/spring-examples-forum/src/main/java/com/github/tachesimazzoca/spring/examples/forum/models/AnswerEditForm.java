package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;

import java.util.Map;

import static com.github.tachesimazzoca.spring.examples.forum.util.ParameterUtils.params;

@Data
public class AnswerEditForm {
    private Answer answer;
    private Question question;
    private String body;
    private Integer status;

    private static final Map<String, Object> statusMap = params(
            String.valueOf(Answer.Status.PUBLISHED.getValue()),
            Answer.Status.PUBLISHED.getLabel(),
            String.valueOf(Answer.Status.DRAFT.getValue()),
            Answer.Status.DRAFT.getLabel());

    public Map<String, Object> getStatusMap() {
        return statusMap;
    }

    public static String[] getAllowedFields() {
        return new String[]{"body", "status"};
    }
}
