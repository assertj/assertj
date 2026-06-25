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
package org.assertj.core.api.annotatedelement;

import org.assertj.core.api.AnnotatedElementAssert;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveAnnotations.shouldHaveAnnotations;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

/**
 * Tests for <code>{@link AnnotatedElementAssert#hasAnnotation(Class)}</code>.
 *
 * @author William Bakker
 */
class AnnotatedElementAssert_hasAnnotation_Test {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface MyAnnotation {
  }

  @MyAnnotation
  void annotatedMethod() {};

  void notAnnotatedMethod() {};

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Method actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasAnnotation(MyAnnotation.class));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_have_annotation() {
    // GIVEN
    Method actual = getMethod("notAnnotatedMethod");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasAnnotation(MyAnnotation.class));
    // THEN
    then(assertionError).hasMessage(shouldHaveAnnotations(actual, Arrays.asList(MyAnnotation.class),
                                                          Arrays.asList(MyAnnotation.class)).create());
  }

  @Test
  void should_pass_if_actual_has_annotation() {
    // GIVEN
    Method actual = getMethod("annotatedMethod");
    // WHEN/THEN
    assertThat(actual).hasAnnotation(MyAnnotation.class);
  }

  private static Method getMethod(String name) {
    try {
      return AnnotatedElementAssert_hasAnnotation_Test.class.getDeclaredMethod(name);
    } catch (NoSuchMethodException exception) {
      throw new RuntimeException("failed to get method", exception);
    }
  }

}
