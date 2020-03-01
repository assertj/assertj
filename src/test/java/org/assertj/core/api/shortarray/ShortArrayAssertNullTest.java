package org.assertj.core.api.shortarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.assertj.core.api.AbstractShortArrayAssert;
import org.assertj.core.api.ShortArrayAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Adds a test to verify null behavior against empty arrays.
 * 
 * Should be used for methods with an int[] or int... parameter.
 * 
 * @author Dan Avila
 */
public abstract class ShortArrayAssertNullTest extends ShortArrayAssertBaseTest {

  @Test
  public void should_throw_exception_on_null_argument() {
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
      int[] content = null;
      invoke_api_with_null_value(assertThat(new short[] {}), content);
    }).withMessage("The array of values to look for should not be null");
  }

  protected abstract void invoke_api_with_null_value(AbstractShortArrayAssert<?> emptyAssert, int[] nullArray);
}
