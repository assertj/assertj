/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
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
   * <pre><code class='java'> // assertions will pass
   * assertThat(new byte[]{1, 2}).hasSameSizeAs(new byte[]{2, 3});
   * assertThat(new byte[]{1, 2}).hasSameSizeAs(new Byte[]{2, 3});
   * assertThat(new byte[]{1, 2}).hasSameSizeAs(new int[]{2, 3});
   * assertThat(new byte[]{1, 2}).hasSameSizeAs(new String[]{"1", "2"});
   *
   * // assertion will fail
   * assertThat(new byte[]{ 1, 2 }).hasSameSizeAs(new byte[]{ 1, 2, 3 });</code></pre>
   */
  public S hasSameSizeAs(Object other) {
    assertIsArray(info, other);
    new Arrays().assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  protected AbstractEnumerableAssert(final A actual, final Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Enable hexadecimal object representation of Iterable elements instead of standard java representation in error messages.
   * <p/>
   * It can be useful to better understand what the error was with a more meaningful error message.
   * <p/>
   * Example
   * <pre><code class='java'> assertThat(new byte[]{0x10,0x20}).inHexadecimal().contains(new byte[]{0x30});</code></pre>
   *
   * With standard error message:
   * <pre><code class='java'> Expecting:
   *  <[16, 32]>
   * to contain:
   *  <[48]>
   * but could not find:
   *  <[48]></code></pre>
   *
   * With Hexadecimal error message:
   * <pre><code class='java'> Expecting:
   *  <[0x10, 0x20]>
   * to contain:
   *  <[0x30]>
   * but could not find:
   *  <[0x30]></code></pre>
   *
   * @return {@code this} assertion object.
   */
  @Override
  public S inHexadecimal() {
    return super.inHexadecimal();
  }

  @Override
  public S inBinary() {
    return super.inBinary();
  }

}
