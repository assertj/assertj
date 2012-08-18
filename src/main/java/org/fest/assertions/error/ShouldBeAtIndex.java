package org.fest.assertions.error;

import java.util.List;

import org.fest.assertions.core.Condition;
import org.fest.assertions.data.Index;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains a value at a given index that
 * satisfies a <code>{@link Condition}</code> failed.
 * 
 * @author Bo Gotthardt
 */
public class ShouldBeAtIndex extends BasicErrorMessageFactory {
  /**
   * Creates a new </code>{@link ShouldBeAtIndex}</code>.
   * @param <T> guarantees that the type of the actual value and the generic type of the {@code Condition} are the same.
   * @param actual the actual value in the failed assertion.
   * @param condition the {@code Condition}.
   * @param index the index of the expected value.
   * @param found the value in {@code actual} stored under {@code index}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <T> ErrorMessageFactory shouldBeAtIndex(List<T> actual, Condition<? super T> condition, Index index, T found) {
    return new ShouldBeAtIndex(actual, condition, index, found);
  }

  private <T> ShouldBeAtIndex(List<T> actual, Condition<? super T> condition, Index index, T found) {
    super("expecting:<%s> at index <%s> to be:<%s> in:\n <%s>\n", found, index.value, condition, actual);
  }
}
