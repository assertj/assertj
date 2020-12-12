package org.assertj.core.api.chararray;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.CharArrays.arrayOf;

import org.assertj.core.api.CharArrayAssert;
import org.assertj.core.api.CharArrayAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link CharArrayAssert#containsOnly(Character[])}</code>.
 *
 * @author Lucero Garcia
 */
@DisplayName("CharacterArrayAssert containsOnly(Character[])")
public class CharArrayAssert_containsOnly_with_Character_array_Test extends CharArrayAssertBaseTest{

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    Character[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.contains(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
      .hasMessage(shouldNotBeNull("values").create());
  }

  @Override
  protected CharArrayAssert invoke_api_method() {
    return assertions.containsOnly(new Character[]{'a', 'b', 'c'});
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsOnly(getInfo(assertions), getActual(assertions), arrayOf('a', 'b', 'c'));
  }
}
