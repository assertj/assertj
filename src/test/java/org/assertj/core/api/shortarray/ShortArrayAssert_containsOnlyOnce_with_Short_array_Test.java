package org.assertj.core.api.shortarray;

import org.assertj.core.api.ShortArrayAssert;
import org.assertj.core.api.ShortArrayAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.ShortArrays.arrayOf;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link ShortArrayAssert#containsOnlyOnce(Short[])}</code>.
 *
 * @author Lucero Garcia
 */
@DisplayName("ShortArrayAssert containsOnlyOnce(Short[])")
public class ShortArrayAssert_containsOnlyOnce_with_Short_array_Test extends ShortArrayAssertBaseTest {

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    Short[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.containsOnlyOnce(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
      .hasMessage(shouldNotBeNull("values").create());
  }

  @Override
  protected ShortArrayAssert invoke_api_method() {
    return assertions.containsOnlyOnce(new Short[]{ 1, 2 });
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsOnlyOnce(getInfo(assertions), getActual(assertions), arrayOf(1, 2));
  }

}
