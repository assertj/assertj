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
package org.assertj.core.api.classes;

import java.util.function.Consumer;
import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link ClassAssert#hasAnnotationSatisfying(Class, Consumer)}</code>.
 *
 * @author Mike Cowan
 */
class ClassAssert_hasAnnotationSatisfying_Test extends ClassAssertBaseTest {

  @Override
  protected ClassAssert invoke_api_method() {
    return assertions.hasAnnotationSatisfying(MyAnnotation.class,
                                              annotation -> assertThat(annotation).extracting(MyAnnotation::name)
                                                                                  .isEqualTo("annotation name"));
  }

  @Override
  protected void verify_internal_effects() {
    verify(classes).assertContainsAnnotations(getInfo(assertions), getActual(assertions), array(MyAnnotation.class));
  }

  @Test
  void should_fail_if_type_is_null() {
    // GIVEN
    ThrowingCallable code = () -> assertThat(AnnotatedClass.class).hasAnnotationSatisfying(null, r -> {});
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(code)
                                    .withMessage("The class to compare actual with should not be null");
  }

  @Test
  void should_fail_if_consumer_is_null() {
    // GIVEN
    ThrowingCallable code = () -> assertThat(AnnotatedClass.class).hasAnnotationSatisfying(MyAnnotation.class, null);
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(code)
                                    .withMessage("The Consumer<? extends Annotation> expressing the assertions requirements must not be null");
  }

  @Test
  void should_fail_according_to_requirements() {
    // GIVEN
    Consumer<MyAnnotation> requirements = r -> assertThat(r).extracting(MyAnnotation::name).isEqualTo("expected name");
    // WHEN
    ThrowingCallable code = () -> assertThat(AnnotatedClass.class).hasAnnotationSatisfying(MyAnnotation.class, requirements);
    AssertionError assertionError = expectAssertionError(code);
    // THEN
    then(assertionError).hasMessageContaining("expected: \"expected name\"\n but was: \"annotation name\"");
  }

}
