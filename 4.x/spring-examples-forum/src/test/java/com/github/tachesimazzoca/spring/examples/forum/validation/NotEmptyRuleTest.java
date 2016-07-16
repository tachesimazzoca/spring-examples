package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-application.xml")
public class NotEmptyRuleTest {
    @Test
    public void testCheck() {
        final Rule rule = new NotEmptyRule("username", "NotEmpty.username");

        final String[] values = new String[] { null, "", " ", "\n\n", "\t", "　" };
        for (String value : values) {
            final Map<String, Object> m = new HashMap<>();
            m.put("username", value);
            final Errors errors = new MapBindingResult(m, "test");
            assertEquals(0, errors.getErrorCount());

            rule.check(new Object(), errors);
            assertTrue(errors.hasErrors());
            assertTrue(errors.hasFieldErrors());
            assertEquals("NotEmpty.username", errors.getFieldError("username").getCode());
        }
    }
}
