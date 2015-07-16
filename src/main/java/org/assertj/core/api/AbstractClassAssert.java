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

import java.lang.annotation.Annotation;

import org.assertj.core.internal.Classes;

/**
 * Base class for all implementations of assertions for {@link Class}es.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * 
 * @author William Delanoue
 * @author Mikhail Mazursky
 */
public abstract class AbstractClassAssert<S extends AbstractClassAssert<S>> extends AbstractAssert<S, Class<?>> {

  Classes classes = Classes.instance();

  protected AbstractClassAssert(Class<?> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code Class} is assignable from others {@code Class}
   * 
   * <pre><code class='java'> class Jedi {}
   * class HumanJedi extends Jedi {}
   * 
   * // Should pass if :
   * assertThat(Jedi.class).isAssignableFrom(HumanJedi.class);
   * 
   * // Should fail if :
   * assertThat(HumanJedi.class).isAssignableFrom(Jedi.class);</code></pre>
   * 
   * @see Class#isAssignableFrom(Class)
   * @param others {@code Class} who can be assignable from.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Class} is {@code null}.
   * @throws IllegalArgumentException if no {@code others} classes have been specified.
   * @throws AssertionError if the actual {@code Class} is not assignable from all of the {@code others} classes.
   */
  public S isAssignableFrom(Class<?>... others) {
    classes.assertIsAssignableFrom(info, actual, others);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is not an interface.
   * 
   * <pre><code class='java'> interface Jedi {}
   * class HumanJedi implements Jedi {}
   * 
   * // Should pass if :
   * assertThat(HumanJedi.class).isNotInterface();
   * 
   * // Should fail if :
   * assertThat(Jedi.class).isNotInterface();</code></pre>
   * 
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not an interface.
   */
  public S isNotInterface() {
    classes.assertIsNotInterface(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is an interface.
   * 
   * <pre><code class='java'> interface Jedi {}
   * class HumanJedi implements Jedi {}
   * 
   * // Should pass if :
   * assertThat(Jedi.class).isInterface();
   * 
   * // Should fail if :
   * assertThat(HumanJedi.class).isInterface();</code></pre>
   * 
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not an interface.
   */
  public S isInterface() {
    classes.assertIsInterface(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is an annotation.
   * 
   * <pre><code class='java'> public @interface Jedi {}
   * 
   * // Should pass if :
   * assertThat(Jedi.class).isAnnotation();
   * assertThat(Override.class).isAnnotation();
   * assertThat(Deprecated.class).isAnnotation();
   * 
   * // Should fail if :
   * assertThat(String.class).isAnnotation();</code></pre>
   * 
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not an annotation.
   */
  public S isAnnotation() {
    classes.assertIsAnnotation(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is not an annotation.
   * 
   * <pre><code class='java'> public @interface Jedi {}
   * 
   * // Should pass if :
   * assertThat(String.class).isNotAnnotation();
   * 
   * // Should fail if :
   * assertThat(Jedi.class).isNotAnnotation();
   * assertThat(Override.class).isNotAnnotation();
   * assertThat(Deprecated.class).isNotAnnotation();</code></pre>
   * 
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is an annotation.
   */
  public S isNotAnnotation() {
    classes.assertIsNotAnnotation(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is final (has {@code final} modifier).
   *
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(String.class).isFinal();
   * assertThat(Math.class).isFinal();
   *
   * // These assertions fail:
   * assertThat(Object.class).isFinal();
   * assertThat(Throwable.class).isFinal();</code></pre>
   *
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not final.
   */
  public S isFinal() {
    classes.assertIsFinal(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is not final (does not have {@code final} modifier).
   *
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(Object.class).isNotFinal();
   * assertThat(Throwable.class).isNotFinal();
   *
   * // These assertions fail:
   * assertThat(String.class).isNotFinal();
   * assertThat(Math.class).isNotFinal();</code></pre>
   *
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is final.
   */
  public S isNotFinal() {
    classes.assertIsNotFinal(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given {@code Annotation}s.
   * 
   * <pre><code class='java'> &#64;Target(ElementType.TYPE)
   * &#64;Retention(RetentionPolicy.RUNTIME)
   * private static @interface Force { }
   * 
   * &#64;Target(ElementType.TYPE)
   * &#64;Retention(RetentionPolicy.RUNTIME)
   * private static @interface Hero { }
   * 
   * &#64;Target(ElementType.TYPE)
   * &#64;Retention(RetentionPolicy.RUNTIME)
   * private static @interface DarkSide { }
   * 
   * &#64;Hero &#64;Force
   * class Jedi implements Jedi {}
   * 
   * // Should pass:
   * assertThat(Jedi.class).containsAnnotations(Force.class, Hero.class);
   * 
   * // Should fail:
   * assertThat(Jedi.class).containsAnnotations(Force.class, DarkSide.class);</code></pre>
   * 
   * @param annotations annotations who must be attached to the class
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of these annotations.
   */
  public S hasAnnotations(@SuppressWarnings("unchecked") Class<? extends Annotation>... annotations) {
    classes.assertContainsAnnotations(info, actual, annotations);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given {@code Annotation}.
   * 
   * <pre><code class='java'> &#64;Target(ElementType.TYPE)
   * &#64;Retention(RetentionPolicy.RUNTIME)
   * private static @interface Force { }
   * &#64;Force
   * class Jedi implements Jedi {}
   * 
   * // Should pass if :
   * assertThat(Jedi.class).containsAnnotation(Force.class);
   * 
   * // Should fail if :
   * assertThat(Jedi.class).containsAnnotation(DarkSide.class);</code></pre>
   * 
   * @param annotation annotations who must be attached to the class
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of these annotations.
   */
  @SuppressWarnings("unchecked")
  public S hasAnnotation(Class<? extends Annotation> annotation) {
    classes.assertContainsAnnotations(info, actual, annotation);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the {@code fields}.
   * 
   * <pre><code class='java'> class MyClass {
   *     public String fieldOne;
   *     private String fieldTwo;
   * }
   * 
   * // This one should pass :
   * assertThat(MyClass.class).hasFields("fieldOne");
   * 
   * // This one should fail :
   * assertThat(MyClass.class).hasFields("fieldTwo");
   * assertThat(MyClass.class).hasDeclaredFields("fieldThree");</code></pre>
   * 
   * @see Class#getField(String)
   * @param fields the fields who must be in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the field.
   */
  public S hasFields(String... fields) {
    classes.assertHasFields(info, actual, fields);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the declared {@code fields}.
   * 
   * <pre><code class='java'> class MyClass {
   *     public String fieldOne;
   *     private String fieldTwo;
   * }
   * 
   * // This one should pass :
   * assertThat(MyClass.class).hasDeclaredFields("fieldOne", "fieldTwo");
   * 
   * // This one should fail :
   * assertThat(MyClass.class).hasDeclaredFields("fieldThree");</code></pre>
   * 
   * @see Class#getDeclaredField(String)
   * @param fields the fields who must be declared in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the field.
   */
  public S hasDeclaredFields(String... fields) {
    classes.assertHasDeclaredFields(info, actual, fields);
    return myself;
  }
}
