package org.assertj.guava.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

import com.google.common.collect.Range;

public class RangeShouldBeOpenedInTheLowerBound extends BasicErrorMessageFactory {

  public static <T extends Comparable<T>> ErrorMessageFactory shouldHaveOpenedLowerBound(final Range<T> actual) {
    return new RangeShouldBeOpenedInTheLowerBound(
        "\nExpecting:\n<%s>\nto be opened in the lower bound\nbut is closed", actual);
  }

  /**
   * Creates a new </code>{@link org.assertj.core.error.BasicErrorMessageFactory}</code>.
   *
   * @param format the format string.
   * @param arguments arguments referenced by the format specifiers in the format string.
   */
  public RangeShouldBeOpenedInTheLowerBound(final String format, final Object... arguments) {
    super(format, arguments);
  }
}
