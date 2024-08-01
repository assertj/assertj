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

import java.util.Spliterator;

import org.assertj.core.internal.Spliterators;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertions for {@link Spliterator} type.
 *
 * @author William Bakker
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <ELEMENT> the type of elements.
 */
public class AbstractSpliteratorAssert<SELF extends AbstractSpliteratorAssert<SELF, ELEMENT>, ELEMENT> extends
    AbstractAssert<SELF, Spliterator<ELEMENT>> {

  @VisibleForTesting
  Spliterators spliterators = Spliterators.instance();

  protected AbstractSpliteratorAssert(Spliterator<ELEMENT> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Asserts the actual <code>{@link Spliterator}</code> has the given characteristics.
   * <p>
   * Example:
   * <pre><code class='java'> Spliterator&lt;Integer&gt; spliterator = Stream.of(1, 2, 3).spliterator();
   *
   * // this assertion succeeds:
   * assertThat(spliterator).hasCharacteristics(Spliterator.SIZED, Spliterator.ORDERED);
   *
   * // this assertion fails as the Spliterator does not have characteristic DISTINCT:
   * assertThat(spliterator).hasCharacteristics(Spliterator.DISTINCT); </code></pre>
   *
   * @param characteristics the expected characteristics.
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual {@code Spliterator} is {@code null}.
   * @throws AssertionError if the actual {@code Spliterator} does not have the expected characteristics.
   */
  public SELF hasCharacteristics(int... characteristics) {
    isNotNull();
    spliterators.assertHasCharacteristics(info, actual, characteristics);
    return this.myself;
  }

  /**
   * Asserts the actual <code>{@link Spliterator}</code> has only the given characteristics and nothing else.
   * <p>
   * Example:
   * <pre><code class='java'> Spliterator&lt;Integer&gt; spliterator = Stream.of(1, 2, 3).spliterator();
   *
   * // this assertion succeeds:
   * assertThat(spliterator).hasOnlyCharacteristics(Spliterator.SIZED,
   *                                                Spliterator.SUBSIZED,
   *                                                Spliterator.IMMUTABLE,
   *                                                Spliterator.ORDERED);
   *
   * // this assertion fails as the Spliterator has additional characteristics IMMUTABLE and ORDERED:
   * assertThat(spliterator).hasOnlyCharacteristics(Spliterator.SIZED, Spliterator.SUBSIZED); </code></pre>
   *
   * @param characteristics the expected characteristics.
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual {@code Spliterator} is {@code null}.
   * @throws AssertionError if the actual {@code Spliterator} does not have the expected characteristics
   *                        or the actual {@code Spliterator} has additional characteristics.
   */
  public SELF hasOnlyCharacteristics(int... characteristics) {
    isNotNull();
    spliterators.assertHasOnlyCharacteristics(info, actual, characteristics);
    return this.myself;
  }
}
