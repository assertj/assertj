package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.data.Index;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.CharArrays;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractCharArrayAssert<S extends AbstractCharArrayAssert<S>>
    extends AbstractAssert<S, char[]>
    implements EnumerableAssert<AbstractCharArrayAssert<S>, Character>,
    ArraySortedAssert<AbstractCharArrayAssert<S>, Character> {

  @VisibleForTesting
  protected CharArrays arrays = CharArrays.instance();

  public AbstractCharArrayAssert(char[] actual, Class<?> selfType) {
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
   * assertThat(new char[] { 'a', 'b', 'c' }).hasSize(3);
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c', 'd' }).hasSize(3);
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
   * assertThat(new char[] { 'a', 'b' }).hasSameSizeAs(new int[] { 1, 2 });
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b' }).hasSameSizeAs(new int[] { 1, 2, 3 });
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
   * assertThat(new char[] { 'a', 'b' }).hasSameSizeAs(Arrays.asList(1, 2));
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b' }).hasSameSizeAs(Arrays.asList(1, 2, 3));
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
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('a', 'b');
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('c', 'a');
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('a', 'c', 'b');
   *
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('a', 'd');
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('d', 'f');
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
  public S contains(char... values) {
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
   * assertThat(new char[] { 'a', 'b', 'c' }).containsOnly('a', 'b', 'c');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsOnly('b', 'c', 'a');
   *
   * // assertion will fail
   * * assertThat(new char[] { 'a', 'b', 'c' }).containsOnly('a', 'b', 'c', 'd');
   * * assertThat(new char[] { 'a', 'b', 'c' }).containsOnly('d', 'f');
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
  public S containsOnly(char... values) {
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
   * assertThat(new char[] { 'a', 'b', 'c' }).containsOnlyOnce('a', 'b');
   * 
   * // assertions will fail
   * assertThat(new char[] { 'a', 'b', 'a' }).containsOnlyOnce('a');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsOnlyOnce('d');
   * assertThat(new char[] { 'a', 'b', 'c', 'c' }).containsOnlyOnce(0, 'a', 'b', 'c', 'd', 'e');
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
  public S containsOnlyOnce(char... values) {
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
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSequence('a', 'b');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSequence('a', 'b', 'c');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSequence('b', 'c');
   *
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSequence('c', 'a');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSequence('d', 'f');
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
  public S containsSequence(char... sequence) {
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
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('a', atIndex(O));
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('c', atIndex(2));
   *
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('a', atIndex(1));
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('d', atIndex(2));
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
  public S contains(char value, Index index) {
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
   * assertThat(new char[] { 'a', 'b', 'c' }).doesNotContain('d');
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).doesNotContain('b');
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
  public S doesNotContain(char... values) {
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
   * assertThat(new char[] { 'a', 'b', 'c' }).doesNotContain('a', atIndex(1));
   * assertThat(new char[] { 'a', 'b', 'c' }).doesNotContain('b', atIndex(0));
   *
   * // assertion will fail
   * * assertThat(new char[] { 'a', 'b', 'c' }).doesNotContain('a', atIndex(0));
   * * assertThat(new char[] { 'a', 'b', 'c' }).doesNotContain('b', atIndex(1));
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
  public S doesNotContain(char value, Index index) {
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
   * assertThat(new char[] { 'a', 'b', 'c' }).doesNotHaveDuplicates();
   * 
   * // assertion will fail
   * assertThat(new byte[] { 'a', 'a', 'b', 'c' }).doesNotHaveDuplicates();
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
   * Similar to <code>{@link #containsSequence(char...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).startsWith('a', 'b');
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).startsWith('b', 'c');
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
  public S startsWith(char... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(char...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).endsWith('b', 'c');
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).endsWith('c', 'd');
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
  public S endsWith(char... sequence) {
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
  public S isSortedAccordingTo(Comparator<? super Character> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S usingElementComparator(Comparator<? super Character> customComparator) {
    this.arrays = new CharArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S usingDefaultElementComparator() {
    this.arrays = CharArrays.instance();
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).containsExactly('a', 'b', 'c');
   * 
   * // assertion will fail as actual and expected orders differ.
   * assertThat(new char[] { 'a', 'b', 'c' }).containsExactly('b', 'a', 'c');
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
  public S containsExactly(char... values) {
    objects.assertEqual(info, actual, values);
    return myself;
  }

}