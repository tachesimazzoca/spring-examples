package com.github.tachesimazzoca.spring.examples.overview;

import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BeanWrapperTest {
    public class Company {
        private String[] phoneNumbers;
        private Map<String, Department> departmentMap;

        public String[] getPhoneNumbers() {
            return phoneNumbers;
        }

        public void setPhoneNumbers(String[] phoneNumbers) {
            this.phoneNumbers = phoneNumbers;
        }

        public Map<String, Department> getDepartmentMap() {
            return departmentMap;
        }

        public void setDepartmentMap(Map<String, Department> departmentMap) {
            this.departmentMap = departmentMap;
        }
    }

    public class Department {
        private String name;
        private List<Employee> employeeList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Employee> getEmployeeList() {
            return employeeList;
        }

        public void setEmployeeList(List<Employee> employeeList) {
            this.employeeList = employeeList;
        }
    }

    public class Employee {
        private final String name;

        public Employee(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    public void testNestedProperties() {
        Map<String, Department> depMap = new HashMap<String, Department>();

        depMap.put("rd", new Department());
        depMap.get("rd").setName("Research & Development");

        depMap.put("hr", new Department());
        BeanWrapper hr = new BeanWrapperImpl(depMap.get("hr"));
        hr.setPropertyValue("name", "Human Resource");
        List<Employee> empList = new ArrayList<Employee>();
        empList.add(new Employee("Alice"));
        empList.add(new Employee("Bob"));
        empList.add(new Employee("Chris"));
        hr.setPropertyValue("employeeList", empList);

        assertEquals("Human Resource", hr.getPropertyValue("name"));
        assertEquals("Alice", hr.getPropertyValue("employeeList[0].name"));
        assertEquals("Bob", hr.getPropertyValue("employeeList[1].name"));
        assertEquals("Chris", hr.getPropertyValue("employeeList[2].name"));

        BeanWrapper company = new BeanWrapperImpl(new Company());
        company.setPropertyValue("phoneNumbers", new String[]{"01-234-1111", "01-234-2222"});
        company.setPropertyValue("departmentMap", depMap);

        assertEquals("01-234-1111", company.getPropertyValue("phoneNumbers[0]"));
        assertEquals("01-234-2222", company.getPropertyValue("phoneNumbers[1]"));
        assertEquals("Research & Development", company.getPropertyValue("departmentMap[rd].name"));
        assertEquals("Human Resource", company.getPropertyValue("departmentMap[hr].name"));
        assertEquals("Alice", company.getPropertyValue("departmentMap[hr].employeeList[0].name"));
        assertEquals("Bob", company.getPropertyValue("departmentMap[hr].employeeList[1].name"));
        assertEquals("Chris", company.getPropertyValue("departmentMap[hr].employeeList[2].name"));
    }
}
