package org.assertj.guava.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

import com.google.common.collect.Range;

public class RangeShouldBeOpenedInTheUpperBound extends BasicErrorMessageFactory {

  public static <T extends Comparable<T>> ErrorMessageFactory shouldHaveOpenedUpperBound(final Range<T> actual) {
    return new RangeShouldBeOpenedInTheUpperBound(
        "\nExpecting:\n<%s>\nto be opened in the upper bound\nbut is closed", actual);
  }

  /**
   * Creates a new </code>{@link org.assertj.core.error.BasicErrorMessageFactory}</code>.
   *
   * @param format the format string.
   * @param arguments arguments referenced by the format specifiers in the format string.
   */
  public RangeShouldBeOpenedInTheUpperBound(final String format, final Object... arguments) {
    super(format, arguments);
  }
}
