package org.assertj.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveOnlyElementsOfType.shouldHaveOnlyElementsOfType;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.Test;

/**
 * Tests for {@link ObjectArrayAssert#hasOnlyElementsOfType(Class)}.
 */
public class ObjectArrays_assertHasOnlyElementsOfType_Test extends ObjectArraysBaseTest {

  private static final Object[] array = { 6, 7.0, 8L };
 
  @Test
  public void should_pass_if_actual_has_only_elements_of_the_expected_type() {
	arrays.assertHasOnlyElementsOfType(someInfo(), array, Number.class);
  }

  @Test
  public void should_fail_if_actual_is_null() {
	thrown.expectAssertionError(actualIsNull());
	arrays.assertHasOnlyElementsOfType(someInfo(), null, Integer.class);
  }

  @Test
  public void should_throw_exception_if_expected_type_is_null() {
	thrown.expect(NullPointerException.class);
	arrays.assertHasOnlyElementsOfType(someInfo(), array, null);
  }

  @Test
  public void should_fail_if_one_element_in_actual_does_not_belong_to_the_expected_type() {
	try {
	  arrays.assertHasOnlyElementsOfType(someInfo(), array, Long.class);
	} catch (AssertionError e) {
	  assertThat(e).hasMessage(shouldHaveOnlyElementsOfType(array, Long.class, Integer.class).create());
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }

}