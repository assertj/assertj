package org.assertj.guava.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

import com.google.common.collect.Range;

public class RangeShouldHaveLowerEndpointEqual extends BasicErrorMessageFactory {

  public static <T extends Comparable<T>> ErrorMessageFactory shouldHaveEqualLowerEndpoint(final Range<T> actual,
      final Object value, final Object actualEndpoint) {
    return new RangeShouldHaveLowerEndpointEqual(
        "\nExpecting:\n<%s>\nto have lower endpoint equal to:\n<%s>\nbut was\n<%s>", actual, value, actualEndpoint);
  }

  /**
   * Creates a new </code>{@link org.assertj.core.error.BasicErrorMessageFactory}</code>.
   *
   * @param format the format string.
   * @param arguments arguments referenced by the format specifiers in the format string.
   */
  public RangeShouldHaveLowerEndpointEqual(final String format, final Object... arguments) {
    super(format, arguments);
  }
}
