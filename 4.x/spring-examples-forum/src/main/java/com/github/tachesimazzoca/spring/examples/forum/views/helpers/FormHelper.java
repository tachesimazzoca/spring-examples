package com.github.tachesimazzoca.spring.examples.forum.views.helpers;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;

import javax.validation.ConstraintViolation;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FormHelper<T> {
    private final T form;
    private final Set<ConstraintViolation<T>> errors;
    private final List<String> messages;

    public FormHelper(T form) {
        this.form = form;
        this.errors = Collections.emptySet();
        this.messages = Collections.emptyList();
    }

    public FormHelper(T form, Set<ConstraintViolation<T>> errors) {
        this.form = form;
        this.errors = errors;
        this.messages = Collections.emptyList();
    }

    public FormHelper(T form, List<String> messages) {
        this.form = form;
        this.errors = Collections.emptySet();
        this.messages = messages;
    }

    public FormHelper(T form, Set<ConstraintViolation<T>> errors, List<String> messages) {
        this.form = form;
        this.errors = errors;
        this.messages = messages;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasMessages() {
        return !messages.isEmpty();
    }

    public T getForm() {
        return form;
    }

    public Set<ConstraintViolation<T>> getErrors() {
        return errors;
    }

    public List<String> getMessages() {
        return messages;
    }

    public Object get(String name) {
        return property(name, Object.class);
    }

    public String toHTMLInput(String type, String name) {
        return toHTMLInput(type, name, "");
    }

    public String toHTMLInput(String type, String name, String attr) {
        String v = asString(name);
        String attrStr = "";
        if (!attr.isEmpty()) {
            attrStr = " " + attr;
        }
        return String.format("<input type=\"%s\" name=\"%s\" value=\"%s\"%s>",
                type, name, StringEscapeUtils.escapeHtml(v), attrStr);
    }

    public String toHTMLTextarea(String name) {
        return toHTMLTextarea(name, "");
    }

    public String toHTMLTextarea(String name, String attr) {
        String v = asString(name);
        String attrStr = "";
        if (!attr.isEmpty()) {
            attrStr = " " + attr;
        }
        return String.format("<textarea name=\"%s\"%s>%s</textarea>",
                name, attrStr, StringEscapeUtils.escapeHtml(v));
    }

    public String toHTMLOptions(String name) {
        String v = asString(name);
        Map<String, String> m = asMap(name + "Map");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> option : m.entrySet()) {
            sb.append("<option value=\"");
            sb.append(StringEscapeUtils.escapeHtml(option.getKey()));
            sb.append("\"");
            if (v.equals(option.getKey()))
                sb.append(" selected=\"selected\"");
            sb.append(">");
            sb.append(StringEscapeUtils.escapeHtml(option.getValue()));
            sb.append("</option>");
        }
        return sb.toString();
    }

    private String asString(String name) {
        String v = property(name, String.class);
        if (v == null)
            v = "";
        return v;
    }

    private Map<String, String> asMap(String name) {
        @SuppressWarnings("unchecked")
        Map<String, String> v = (Map<String, String>) property(name, Map.class);
        if (v == null)
            v = Collections.emptyMap();
        return v;
    }

    @SuppressWarnings("unchecked")
    private <V> V property(String name, Class<V> type) {
        try {
            return (V) MethodUtils.invokeMethod(
                    form, "get" + StringUtils.capitalize(name), null);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
