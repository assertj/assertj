package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.data.Index;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.DoubleArrays;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractDoubleArrayAssert<S extends AbstractDoubleArrayAssert<S>>
    extends AbstractAssert<S, double[]>
    implements EnumerableAssert<AbstractDoubleArrayAssert<S>, Double>,
    ArraySortedAssert<AbstractDoubleArrayAssert<S>, Double> {

  @VisibleForTesting
  protected DoubleArrays arrays = DoubleArrays.instance();

  public AbstractDoubleArrayAssert(double[] actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  @Override
  public void isNullOrEmpty() {
    arrays.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public void isEmpty() {
    arrays.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public S isNotEmpty() {
    arrays.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Examples :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).hasSize(3);
   * 
   * // assertions will fail
   * assertThat(new double[] { 1.0, 2.0, 1.0 }).hasSize(2);
   * </pre>
   * 
   * </p>
   */
  @Override
  public S hasSize(int expected) {
    arrays.assertHasSize(info, actual, expected);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Examples :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).hasSameSizeAs(new int[] { 1, 2, 3 });
   * 
   * // assertions will fail
   * assertThat(new double[] { 1.0, 2.0, 1.0 }).hasSameSizeAs(new int[] { 1, 2 });
   * </pre>
   * 
   * </p>
   */
  @Override
  public S hasSameSizeAs(Object[] other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Examples :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).hasSameSizeAs(Arrays.asList(1, 2, 3));
   * 
   * // assertions will fail
   * assertThat(new double[] { 1.0, 2.0, 1.0 }).hasSameSizeAs(Arrays.asList(1, 2);
   * </pre>
   * 
   * </p>
   */
  @Override
  public S hasSameSizeAs(Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values, in any order.
   * <p>
   * Examples :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).contains(1.0, 3.0, 2.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).contains(3.0, 1.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).contains(1.0, 2.0);
   * 
   * // assertions will fail
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).contains(1.0, 4.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).contains(4.0, 7.0);
   * </pre>
   * 
   * </p>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   */
  public S contains(double... values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * Examples :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsOnly(1.0, 2.0, 3.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsOnly(2.0, 3.0, 1.0);
   * 
   * // assertions will fail
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsOnly(1.0, 4.0, 2.0, 3.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsOnly(4.0, 7.0);
   * </pre>
   * 
   * </p>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  public S containsOnly(double... values) {
    arrays.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * Examples :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsOnlyOnce(1.0, 2.0);
   * 
   * // assertions will fail
   * assertThat(new double[] { 1.0, 2.0, 1.0 }).containsOnlyOnce(1.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsOnlyOnce(4.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0, 3.0 }).containsOnlyOnce(0.0, 1.0, 2.0, 3.0, 4.0, 5.0);
   * </pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   */
  public S containsOnlyOnce(double... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Examples :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsSequence(1.0, 2.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsSequence(1.0, 2.0, 3.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsSequence(2.0, 3.0);
   * 
   * // assertions will fail
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsSequence(1.0, 3.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsSequence(4.0, 7.0);
   * </pre>
   * 
   * </p>
   * 
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public S containsSequence(double... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Examples :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsSubsequence(1.0, 2.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsSubsequence(1.0, 2.0, 3.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsSubsequence(2.0, 3.0);
   * 
   * // assertions will fail
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsSubsequence(1.0, 3.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsSubsequence(4.0, 7.0);
   * </pre>
   * 
   * </p>
   * 
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  public S containsSubsequence(double... subsequence) {
    arrays.assertContainsSubsequence(info, actual, subsequence);
    return myself;
  }
  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).contains(1.0, atIndex(O));
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).contains(3.0, atIndex(2));
   * 
   * // assertion will fail
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).contains(1.0, atIndex(1));
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).contains(4.0, atIndex(2));
   * </pre>
   * 
   * </p>
   * 
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual array.
   * @throws AssertionError if the actual array does not contain the given value at the given index.
   */
  public S contains(double value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).doesNotContain(4.0, 8.0);
   * 
   * // assertion will fail
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).doesNotContain(1.0, 2.0, 3.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).doesNotContain(3.0, 1.0);
   * </pre>
   * 
   * </p>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   */
  public S doesNotContain(double... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).doesNotContain(1.0, atIndex(1));
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).doesNotContain(2.0, atIndex(0));
   * 
   * // assertion will fail
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).doesNotContain(1.0, atIndex(0));
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).doesNotContain(2.0, atIndex(1));
   * </pre>
   * 
   * </p>
   * 
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public S doesNotContain(double value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain duplicates.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).doesNotHaveDuplicates();
   * 
   * // assertion will fail
   * assertThat(new double[] { 1.0, 1.0, 2.0, 3.0 }).doesNotHaveDuplicates();
   * </pre>
   * 
   * </p>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains duplicates.
   */
  public S doesNotHaveDuplicates() {
    arrays.assertDoesNotHaveDuplicates(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(double...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).startsWith(1.0, 2.0);
   * 
   * // assertion will fail
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).startsWith(2.0, 3.0);
   * </pre>
   * 
   * </p>
   * 
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   */
  public S startsWith(double... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(double...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).endsWith(2.0, 3.0);
   * 
   * // assertion will fail
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).endsWith(1.0, 3.0);
   * </pre>
   * 
   * </p>
   * 
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public S endsWith(double... sequence) {
    arrays.assertEndsWith(info, actual, sequence);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isSorted() {
    arrays.assertIsSorted(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isSortedAccordingTo(Comparator<? super Double> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S usingElementComparator(Comparator<? super Double> customComparator) {
    this.arrays = new DoubleArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S usingDefaultElementComparator() {
    this.arrays = DoubleArrays.instance();
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsExactly(1.0, 2.0, 3.0);
   * 
   * // assertion will fail as actual and expected orders differ.
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsExactly(2.0, 1.0, 3.0);
   * </pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   */
  public S containsExactly(double... values) {
    objects.assertEqual(info, actual, values);
    return myself;
  }

}