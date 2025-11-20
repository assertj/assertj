/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import static org.assertj.core.util.Arrays.array;
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

/**
 * @author William Delanoue
 * @author Joel Costigliola
 */
class ClassAssert_hasAnnotations_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    Class<? extends Annotation>[] annotations = array(FunctionalInterface.class);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasAnnotations(annotations));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_annotations_is_null() {
    // GIVEN
    Class<?> actual = Integer.class;
    Class<? extends Annotation>[] annotations = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).hasAnnotations(annotations));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(shouldNotBeNull("annotations").create());
  }

  @Test
  void should_fail_if_annotations_contains_null() {
    // GIVEN
    Class<?> actual = Integer.class;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).hasAnnotations(FunctionalInterface.class, null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class);
  }

  @ParameterizedTest
  @MethodSource
  void should_pass_if_actual_has_annotations(Class<?> actual, Class<? extends Annotation>[] annotations) {
    // WHEN/THEN
    assertThat(actual).hasAnnotations(annotations);
  }

  static Stream<Arguments> should_pass_if_actual_has_annotations() {
    return Stream.of(arguments(Integer.class, new Class[0]),
                     arguments(Annotated.class, array(InheritedAnnotation.class)),
                     arguments(Subclass.class, array(InheritedAnnotation.class)),
                     arguments(ComposedAnnotated.class, array(ComposedAnnotation.class)),
                     arguments(SingleRepeatableAnnotated.class, array(RepeatableAnnotation.class)),
                     arguments(MultiRepeatableAnnotated.class, array(RepeatableAnnotation.class)),
                     arguments(MultiRepeatableAnnotated.class, array(RepeatableAnnotationContainer.class)));
  }

  @ParameterizedTest
  @MethodSource
  void should_fail_if_actual_does_not_have_all_annotations(Class<?> actual, Class<? extends Annotation>[] annotations,
                                                           Set<Class<? extends Annotation>> missing) {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasAnnotations(annotations));
    // THEN
    then(assertionError).hasMessage(shouldHaveAnnotations(actual, Set.of(annotations), missing).create());
  }

  static Stream<Arguments> should_fail_if_actual_does_not_have_all_annotations() {
    return Stream.of(arguments(Annotated.class,
                               array(Deprecated.class),
                               Set.of(Deprecated.class)),
                     arguments(Annotated.class,
                               array(InheritedAnnotation.class, Deprecated.class),
                               Set.of(Deprecated.class)),
                     // meta-annotation lookup is currently unsupported
                     arguments(ComposedAnnotated.class,
                               array(MetaAnnotation.class),
                               Set.of(MetaAnnotation.class)),
                     // no container created when only one annotation is present
                     arguments(SingleRepeatableAnnotated.class,
                               array(RepeatableAnnotationContainer.class),
                               Set.of(RepeatableAnnotationContainer.class)));
  }

  /* ------------------------------------------------------------------------------------------ */

  @Target(TYPE)
  @Retention(RUNTIME)
  @Inherited
  private @interface InheritedAnnotation {
  }

  @InheritedAnnotation
  private static class Annotated {
  }

  private static class Subclass extends Annotated {
  }

  @Target({ TYPE, ANNOTATION_TYPE })
  @Retention(RUNTIME)
  private @interface MetaAnnotation {
  }

  @Target(TYPE)
  @Retention(RUNTIME)
  @MetaAnnotation
  private @interface ComposedAnnotation {
  }

  @ComposedAnnotation
  private static class ComposedAnnotated {
  }

  @Target(TYPE)
  @Retention(RUNTIME)
  @Repeatable(RepeatableAnnotationContainer.class)
  private @interface RepeatableAnnotation {
  }

  @Target(TYPE)
  @Retention(RUNTIME)
  private @interface RepeatableAnnotationContainer {
    RepeatableAnnotation[] value();
  }

  @RepeatableAnnotation
  private static class SingleRepeatableAnnotated {
  }

  @RepeatableAnnotation
  @RepeatableAnnotation
  private static class MultiRepeatableAnnotated {
  }

}
