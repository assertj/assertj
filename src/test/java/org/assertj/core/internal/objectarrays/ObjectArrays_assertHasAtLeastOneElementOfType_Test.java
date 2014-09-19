package org.assertj.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveAtLeastOneElementOfType.shouldHaveAtLeastOneElementOfType;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.Test;

public class ObjectArrays_assertHasAtLeastOneElementOfType_Test extends ObjectArraysBaseTest {

  private static final Object[] array = { 6, "Hello" };
 
  @Test
  public void should_pass_if_actual_has_one_element_of_the_expected_type() {
	arrays.assertHasAtLeastOneElementOfType(someInfo(), array, Integer.class);
	arrays.assertHasAtLeastOneElementOfType(someInfo(), array, String.class);
	arrays.assertHasAtLeastOneElementOfType(someInfo(), array, Object.class);
  }

  @Test
  public void should_fail_if_actual_is_null() {
	thrown.expectAssertionError(actualIsNull());
	arrays.assertHasAtLeastOneElementOfType(someInfo(), null, Integer.class);
  }

  @Test
  public void should_throw_exception_if_expected_type_is_null() {
	thrown.expect(NullPointerException.class);
	arrays.assertHasAtLeastOneElementOfType(someInfo(), array, null);
  }

  @Test
  public void should_fail_if_no_elements_in_actual_belongs_to_the_expected_type() {
	try {
	  arrays.assertHasAtLeastOneElementOfType(someInfo(), array, Float.class);
	} catch (AssertionError e) {
	  assertThat(e).hasMessage(shouldHaveAtLeastOneElementOfType(array, Float.class).create());
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }

}