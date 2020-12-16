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
 * Tests for <code>{@link ShortArrayAssert#doesNotContain(Short[])}</code>.
 *
 * @author Lucero Garcia
 */
@DisplayName("ShortArrayAssert doesNotContain(Short[])")
public class ShortArrayAssert_doesNotContain_with_Short_array_Test extends ShortArrayAssertBaseTest {

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    Short[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.doesNotContain(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
      .hasMessage(shouldNotBeNull("sequence").create());
  }

  @Override
  protected ShortArrayAssert invoke_api_method() {
    return assertions.doesNotContain(new Short[]{ 4 });
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertDoesNotContain(getInfo(assertions), getActual(assertions), arrayOf(4));
  }

}
