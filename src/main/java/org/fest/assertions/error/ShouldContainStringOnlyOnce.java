package org.fest.assertions.error;

import org.fest.assertions.internal.*;

/**
 * Creates an error message indicating that an assertion that verifies that a {@code String} contains another {@code String} only
 * once failed.
 * 
 * @author Pauline Iogna
 * @author Joel Costigliola
 */
public class ShouldContainStringOnlyOnce extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainStringOnlyOnce}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param sequence the String expected to be in {@code actual} only once.
   * @param occurences the number of occurences of sequence in actual.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnlyOnce(String actual, String sequence, int occurences,
      ComparisonStrategy comparisonStrategy) {
    if (occurences == 0) return new ShouldContainStringOnlyOnce(actual, sequence, comparisonStrategy);
    return new ShouldContainStringOnlyOnce(actual, sequence, occurences, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainStringOnlyOnce}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param sequence the String expected to be in {@code actual} only once.
   * @param occurences the number of occurences of sequence in actual.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnlyOnce(String actual, String sequence, int occurences) {
    if (occurences == 0) return new ShouldContainStringOnlyOnce(actual, sequence, StandardComparisonStrategy.instance());
    return new ShouldContainStringOnlyOnce(actual, sequence, occurences, StandardComparisonStrategy.instance());
  }

  private ShouldContainStringOnlyOnce(String actual, String expected, int occurences, ComparisonStrategy comparisonStrategy) {
    super("expecting:\n<%s>\n to appear only once in:\n<%s>\n but it appeared %s times%s.", expected, actual,
        occurences,
        comparisonStrategy);
  }

  private ShouldContainStringOnlyOnce(String actual, String expected, ComparisonStrategy comparisonStrategy) {
    super("expecting:\n<%s>\n to appear only once in:\n<%s>\n but it did not appear%s.", expected, actual,
        comparisonStrategy);
  }
}
