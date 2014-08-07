package org.assertj.core.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public class FieldByFieldComparisonStrategy_areEqual_Test {

    private FieldByFieldComparisonStrategy fieldByFieldComparisonStrategy;

    @Before
    public void setUp() {
        fieldByFieldComparisonStrategy = new FieldByFieldComparisonStrategy();
    }

    @Test
    public void should_return_true_if_both_Objects_are_null() {
        assertTrue(fieldByFieldComparisonStrategy.areEqual(null, null));
    }

    @Test
    public void should_return_true_if_Objects_are_equal() {
        assertTrue(fieldByFieldComparisonStrategy.areEqual(new JarJar("Yoda"), new JarJar("Yoda")));
    }

    @Test
    public void should_return_false_if_Objects_are_not_equal() {
        assertFalse(fieldByFieldComparisonStrategy.areEqual(new JarJar("Yoda"), new JarJar("HanSolo")));
    }

    @Test
    public void should_return_are_not_equal_if_first_Object_is_null_and_second_is_not() {
        assertFalse(fieldByFieldComparisonStrategy.areEqual(null, new JarJar("Yoda")));
    }

    @Test
    public void should_return_are_not_equal_if_second_Object_is_null_and_first_is_not() {
        assertFalse(fieldByFieldComparisonStrategy.areEqual(new JarJar("Yoda"), null));
    }

    @Test
    public void should_return_are_not_equal_if_Objects_are_not_equal() {
        assertFalse(fieldByFieldComparisonStrategy.areEqual(new JarJar("Yoda"), 2));
    }

    public static class JarJar {

        public final String field;

        public JarJar(String field) {
            this.field = field;
        }

    }


}