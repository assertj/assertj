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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ClassAssert;
import org.assertj.tests.core.testkit.NavigationMethodBaseTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author Mike Cowan
 */
class ClassAssert_annotation_Test implements NavigationMethodBaseTest<ClassAssert> {

  @Test
  void should_fail_if_given_annotation_is_null() {
    // WHEN
    Exception exception = catchException(() -> assertThat(AnnotatedClass.class).annotation(null));
    // THEN
    then(exception).isInstanceOf(NullPointerException.class)
                   .hasMessage(shouldNotBeNull("annotation").create());
  }

  @Nested
  class With_Non_Repeatable_Annotation {

    @Test
    void should_allow_object_assertions() {
      // WHEN/THEN
      assertThat(AnnotatedClass.class).annotation(AnnotationA.class)
                                      .extracting(AnnotationA::value)
                                      .isEqualTo("value1");
    }
  }

  @Nested
  class With_Repeatable_Annotation {

    // TODO

  }

  @Override
  public ClassAssert getAssertion() {
    return assertThat(AnnotatedClass.class);
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(ClassAssert assertion) {
    return assertion.annotation(AnnotationA.class);
  }

  @Target(TYPE)
  @Retention(RUNTIME)
  @interface AnnotationA {
    String value();
  }

  @Target(TYPE)
  @Retention(RUNTIME)
  @Repeatable(AnnotationR.AnnotationRs.class)
  @interface AnnotationR {

    String value();

    @Target(TYPE)
    @Retention(RUNTIME)
    @interface AnnotationRs {

      AnnotationR[] value();
    }
  }

  @AnnotationA("value1")
  @AnnotationR("value2")
  @AnnotationR("value3")
  private static class AnnotatedClass {
  }
}
