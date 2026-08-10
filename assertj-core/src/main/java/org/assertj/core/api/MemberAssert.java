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

import java.lang.reflect.Member;

/**
 * Assertion methods for {@code Member}s.
 *
 * @author William Bakker
 */
public interface MemberAssert<SELF extends MemberAssert<SELF, ACTUAL>, ACTUAL extends Member> {
  /**
   * Verifies that the actual {@code Member} is public (has {@code public} modifier).
   * <p>
   * Example:
   * <pre><code class='java'>  class Container {
   *   public void publicMethod() {};
   *   protected void protectedMethod() {};
   *   void packagePrivateMethod() {};
   * }
   *
   * // this assertion succeeds:
   * assertThat(Container.class.getDeclaredMethod("publicMethod")).isPublic();
   *
   * // these assertions fail:
   * assertThat(Container.class.getDeclaredMethod("protectedMethod")).isPublic();
   * assertThat(Container.class.getDeclaredMethod("packagePrivateMethod")).isPublic(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Member} is not public.
   *
   * @since 3.x.x
   */
  public SELF isPublic();

  /**
   * Verifies that the actual {@code Method} is protected (has {@code protected} modifier).
   * <p>
   * Example:
   * <pre><code class='java'>  class Container {
   *   public void publicMethod() {};
   *   protected void protectedMethod() {};
   *   void packagePrivateMethod() {};
   * }
   *
   * // this assertion succeeds:
   * assertThat(Container.class.getDeclaredMethod("protectedMethod")).isPublic();
   *
   * // these assertions fail:
   * assertThat(Container.class.getDeclaredMethod("publicMethod")).isPublic();
   * assertThat(Container.class.getDeclaredMethod("packagePrivateMethod")).isPublic(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Method} is not protected.
   *
   * @since 2.7.0 / 3.7.0
   */
  SELF isProtected();

  /**
   * Verifies that the actual {@code Method} is package-private (has no modifier).
   * <p>
   * Example:
   * <pre><code class='java'>  class Container {
   *   public void publicMethod() {};
   *   protected void protectedMethod() {};
   *   void packagePrivateMethod() {};
   * }
   *
   * // this assertion succeeds:
   * assertThat(Container.class.getDeclaredMethod("packagePrivateMethod")).isPublic();
   *
   * // these assertions fail:
   * assertThat(Container.class.getDeclaredMethod("publicMethod")).isPublic();
   * assertThat(Container.class.getDeclaredMethod("protectedMethod")).isPublic(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Method} is not package-private.
   *
   * @since 3.15.0
   */
  SELF isPackagePrivate();
}
