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
 * Copyright 2012-2020 the original author or authors.
 */

package org.assertj.core.api;

import java.lang.reflect.Constructor;
import org.assertj.core.internal.Constructors;

/**
 * Base class for all implementations of assertions for {@link Constructor}es.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a
 *               href="http://bit.ly/1IZIRcY" target="_blank">Emulating 'self types' using Java
 *               Generics to simplify fluent API implementation</a>&quot; for more details.
 * @author phx
 */
//CS304 Issue link: https://github.com/joel-costigliola/assertj-core/issues/1869
public abstract class AbstractConstructorAssert<SELF extends AbstractConstructorAssert<SELF, ACTUAL>,
    ACTUAL extends Constructor> extends AbstractAssert<SELF, ACTUAL> {

  Constructors constructores = Constructors.instance();

  public AbstractConstructorAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code Constructor} is public (has {@code public} modifier).
   *
   * <p>Example:
   * <pre><code class='java'> class MyClass {
   *   protected MyClass(){
   *   }
   * }
   *
   * // these assertions succeed:
   * assertThat(String.class.getDeclaredConstructor()).isPublic();
   *
   * // This assertion fails:
   * assertThat(MyClass.class.getDeclaredConstructor()).isPublic();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Constructor} is not public.
   */
  public SELF isPublic() {
    constructores.assertIsPublic(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Constructor} is public (has {@code public} modifier).
   *
   * <p>Example:
   * <pre><code class='java'> class MyClass {
   *   private MyClass(){
   *   }
   *
   *   public MyClass(String s){
   *
   *   }
   * }
   *
   * // these assertions succeed:
   * assertThat(String.class.getDeclaredConstructor()).isPrivate();
   *
   * // This assertion fails:
   * assertThat(MyClass.class.getDeclaredConstructor(String.class)).isPrivate();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Constructor} is not private.
   */
  public SELF isPrivate() {
    constructores.assertIsPrivate(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Constructor} is public (has {@code public} modifier).
   *
   * <p>Example:
   * <pre><code class='java'> class MyClass {
   *   protected MyClass(){
   *   }
   *
   *   public MyClass(String s){
   *
   *   }
   * }
   *
   * // these assertions succeed:
   * assertThat(String.class.getDeclaredConstructor()).isProtected();
   *
   * // This assertion fails:
   * assertThat(MyClass.class.getDeclaredConstructor(String.class)).isProtected();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Constructor} is not protected.
   */
  public SELF isProtected() {
    constructores.assertIsProtected(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Constructor} is public (has {@code public} modifier).
   *
   * <p>Example:
   * <pre><code class='java'> class MyClass {
   *   protected MyClass(){
   *   }
   *
   *   public MyClass(String s){
   *
   *   }
   * }
   *
   * // these assertions succeed:
   * Constructor[] cons=Person.getDeclaredConstructors();
   * assertThat(cons[0]).hasArguments();
   * assertThat(cons[1]).hasArguments(String.class);
   *
   * // This assertion fails:
   * assertThat(cons[1]).hasArguments(int.class);</code></pre>
   *
   * @param arguments arguments who to invoke the Constructor.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Constructor} do not contain these arguments.
   */
  public SELF hasArguments(Class<?>... arguments) {
    constructores.hasArguments(info, actual, arguments);
    return myself;
  }
}
