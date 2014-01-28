package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;


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