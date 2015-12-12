package com.github.tachesimazzoca.spring.examples.overview;

import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyEditorSupport;

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
}
