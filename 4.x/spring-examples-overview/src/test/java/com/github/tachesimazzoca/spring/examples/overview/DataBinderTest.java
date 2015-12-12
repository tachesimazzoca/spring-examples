package com.github.tachesimazzoca.spring.examples.overview;

import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DataBinderTest {
    public static class Item {
        private String name;
        private Date openedAt;
        private Date closedAt;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getOpenedAt() {
            return openedAt;
        }

        public void setOpenedAt(Date openedAt) {
            this.openedAt = openedAt;
        }

        public Date getClosedAt() {
            return closedAt;
        }

        public void setClosedAt(Date closedAt) {
            this.closedAt = closedAt;
        }
    }

    public class ItemValidator implements Validator {
        @Override
        public void validate(Object o, Errors errors) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
            Item item = (Item) o;
            if (null == item.getOpenedAt() || null == item.getClosedAt())
                return;
            if (item.getOpenedAt().getTime() > item.getClosedAt().getTime())
                errors.rejectValue("closedAt", "notInFuture");
        }

        @Override
        public boolean supports(Class<?> aClass) {
            return Item.class.equals(aClass);
        }
    }

    private DateFormat createDateFormat() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        df.setLenient(false);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df;
    }

    private DataBinder createItemDataBinder() {
        DataBinder binder = new DataBinder(new Item());
        binder.registerCustomEditor(Date.class, "openedAt",
                new CustomDateEditor(createDateFormat(), true));
        binder.registerCustomEditor(Date.class, "closedAt",
                new CustomDateEditor(createDateFormat(), true));
        return binder;
    }

    @Test
    public void testValidator() {
        DataBinder binder = createItemDataBinder();
        binder.setValidator(new ItemValidator());
        List<PropertyValue> values = new ArrayList<PropertyValue>();
        values.add(new PropertyValue("name", " "));
        values.add(new PropertyValue("openedAt", "2015-12-30 00:00:00"));
        values.add(new PropertyValue("closedAt", "2015-01-04 00:00:00"));
        binder.bind(new MutablePropertyValues(values));
        binder.validate();
        BindingResult result = binder.getBindingResult();
        assertEquals("required", result.getFieldError("name").getCode());
        assertEquals("notInFuture", result.getFieldError("closedAt").getCode());
    }

    @Test
    public void testCustomDateEditor() {
        DataBinder binder = createItemDataBinder();

        List<PropertyValue> values = new ArrayList<PropertyValue>();
        values.add(new PropertyValue("openedAt", "2015-01-01 00:00:00"));
        values.add(new PropertyValue("closedAt", "2015-01-31 23:59:59"));
        binder.bind(new MutablePropertyValues(values));
        BindingResult result = binder.getBindingResult();

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.clear();
        cal.set(2015, 0, 1, 0, 0, 0);
        assertEquals(cal.getTimeInMillis(),
                ((Item) result.getTarget()).getOpenedAt().getTime());

        cal.clear();
        cal.set(2015, 0, 31, 23 ,59 ,59);
        assertEquals(cal.getTimeInMillis(),
                ((Item) result.getTarget()).getClosedAt().getTime());
    }
}
