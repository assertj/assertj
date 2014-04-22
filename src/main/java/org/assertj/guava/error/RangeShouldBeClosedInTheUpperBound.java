package org.assertj.guava.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

import com.google.common.collect.Range;

public class RangeShouldBeClosedInTheUpperBound extends BasicErrorMessageFactory {

  public static <T extends Comparable<T>> ErrorMessageFactory shouldHaveClosedUpperBound(final Range<T> actual) {
    return new RangeShouldBeClosedInTheUpperBound(
        "\nExpecting:\n<%s>\nto be closed in the upper bound\nbut is opened", actual);
  }

  /**
   * Creates a new </code>{@link org.assertj.core.error.BasicErrorMessageFactory}</code>.
   *
   * @param format the format string.
   * @param arguments arguments referenced by the format specifiers in the format string.
   */
  public RangeShouldBeClosedInTheUpperBound(final String format, final Object... arguments) {
    super(format, arguments);
  }
}
