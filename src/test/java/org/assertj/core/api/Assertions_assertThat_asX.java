package org.assertj.core.api;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

/**
 * Tests for Assert.asX() methods
 */
public class Assertions_assertThat_asX {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void should_past_list_asserts_on_list_objects_with_asList() {
        Object listAsObject = Arrays.asList(1, 2, 3);
        assertThat(listAsObject).asList().isSorted();
    }

    @Test
    public void should_fail_list_asserts_on_non_list_objects_even_with_asList() {
        Object nonListAsObject = new Object();

        thrown.expectAssertionError("an instance of:\n <java.util.List>\nbut was instance of:\n <java.lang.Object>");
        assertThat(nonListAsObject).asList().isSorted();
    }

}
