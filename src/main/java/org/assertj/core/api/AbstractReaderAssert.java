package org.assertj.core.api;

import java.io.Reader;

import org.assertj.core.internal.Readers;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link java.io.Reader}s.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <A> the type of the "actual" value.
 *
 * @author Matthieu Baechler
 * @author Mikhail Mazursky
 * @author Bartosz Bierkowski
 */
public abstract class AbstractReaderAssert<S extends AbstractReaderAssert<S, A>, A extends Reader> extends
    AbstractAssert<S, A> {

  @VisibleForTesting
  Readers readers = Readers.instance();

  protected AbstractReaderAssert(A actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the content of the actual {@code Reader} is equal to the content of the given one.
   *
   * @param expected the given {@code Reader} to compare the actual {@code Reader} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code Reader} is {@code null}.
   * @throws AssertionError if the actual {@code Reader} is {@code null}.
   * @throws AssertionError if the content of the actual {@code Reader} is not equal to the content of the given one.
   * @throws org.assertj.core.internal.ReadersException if an I/O error occurs.
   */
  public S hasContentEqualTo(Reader expected) {
    readers.assertEqualContent(info, actual, expected);
    return myself;
  }
}
