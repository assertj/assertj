package org.assertj.core.api;

/**
 * Base class for all array assertions.
 *
 * @param <S> the "self" type of this assertion class.
 * @param <A> the type of the "actual" value which is an Array of E.
 * @param <E> the type of the "actual" array element.
 * @author Joel Costigliola
 */
public abstract class AbstractArrayAssert<S extends AbstractArrayAssert<S, A, E>, A, E>
  extends AbstractEnumerableAssert<S, A, E>
  implements ArraySortedAssert<AbstractArrayAssert<S, A, E>, E> {

  protected AbstractArrayAssert(final A actual, final Class<?> selfType) {
    super(actual, selfType);
  }
}
