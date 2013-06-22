package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.data.Index;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.FloatArrays;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractFloatArrayAssert<S extends AbstractFloatArrayAssert<S>>
    extends AbstractAssert<S, float[]>
    implements EnumerableAssert<AbstractFloatArrayAssert<S>, Float>,
    ArraySortedAssert<AbstractFloatArrayAssert<S>, Float> {

  @VisibleForTesting
  protected FloatArrays arrays = FloatArrays.instance();

  public AbstractFloatArrayAssert(float[] actual, Class<?> selfType) {
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

  /** {@inheritDoc} */
  @Override
  public S hasSize(int expected) {
    arrays.assertHasSize(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S hasSameSizeAs(Object[] other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S hasSameSizeAs(Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values, in any order.
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   */
  public S contains(float... values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  public S containsOnly(float... values) {
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
   * assertThat(new float[] { 1.0f, 2.0f, 3.0f }).containsOnlyOnce(1.0f, 2.0f);
   * 
   * // assertions will fail
   * assertThat(new float[] { 1.0f, 2.0f, 1.0f }).containsOnlyOnce(1.0f);
   * assertThat(new float[] { 1.0f, 2.0f, 3.0f }).containsOnlyOnce(4.0f);
   * assertThat(new float[] { 1.0f, 2.0f, 3.0f, 3.0f }).containsOnlyOnce(0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
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
  public S containsOnlyOnce(float... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * 
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public S containsSequence(float... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
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
  public S contains(float value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   */
  public S doesNotContain(float... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * 
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public S doesNotContain(float value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain duplicates.
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
   * Similar to <code>{@link #containsSequence(float...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * 
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   */
  public S startsWith(float... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(float...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * 
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public S endsWith(float... sequence) {
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
  public S isSortedAccordingTo(Comparator<? super Float> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S usingElementComparator(Comparator<? super Float> customComparator) {
    this.arrays = new FloatArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S usingDefaultElementComparator() {
    this.arrays = FloatArrays.instance();
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * 
   * <pre>
   * float[] floats = { 1.0f, 2.0f, 3.0f };
   * 
   * // assertion will pass
   * assertThat(floats).containsExactly(1.0f, 2.0f, 3.0f);
   * 
   * // assertion will fail as actual and expected orders differ.
   * assertThat(floats).containsExactly(2.0f, 1.0f, 3.0f);
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
  public S containsExactly(float... values) {
    objects.assertEqual(info, actual, values);
    return myself;
  }

}