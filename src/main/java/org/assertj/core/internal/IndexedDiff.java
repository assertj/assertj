package org.assertj.core.internal;

/**
 * The actual and expected elements at a given index.
 * */
public class IndexedDiff {
  private final Object actual;
  private final Object expected;
  private final int index;

  /**
   * Create a {@link IndexedDiff}.
   * @param actual the actual value of the diff.
   * @param expected the expected value of the diff.
   * @param index the index the diff occurred at.
   */
  public IndexedDiff(Object actual, Object expected, int index) {
    this.actual = actual;
    this.expected = expected;
    this.index = index;
  }

  /**
   * Get the actual value of the diff.
   * @return {@link Object} containing the actual value of the diff.
   */
  public Object getActual() {
    return actual;
  }

  /**
   * Get the expected value of the diff.
   * @return {@link Object} containing the expected value of the diff.
   */
  public Object getExpected() {
    return expected;
  }

  /**
   * Get the index the diff occurred at.
   * @return {@code int} containing the index the diff occurred at.
   */
  public int getIndex() {
    return index;
  }
}
