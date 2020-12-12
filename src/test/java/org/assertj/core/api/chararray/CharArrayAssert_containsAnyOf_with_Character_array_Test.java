package org.assertj.core.api.chararray;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.CharArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.CharArrayAssert;
import org.assertj.core.api.CharArrayAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link CharArrayAssert#containsAnyOf(Character[])}</code>.
 *
 * @author Lucero Garcia
 */
@DisplayName("CharArrayAssert containsAnyOf(Character[])")
class CharArrayAssert_containsAnyOf_with_Character_array_Test extends CharArrayAssertBaseTest{

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    Character[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.containsAnyOf(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
      .hasMessage(shouldNotBeNull("values").create());
  }

  @Override
  protected CharArrayAssert invoke_api_method() {
    return assertions.containsAnyOf(new Character[]{'b', 'c'});
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsAnyOf(getInfo(assertions), getActual(assertions), arrayOf('b', 'c'));
  }
}
