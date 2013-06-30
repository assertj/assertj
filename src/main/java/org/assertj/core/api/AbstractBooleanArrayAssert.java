package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.data.Index;
import org.assertj.core.internal.BooleanArrays;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractBooleanArrayAssert<S extends AbstractBooleanArrayAssert<S>>
    extends AbstractAssert<S, boolean[]>
    implements EnumerableAssert<AbstractBooleanArrayAssert<S>, Boolean>,
    ArraySortedAssert<AbstractBooleanArrayAssert<S>, Boolean> {

  @VisibleForTesting
  protected BooleanArrays arrays = BooleanArrays.instance();

  public AbstractBooleanArrayAssert(boolean[] actual, Class<?> selfType) {
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
   * assertThat(new boolean[] { true, false }).hasSize(2);
   * 
   * // assertion will fail
   * assertThat(new boolean[] { true }).hasSize(2);
   * </pre>
   * 
   * </p>
   * 
   */
  @Override
  public S hasSize(int expected) {
    arrays.assertHasSize(info, actual, expected);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new boolean[] { true, false }).hasSameSizeAs(new int[] { 1, 2 });
   * 
   * // assertion will fail
   * assertThat(new boolean[] { true, false }).hasSameSizeAs(new int[] { 1, 2, 3 });
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
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new boolean[] { true, false }).hasSameSizeAs(Arrays.asList(1, 2));
   * 
   * // assertion will fail
   * assertThat(new boolean[] { true, false }).hasSameSizeAs(Arrays.asList(1, 2, 3));
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
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new boolean[] { true, false }).contains(true, false);
   * assertThat(new boolean[] { false, true }).contains(true, false);
   * assertThat(new boolean[] { true, false }).contains(true);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, true }).contains(false);
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
  public S contains(boolean... values) {
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
   * assertThat(new boolean[] { true, false }).containsOnly(true, false);
   * assertThat(new boolean[] { true, false, false, true }).containsOnly(true, false);
   * 
   * // assertion will fail
   * assertThat(new boolean[] { true, false }).containsOnly(false);
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
  public S containsOnly(boolean... values) {
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
   * assertThat(new boolean[] { true, false }).containsOnlyOnce(true, false);
   * 
   * // assertions will fail
   * assertThat(new boolean[] { true, false, true }).containsOnlyOnce(true);
   * assertThat(new boolean[] { true }).containsOnlyOnce(false);
   * assertThat(new boolean[] { true }).containsOnlyOnce(true, false);
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
  public S containsOnlyOnce(boolean... values) {
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
   * assertThat(new boolean[] { true, false }).containsSequence(true, false);
   * assertThat(new boolean[] { true, false, false, true }).containsSequence(false, true);
   * 
   * // assertion will fail
   * assertThat(new boolean[] { true, true, false }).containsSequence(false, true);
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
  public S containsSequence(boolean... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new boolean[] { true, false }).contains(true, atIndex(O));
   * assertThat(new boolean[] { true, false }).contains(false, atIndex(1));
   * 
   * // assertion will fail
   * assertThat(new boolean[] { true, false }).contains(false, atIndex(0));
   * assertThat(new boolean[] { true, false }).contains(true, atIndex(1));
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
  public S contains(boolean value, Index index) {
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
   * assertThat(new boolean[] { true, true }).doesNotContain(false);
   * 
   * // assertion will fail
   * assertThat(new boolean[] { true, true, false }).doesNotContain(false);
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
  public S doesNotContain(boolean... values) {
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
   * assertThat(new boolean[] { true, false }).doesNotContain(true, atIndex(1));
   * assertThat(new boolean[] { true, false }).doesNotContain(false, atIndex(0));
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, false }).doesNotContain(false, atIndex(1));
   * assertThat(new boolean[] { true, false }).doesNotContain(true, atIndex(0));
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
  public S doesNotContain(boolean value, Index index) {
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
   * assertThat(new boolean[] { true, false }).doesNotHaveDuplicates();
   * 
   * // assertion will fail
   * assertThat(new boolean[] { true, true, false }).doesNotHaveDuplicates();
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
   * Similar to <code>{@link #containsSequence(boolean...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new boolean[] { true, false, false, true }).startsWith(true, false);
   * 
   * // assertion will fail
   * assertThat(new boolean[] { true, false, false, true }).startsWith(false, false, true);
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
  public S startsWith(boolean... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(boolean...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   *
   * <pre>
   * // assertion will pass
   * assertThat(new boolean[] { true, false, false, true }).endsWith(false, false, true);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, false, false, true }).endsWith(true, false);
   * </pre>
   *
   * </p>
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public S endsWith(boolean... sequence) {
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
  public S isSortedAccordingTo(Comparator<? super Boolean> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /**
   * Do not use this method.
   * 
   * @deprecated Custom element Comparator is not supported for Boolean array comparison.
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override
  @Deprecated
  public final S usingElementComparator(Comparator<? super Boolean> customComparator) {
    throw new UnsupportedOperationException("custom element Comparator is not supported for Boolean array comparison");
  }

  /**
   * Do not use this method.
   * 
   * @deprecated Custom element Comparator is not supported for Boolean array comparison.
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override
  @Deprecated
  public final S usingDefaultElementComparator() {
    throw new UnsupportedOperationException("custom element Comparator is not supported for Boolean array comparison");
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new boolean[] { true, false, true }).containsExactly(true, false, true);
   * 
   * // assertion will fail as actual and expected orders differ.
   * assertThat(new boolean[] { true, false, true }).containsExactly(false, true, true);
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
  public S containsExactly(boolean... values) {
    objects.assertEqual(info, actual, values);
    return myself;
  }

}