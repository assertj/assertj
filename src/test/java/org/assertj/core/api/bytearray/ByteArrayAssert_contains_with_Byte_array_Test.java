package org.assertj.core.api.bytearray;

import org.assertj.core.api.ByteArrayAssert;
import org.assertj.core.api.ByteArrayAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.ByteArrays.arrayOf;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link ByteArrayAssert#contains(Byte[])}</code>.
 *
 * @author Lucero Garcia
 */
@DisplayName("ByteArrayAssert contains(Byte[])")
class ByteArrayAssert_contains_with_Byte_array_Test extends ByteArrayAssertBaseTest {

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    Byte[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.contains(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
      .hasMessage(shouldNotBeNull("values").create());
  }

  @Override
  protected ByteArrayAssert invoke_api_method() {
    return assertions.contains(new Byte[] {3, 1});
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContains(getInfo(assertions), getActual(assertions), arrayOf(3, 1)) ;
  }

}
