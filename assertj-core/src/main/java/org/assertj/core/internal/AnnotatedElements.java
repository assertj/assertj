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
package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.ShouldHaveAnnotations.shouldHaveAnnotations;
import static org.assertj.core.util.Sets.newLinkedHashSet;

/**
 * Reusable assertions for <code>{@link AnnotatedElement}</code>s.
 *
 * @author William Bakker
 */
public class AnnotatedElements {

  private static final AnnotatedElements INSTANCE = new AnnotatedElements();

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class.
   */
  public static AnnotatedElements instance() {
    return INSTANCE;
  }

  Failures failures = Failures.instance();

  /**
   * Verifies that the actual {@code AnnotatedElement} contains the given {@code Annotation}s.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code AnnotatedElement}.
   * @param annotations annotations who must be attached to the {@code AnnotatedElement}
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code AnnotatedElement} doesn't contains all of these annotations.
   */
  public void assertContainsAnnotations(AssertionInfo info,
                                        AnnotatedElement actual,
                                        Class<? extends Annotation>[] annotations) {
    assertNotNull(info, actual);
    Set<Class<? extends Annotation>> expected = newLinkedHashSet(annotations);
    Set<Class<? extends Annotation>> missing = new LinkedHashSet<>();
    for (Class<? extends Annotation> other : expected) {
      classParameterIsNotNull(other);
      if (actual.getAnnotation(other) == null) missing.add(other);
    }

    if (!missing.isEmpty()) throw failures.failure(info, shouldHaveAnnotations(actual, expected, missing));
  }

  private static void assertNotNull(AssertionInfo info, AnnotatedElement actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  /**
   * used to check that the class to compare is not null, in that case throws a {@link NullPointerException} with an
   * explicit message.
   *
   * @param clazz the class to check
   * @throws NullPointerException with an explicit message if the given class is null
   */
  private static void classParameterIsNotNull(Class<?> clazz) {
    requireNonNull(clazz, "The class to compare actual with should not be null");
  }
}
