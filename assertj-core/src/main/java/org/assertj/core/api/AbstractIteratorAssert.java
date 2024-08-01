/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldBeUnmodifiable.shouldBeUnmodifiable;

import java.util.Iterator;

import org.assertj.core.annotations.Beta;
import org.assertj.core.internal.Iterators;
import org.assertj.core.util.VisibleForTesting;

/**
 * <p>Base class for all implementations of assertions for {@link Iterator}s.</p>
 * <p>Note that none of the assertions modify the actual iterator, i.e. they do not consume any elements.
 * In order to use consuming assertions, use {@link #toIterable()}.</p>
 *
 * @author Stephan Windmüller
 * @param <SELF> the "self" type of this assertion class.
 * @param <ELEMENT> the type of elements.
 *
 * @since 3.12.0
 */
public abstract class AbstractIteratorAssert<SELF extends AbstractIteratorAssert<SELF, ELEMENT>, ELEMENT>
    extends AbstractAssert<SELF, Iterator<? extends ELEMENT>> {

  @VisibleForTesting
  Iterators iterators = Iterators.instance();

  /**
   * Creates a new <code>{@link org.assertj.core.api.AbstractIteratorAssert}</code>.
   *
   * @param actual the actual value to verify
   * @param selfType the "self type"
   */
  protected AbstractIteratorAssert(Iterator<? extends ELEMENT> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * <p>Verifies that the actual {@code Iterator} has at least one more element.</p>
   *
   * Example:
   * <pre><code class='java'> Iterator&lt;TolkienCharacter&gt; elvesRingBearers = list(galadriel, elrond, gandalf).iterator();
   *
   * assertThat(elvesRingBearers).hasNext();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Iterator} is {@code null} or does not have another element.
   * @since 3.12.0
   */
  public SELF hasNext() {
    iterators.assertHasNext(info, actual);
    return myself;
  }

  /**
   * <p>Verifies that the actual {@code Iterator} has no more elements.</p>
   *
   * Example:
   * <pre><code class='java'> Iterator&lt;String&gt; result = Collections.emptyList().iterator();
   *
   * assertThat(result).isExhausted();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Iterator} is {@code null} or has another element.
   * @since 3.12.0
   */
  public SELF isExhausted() {
    iterators.assertIsExhausted(info, actual);
    return myself;
  }

  /**
   * <p>Creates a new {@link IterableAssert} from this {@link IteratorAssert} which allows for
   * using any Iterable assertions like {@link IterableAssert#contains(Object[])}.</p>
   * Example:
   * <pre><code class='java'> Iterator&lt;String&gt; bestBasketBallPlayers = getBestBasketBallPlayers();
   *
   * assertThat(bestBasketBallPlayers).toIterable().contains("Jordan", "Magic", "Lebron");</code></pre>
   *
   * @return the new {@link IterableAssert}.
   * @since 3.12.0
   */
  public IterableAssert<ELEMENT> toIterable() {
    return new IterableAssert<>(IterableAssert.toIterable(actual));
  }

  /**
   * Verifies that the actual iterator is unmodifiable, i.e., throws an {@link UnsupportedOperationException} with
   * any attempt to remove from the iterator.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(List.of().iterator()).isUnmodifiable();
   * assertThat(Set.of().iterator()).isUnmodifiable();
   *
   * // assertions will fail
   * assertThat(new ArrayList&lt;&gt;().iterator()).isUnmodifiable();
   * assertThat(new HashSet&lt;&gt;().iterator()).isUnmodifiable();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual iterator is modifiable.
   * @since 3.26.0
   */
  @Beta
  public SELF isUnmodifiable() {
    isNotNull();
    assertIsUnmodifiable();
    return myself;
  }

  private void assertIsUnmodifiable() {
    switch (actual.getClass().getName()) {
    case "java.util.Collections$EmptyIterator":
    case "java.util.Collections$EmptyListIterator":
      // immutable by contract, although not all methods throw UnsupportedOperationException
      return;
    }

    expectUnsupportedOperationException(actual::remove, "Iterator.remove()");
  }

  // Same as AbstractCollectionAssert#expectUnsupportedOperationException
  private void expectUnsupportedOperationException(Runnable runnable, String method) {
    try {
      runnable.run();
      throwAssertionError(shouldBeUnmodifiable(method));
    } catch (UnsupportedOperationException e) {
      // happy path
    } catch (RuntimeException e) {
      throwAssertionError(shouldBeUnmodifiable(method, e));
    }
  }
}
