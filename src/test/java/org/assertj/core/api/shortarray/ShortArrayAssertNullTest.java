package org.assertj.core.api.shortarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

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

    // GIVEN
    int[] nullContent = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> invoke_api_with_null_value(assertThat(new short[] {}), nullContent),
                                                    NullPointerException.class);
    // THEN
    assertThat(npe).hasMessage("The array of values to look for should not be null");
  }

  protected abstract void invoke_api_with_null_value(AbstractShortArrayAssert<?> emptyAssert, int[] nullArray);
}
