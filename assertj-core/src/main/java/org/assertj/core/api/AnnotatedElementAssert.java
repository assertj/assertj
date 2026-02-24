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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Assertion methods for {@code AnnotatedElement}s.
 *
 * @author William Bakker
 */
public interface AnnotatedElementAssert<SELF extends AnnotatedElementAssert<SELF, ACTUAL>, ACTUAL extends AnnotatedElement> {

  /**
   * Verifies that the actual {@code AnnotatedElement} has the given {@code Annotation}s.
   * <p>
   * Example:
   * <pre><code class='java'>  &#64;Target(ElementType.TYPE)
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
   * // this assertion succeeds:
   * assertThat(Jedi.class).hasAnnotations(Force.class, Hero.class);
   *
   * // this assertion fails:
   * assertThat(Jedi.class).hasAnnotations(Force.class, DarkSide.class);</code></pre>
   *
   * @param annotations annotations who must be attached to the {@code AnnotatedElement}
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code AnnotatedElement} doesn't contains all of these annotations.
   */
  SELF hasAnnotations(Class<? extends Annotation>... annotations);

  /**
   * Verifies that the actual {@code AnnotedElement} has the given {@code Annotation}.
   * <p>
   * Example:
   * <pre><code class='java'> &#64;Target(ElementType.TYPE)
   * &#64;Retention(RetentionPolicy.RUNTIME)
   * private static @interface Force { }
   * &#64;Force
   * class Jedi implements Jedi {}
   *
   * // this assertion succeeds:
   * assertThat(Jedi.class).containsAnnotation(Force.class);
   *
   * // this assertion fails:
   * assertThat(Jedi.class).containsAnnotation(DarkSide.class);</code></pre>
   *
   * @param annotation annotations who must be attached to the {@code AnnotatedElement}
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code AnnotatedElement} doesn't contains all of these annotations.
   */
  SELF hasAnnotation(Class<? extends Annotation> annotation);
}
