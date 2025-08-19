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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.assertj.core.error.MemberModifierShouldBe.shouldBeFinal;
import static org.assertj.core.error.MemberModifierShouldBe.shouldBePackagePrivate;
import static org.assertj.core.error.MemberModifierShouldBe.shouldBeProtected;
import static org.assertj.core.error.MemberModifierShouldBe.shouldBePublic;
import static org.assertj.core.error.MemberModifierShouldBe.shouldBeStatic;
import static org.assertj.core.error.MemberModifierShouldBe.shouldNotBeFinal;
import static org.assertj.core.error.MemberModifierShouldBe.shouldNotBeStatic;

/**
 * Base class for all implementations of assertions for {@link Field}s.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author William Bakker
 */
public abstract class AbstractFieldAssert<SELF extends AbstractFieldAssert<SELF>>
    extends AbstractAssert<SELF, Field> implements MemberAssert<SELF, Field> {

  protected AbstractFieldAssert(Field actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  @Override
  public SELF isPublic() {
    isNotNull();
    assertIsPublic();
    return myself;
  }

  private void assertIsPublic() {
    if (!Modifier.isPublic(actual.getModifiers())) throw assertionError(shouldBePublic(actual));
  }

  /** {@inheritDoc} */
  @Override
  public SELF isProtected() {
    isNotNull();
    assertIsProtected();
    return myself;
  }

  private void assertIsProtected() {
    if (!Modifier.isProtected(actual.getModifiers())) throw assertionError(shouldBeProtected(actual));
  }

  /** {@inheritDoc} */
  @Override
  public SELF isPackagePrivate() {
    isNotNull();
    assertIsPackagePrivate();
    return myself;
  }

  private void assertIsPackagePrivate() {
    final int modifiers = actual.getModifiers();
    if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers)) {
      throw assertionError(shouldBePackagePrivate(actual));
    }
  }

  /**
   * Verifies that the actual {@code Field} is final (has {@code final} modifier).
   * <p>
   * Example:
   * <pre><code class='java'>  // this assertion succeeds:
   * assertThat(Math.class.getDeclaredField("PI")).isFinal();
   *
   * // this assertion fails:
   * assertThat(AtomicLong.class.getDeclaredField("value")).isFinal(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Field} is not final.
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
   * Verifies that the actual {@code Field} is not final (does not have {@code final} modifier).
   * <p>
   * Example:
   * <pre><code class='java'>  // this assertion succeeds:
   * assertThat(AtomicLong.class.getDeclaredField("value")).isNotFinal();
   *
   * // this assertion fails:
   * assertThat(Math.class.getDeclaredField("PI")).isNotFinal(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Field} is final.
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
   * Verifies that the actual {@code Field} is static (has {@code static} modifier).
   * <p>
   * Example:
   * <pre><code class='java'>  // this assertion succeeds:
   * assertThat(Math.class.getDeclaredField("PI")).isStatic();
   *
   * // this assertion fails:
   * assertThat(AtomicLong.class.getDeclaredField("value")).isStatic(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Field} is not static.
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
   * Verifies that the actual {@code Field} is not static (does not have {@code static} modifier).
   * <p>
   * Example:
   * <pre><code class='java'>  // this assertion succeeds:
   * assertThat(AtomicLong.class.getDeclaredField("value")).isNotStatic();
   *
   * // this assertion fails:
   * assertThat(Math.class.getDeclaredField("PI")).isNotStatic(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Field} is static.
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
