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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.api.class_;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveAnnotations.shouldHaveAnnotations;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ClassAssert_hasAnnotation_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasAnnotation(FunctionalInterface.class));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_null_annotation_is_given() {
    // GIVEN
    Class<?> actual = Integer.class;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).hasAnnotation(null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(shouldNotBeNull("annotation").create());
  }

  @ParameterizedTest
  @MethodSource
  void should_pass_if_actual_has_given_annotation(Class<?> actual, Class<? extends Annotation> annotation) {
    // WHEN/THEN
    assertThat(actual).hasAnnotation(annotation);
  }

  static Stream<Arguments> should_pass_if_actual_has_given_annotation() {
    return Stream.of(arguments(AnnotatedClass.class, InheritedAnnotation.class),
                     arguments(ChildClass.class, InheritedAnnotation.class),
                     arguments(ComposedAnnotated.class, ComposedAnnotation.class),
                     arguments(SingleRepeatable.class, RepeatableAnnotation.class),
                     arguments(MultipleRepeatable.class, RepeatableAnnotation.class),
                     arguments(MultipleRepeatable.class, RepeatableAnnotation.Container.class));
  }

  @ParameterizedTest
  @MethodSource
  void should_fail_if_actual_does_not_have_given_annotation(Class<?> actual, Class<? extends Annotation> annotation) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasAnnotation(annotation));
    // THEN
    then(assertionError).hasMessage(shouldHaveAnnotations(actual, Set.of(annotation), Set.of(annotation)).create());
  }

  static Stream<Arguments> should_fail_if_actual_does_not_have_given_annotation() {
    return Stream.of(arguments(AnnotatedClass.class, Deprecated.class),
                     // meta-annotation lookup is currently unsupported
                     arguments(ComposedAnnotated.class, MetaAnnotation.class),
                     // no container when only one is present
                     arguments(SingleRepeatable.class, RepeatableAnnotation.Container.class));
  }

  @Target(TYPE)
  @Retention(RUNTIME)
  @Inherited
  @interface InheritedAnnotation {
  }

  @InheritedAnnotation
  static class AnnotatedClass {
  }

  static class ChildClass extends AnnotatedClass {
  }

  @Target({ TYPE, ANNOTATION_TYPE })
  @Retention(RUNTIME)
  @interface MetaAnnotation {
  }

  @Target({ TYPE })
  @Retention(RUNTIME)
  @MetaAnnotation
  @interface ComposedAnnotation {
  }

  @ComposedAnnotation
  static class ComposedAnnotated {
  }

  @Target(TYPE)
  @Retention(RUNTIME)
  @Repeatable(RepeatableAnnotation.Container.class)
  @interface RepeatableAnnotation {

    @Target(TYPE)
    @Retention(RUNTIME)
    @interface Container {
      RepeatableAnnotation[] value();
    }
  }

  @RepeatableAnnotation
  static class SingleRepeatable {
  }

  @RepeatableAnnotation
  @RepeatableAnnotation
  static class MultipleRepeatable {
  }

}
