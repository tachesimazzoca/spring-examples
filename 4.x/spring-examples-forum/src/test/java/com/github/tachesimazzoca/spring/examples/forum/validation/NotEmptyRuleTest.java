package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-application.xml")
public class NotEmptyRuleTest {
    @Test
    public void testCheck() {
        final Checker checker = new NotEmptyChecker();
        final String[] values = new String[] { null, "", " ", "\n\n", "\t", "　" };
        for (String value : values) {
            assertFalse(checker.check(value, null, null));
        }
        assertFalse(checker.check(Collections.<String>emptyList(), null, null));
        assertTrue(checker.check(".", null, null));
        assertTrue(checker.check(Arrays.asList("."), null, null));
    }
}
