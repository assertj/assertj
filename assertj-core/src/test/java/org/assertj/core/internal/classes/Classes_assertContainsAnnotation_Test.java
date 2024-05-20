/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.classes;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveAnnotations.shouldHaveAnnotations;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Classes#assertContainsAnnotations(org.assertj.core.api.AssertionInfo, Class, Class[])}</code>.
 *
 * @author William Delanoue
 */
class Classes_assertContainsAnnotation_Test extends ClassesBaseTest {

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  private @interface MyAnnotation {

  }

  @MyAnnotation
  private static class AnnotatedClass {
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertContainsAnnotations(someInfo(), actual,
                                                                                                 array(Override.class)));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_has_null_value() {
    // GIVEN
    actual = AssertionInfo.class;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> classes.assertContainsAnnotations(someInfo(), actual,
                                                                                        array(Override.class, null,
                                                                                              Deprecated.class)))
                                    .withMessage("The class to compare actual with should not be null");
  }

  @Test
  void should_pass_if_expected_is_empty() {
    actual = AssertionInfo.class;
    classes.assertContainsAnnotations(someInfo(), actual, array());
  }

  @Test
  void should_pass_if_actual_have_annotation() {
    actual = AnnotatedClass.class;
    classes.assertContainsAnnotations(someInfo(), actual, array(MyAnnotation.class));
  }

  @SuppressWarnings("unchecked")
  @Test
  void should_fail_if_actual_does_not_contains_an_annotation() {
    // GIVEN
    Class<Annotation>[] expected = new Class[] { Override.class, Deprecated.class, MyAnnotation.class };
    actual = AnnotatedClass.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertContainsAnnotations(someInfo(), actual, expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveAnnotations(actual, newLinkedHashSet(expected),
                                                          newLinkedHashSet(Override.class, Deprecated.class)).create());
  }
}
