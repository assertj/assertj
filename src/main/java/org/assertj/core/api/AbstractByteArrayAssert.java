package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.data.Index;
import org.assertj.core.internal.ByteArrays;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractByteArrayAssert<S extends AbstractByteArrayAssert<S>>
  extends AbstractArrayAssert<S, byte[], Byte> {

  @VisibleForTesting
  protected ByteArrays arrays = ByteArrays.instance();

  public AbstractByteArrayAssert(byte[] actual, Class<?> selfType) {
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
   * Example:
   *
   * <pre>
   * // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).hasSize(3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3, 4 }).hasSize(3);
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
   * <p/>
   * Examples:
   * <pre>
   * // assertion will pass
   * assertThat(new byte[] { 1, 2 }).hasSameSizeAs(Arrays.asList(2, 3));
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2 }).hasSameSizeAs(Arrays.asList(1, 2, 3));
   * </pre>
   */
  @Override
  public S hasSameSizeAs(Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values, in any order.
   * <p>
   * Example:
   *
   * <pre>
   * // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).contains(1, 2);
   * assertThat(new byte[] { 1, 2, 3 }).contains(3, 1);
   * assertThat(new byte[] { 1, 2, 3 }).contains(1, 3, 2);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).contains(1, 4);
   * assertThat(new byte[] { 1, 2, 3 }).contains(4, 7);
   * </pre>
   *
   * </p>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not contain the given values.
   */
  public S contains(byte... values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * Example:
   *
   * <pre>
   * // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(1, 2, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(2, 3, 1);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(1, 2, 3, 4);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(4, 7);
   * </pre>
   *
   * </p>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not contain the given values, i.e. the actual array
   *                                  contains some
   *                                  or none of the given values, or the actual array contains more values than the
   *                                  given ones.
   */
  public S containsOnly(byte... values) {
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
   * assertThat(new byte[] { 1, 2, 3 }).containsOnlyOnce(1, 2);
   *
   * // assertions will fail
   * assertThat(new byte[] { 1, 2, 1 }).containsOnlyOnce(1);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnlyOnce(4);
   * assertThat(new byte[] { 1, 2, 3, 3 }).containsOnlyOnce(0, 1, 2, 3, 4, 5);
   * </pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual group does not contain the given values, i.e. the actual group
   *                                  contains some
   *                                  or none of the given values, or the actual group contains more than once these
   *                                  values.
   */
  public S containsOnlyOnce(byte... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   *
   * <pre>
   * // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(1, 2);
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(1, 2, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(2, 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(1, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(4, 7);
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
  public S containsSequence(byte... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   *
   * <pre>
   * // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(1, 2, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(1, 2);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsubsequence(1, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(2, 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(2, 1);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(4, 7);
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
  public S containsSubsequence(byte... subsequence) {
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
   * assertThat(new byte[] { 1, 2, 3 }).contains(1, atIndex(O));
   * assertThat(new byte[] { 1, 2, 3 }).contains(3, atIndex(2));
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).contains(1, atIndex(1));
   * assertThat(new byte[] { 1, 2, 3 }).contains(4, atIndex(2));
   * </pre>
   *
   * </p>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError            if the actual array is {@code null} or empty.
   * @throws NullPointerException      if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *                                   the actual array.
   * @throws AssertionError            if the actual array does not contain the given value at the given index.
   */
  public S contains(byte value, Index index) {
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
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(4);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(2);
   * </pre>
   *
   * </p>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array contains any of the given values.
   */
  public S doesNotContain(byte... values) {
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
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(1, atIndex(1));
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(2, atIndex(0));
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(1, atIndex(0));
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(2, atIndex(1));
   * </pre>
   *
   * </p>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError       if the actual array contains the given value at the given index.
   */
  public S doesNotContain(byte value, Index index) {
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
   * assertThat(new byte[] { 1, 2, 3 }).doesNotHaveDuplicates();
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 1, 2, 3 }).doesNotHaveDuplicates();
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
   * Similar to <code>{@link #containsSequence(byte...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   *
   * <pre>
   * // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).startsWith(1, 2);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).startsWith(2, 3);
   * </pre>
   *
   * </p>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not start with the given sequence.
   */
  public S startsWith(byte... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(byte...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   *
   * <pre>
   * // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).endsWith(2, 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).endsWith(3, 4);
   * </pre>
   *
   * </p>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not end with the given sequence.
   */
  public S endsWith(byte... sequence) {
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
  public S isSortedAccordingTo(Comparator<? super Byte> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S usingElementComparator(Comparator<? super Byte> customComparator) {
    this.arrays = new ByteArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S usingDefaultElementComparator() {
    this.arrays = ByteArrays.instance();
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   *
   * <pre>
   * // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsExactly(1, 2, 3);
   *
   * // assertion will fail as actual and expected orders differ.
   * assertThat(new byte[] { 1, 2, 3 }).containsExactly(2, 1, 3);
   * </pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the actual group is {@code null}.
   * @throws AssertionError       if the actual group does not contain the given values with same order, i.e. the actual
   *                              group
   *                              contains some or none of the given values, or the actual group contains more values
   *                              than the given ones
   *                              or values are the same but the order is not.
   */
  public S containsExactly(byte... values) {
    objects.assertEqual(info, actual, values);
    return myself;
  }

}