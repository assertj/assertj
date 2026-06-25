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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.error.MemberModifierShouldBe.shouldBeFinal;
import static org.assertj.core.error.MemberModifierShouldBe.shouldBeStatic;
import static org.assertj.core.error.MemberModifierShouldBe.shouldNotBeFinal;
import static org.assertj.core.error.MemberModifierShouldBe.shouldNotBeStatic;
import static org.assertj.core.error.ShouldBeAbstract.shouldBeAbstract;

/**
 * Base class for all implementations of assertions for {@link Method}s.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author William Bakker
 */
public abstract class AbstractMethodAssert<SELF extends AbstractMethodAssert<SELF>>
    extends AbstractExecutableAssert<SELF, Method> {

  protected AbstractMethodAssert(Method actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code Method} is abstract (has {@code abstract} modifier).
   * <p>
   * Example:
   * <pre><code class='java'>  // this assertion succeeds:
   * assertThat(Number.class.getDeclaredMethod("intValue")).isAbstract();
   *
   * // this assertion fails:
   * assertThat(Math.class.getDeclaredMethod("abs", long.class)).isAbstract(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Method} is not abstract.
   *
   * @since 3.12.0
   */
  public SELF isAbstract() {
    isNotNull();
    assertIsAbstract();
    return myself;
  }

  private void assertIsAbstract() {
    if (!Modifier.isAbstract(actual.getModifiers())) throw assertionError(shouldBeAbstract(actual));
  }

  /**
   * Verifies that the actual {@code Method} is final (has {@code final} modifier).
   * <p>
   * Example:
   * <pre><code class='java'>  // this assertion succeeds:
   * assertThat(AtomicLong.class.getDeclaredMethod("get")).isFinal();
   *
   * // this assertion fails:
   * assertThat(Math.class.getDeclaredMethod("abs", long.class)).isFinal(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Method} is not final.
   */
  public SELF isFinal() {
    isNotNull();
    assertIsFinal();
    return myself;
  }

  private void assertIsFinal() {
    if (!Modifier.isFinal(actual.getModifiers())) throw assertionError(shouldBeFinal(actual));
  }

  /**
   * Verifies that the actual {@code Method} is not final (does not have {@code final} modifier).
   * <p>
   * Example:
   * <pre><code class='java'>  // this assertion succeeds:
   * assertThat(Math.class.getDeclaredMethod("abs", long.class)).isNotFinal();
   *
   * // this assertion fails:
   * assertThat(AtomicLong.class.getDeclaredMethod("get")).isNotFinal(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Method} is final.
   */
  public SELF isNotFinal() {
    isNotNull();
    assertIsNotFinal();
    return myself;
  }

  private void assertIsNotFinal() {
    if (Modifier.isFinal(actual.getModifiers())) throw assertionError(shouldNotBeFinal(actual));
  }

  /**
   * Verifies that the actual {@code Method} is static (has {@code static} modifier).
   * <p>
   * Example:
   * <pre><code class='java'>  // this assertion succeeds:
   * assertThat(Math.class.getDeclaredMethod("abs", long.class)).isStatic();
   *
   * // this assertion fails:
   * assertThat(AtomicLong.class.getDeclaredMethod("get")).isStatic(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Method} is not static.
   * @since 3.23.0
   */
  public SELF isStatic() {
    isNotNull();
    assertIsStatic();
    return myself;
  }

  private void assertIsStatic() {
    if (!Modifier.isStatic(actual.getModifiers())) throw assertionError(shouldBeStatic(actual));
  }

  /**
   * Verifies that the actual {@code Method} is not static (does not have {@code static} modifier).
   * <p>
   * Example:
   * <pre><code class='java'>  // this assertion succeeds:
   * assertThat(AtomicLong.class.getDeclaredMethod("get")).isNotStatic();
   *
   * // this assertion fails:
   * assertThat(Math.class.getDeclaredMethod("abs", long.class)).isNotStatic(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Method} is static.
   * @since 3.23.0
   */
  public SELF isNotStatic() {
    isNotNull();
    assertIsNotStatic();
    return myself;
  }

  private void assertIsNotStatic() {
    if (Modifier.isStatic(actual.getModifiers())) throw assertionError(shouldNotBeStatic(actual));
  }
}
