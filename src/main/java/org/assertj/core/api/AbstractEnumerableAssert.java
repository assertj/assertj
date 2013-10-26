package org.assertj.core.api;

import static org.assertj.core.internal.Arrays.assertIsArray;

import org.assertj.core.internal.Arrays;

/**
 * Base implementation for Enumerable class assertions.
 *
 * @param <S> the "self" type of this assertion class.
 * @param <A> the type of the "actual" value which is an Array of E.
 * @param <E> the type of the "actual" array element.
 * @author Joel Costigliola
 */
public abstract class AbstractEnumerableAssert<S extends AbstractEnumerableAssert<S, A, E>, A, E>
  extends AbstractAssert<S, A>
  implements EnumerableAssert<AbstractEnumerableAssert<S, A, E>, E> {

  /**
   * {@inheritDoc}
   * <p>
   * Example with byte array:
   * <pre>
   * // assertions will pass
   * assertThat(new byte[]{1, 2}).hasSameSizeAs(new byte[]{2, 3});
   * assertThat(new byte[]{1, 2}).hasSameSizeAs(new Byte[]{2, 3});
   * assertThat(new byte[]{1, 2}).hasSameSizeAs(new int[]{2, 3});
   * assertThat(new byte[]{1, 2}).hasSameSizeAs(new String[]{"1", "2"});
   *
   * // assertion will fail
   * assertThat(new byte[]{ 1, 2 }).hasSameSizeAs(new byte[]{ 1, 2, 3 });
   * </pre>
   */
  public S hasSameSizeAs(Object other) {
    assertIsArray(info, other);
    new Arrays().assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  protected AbstractEnumerableAssert(final A actual, final Class<?> selfType) {
    super(actual, selfType);
  }
}
