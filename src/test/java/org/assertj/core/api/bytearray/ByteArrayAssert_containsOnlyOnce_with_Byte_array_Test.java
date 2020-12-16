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
 * Tests for <code>{@link ByteArrayAssert#containsOnlyOnce(Byte[])}</code>.
 *
 * @author Lucero Garcia
 */
@DisplayName("ByteArrayAssert containsOnlyOnce(Byte[])")
class ByteArrayAssert_containsOnlyOnce_with_Byte_array_Test extends ByteArrayAssertBaseTest {

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    Byte[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.containsOnlyOnce(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
      .hasMessage(shouldNotBeNull("values").create());
  }

  @Override
  protected ByteArrayAssert invoke_api_method() {
    return assertions.containsOnlyOnce(new Byte[] {1, 2, 3});
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsOnlyOnce(getInfo(assertions), getActual(assertions), arrayOf(1, 2, 3)) ;
  }

}
