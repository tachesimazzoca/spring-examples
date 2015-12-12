package com.github.tachesimazzoca.spring.examples.overview;

import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;

import java.beans.PropertyEditorSupport;
import java.util.Properties;

import static org.junit.Assert.*;

public class PropertyEditorTest {
    public static class Department {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Employee {
        private String name;
        private Department department;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Department getDepartment() {
            return department;
        }

        public void setDepartment(Department department) {
            this.department = department;
        }
    }

    public static class DepartmentEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            Department dep = new Department();
            dep.setName(text);
            setValue(dep);
        }
    }

    @Test
    public void testDepartmentEditor() {
        BeanWrapper emp = new BeanWrapperImpl(new Employee());
        emp.setPropertyValue("name", "David");
        emp.setPropertyValue("department", "Laboratory");
        assertEquals("Laboratory", emp.getPropertyValue("department.name"));
    }

    public static class Item {
        private boolean active;
        private byte[] secret;
        private Properties options;

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public byte[] getSecret() {
            return secret;
        }

        public void setSecret(byte[] secret) {
            this.secret = secret;
        }

        public Properties getOptions() {
            return options;
        }

        public void setOptions(Properties options) {
            this.options = options;
        }
    }

    @Test
    public void testByteArrayPropertyEditor() {
        BeanWrapper item = new BeanWrapperImpl(new Item());
        item.setPropertyValue("secret", "dead-beef");
        assertEquals("dead-beef", new String((byte[]) item.getPropertyValue("secret")));
    }

    @Test
    public void testCustomBooleanEditor() {
        BeanWrapper item = new BeanWrapperImpl(new Item());
        String[] yesStrings = new String[]{
                CustomBooleanEditor.VALUE_1,
                CustomBooleanEditor.VALUE_TRUE,
                CustomBooleanEditor.VALUE_YES,
                CustomBooleanEditor.VALUE_ON
        };
        for (String yesString : yesStrings) {
            item.setPropertyValue("active", yesString);
            assertEquals(true, item.getPropertyValue("active"));
        }

        String[] noStrings = new String[]{
                CustomBooleanEditor.VALUE_0,
                CustomBooleanEditor.VALUE_FALSE,
                CustomBooleanEditor.VALUE_NO,
                CustomBooleanEditor.VALUE_OFF
        };
        for (String noString : noStrings) {
            item.setPropertyValue("active", noString);
            assertEquals(false, item.getPropertyValue("active"));
        }
    }

    @Test
    public void testPropertiesEditor() {
        BeanWrapper item = new BeanWrapperImpl(new Item());
        final String NEWLINE = String.format("%n");
        item.setPropertyValue("options",
                "meta.description=For sale" + NEWLINE +
                "meta.note=No responsibility" + NEWLINE);
        assertEquals("For sale", ((Properties) item.getPropertyValue("options")).get("meta.description"));
        assertEquals("No responsibility", ((Properties) item.getPropertyValue("options")).get("meta.note"));
    }
}
