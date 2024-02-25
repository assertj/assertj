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

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest;
import org.assertj.core.api.NavigationMethodBaseTest;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link ClassAssert#annotation(Class)}</code>.
 *
 * @author Mike Cowan
 */
class ClassAssert_annotation_Test extends ClassAssertBaseTest
    implements NavigationMethodBaseTest<ClassAssert> {

  @Override
  protected ClassAssert invoke_api_method() {
    assertions.annotation(MyAnnotation.class);
    return assertions;
  }

  @Override
  protected void verify_internal_effects() {
    verify(classes).assertContainsAnnotations(getInfo(assertions), getActual(assertions), array(MyAnnotation.class));
  }

  @Override
  public void should_return_this() {
    // Test disabled since annotation does not return this.
  }

  @Override
  public ClassAssert getAssertion() {
    return assertions;
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(ClassAssert assertion) {
    return assertion.annotation(MyAnnotation.class);
  }

  @Test
  void should_fail_if_type_is_null() {
    // GIVEN
    ThrowingCallable code = () -> assertThat(AnnotatedClass.class).annotation(null);
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(code).withMessage("The class to compare actual with should not be null");
  }

  @Test
  void should_return_narrowed_assert_type() {
    // WHEN
    AbstractAssert<?, ?> result = assertions.annotation(MyAnnotation.class);
    // THEN
    then(result).isInstanceOf(AbstractObjectAssert.class);
  }

  @Test
  void should_honor_test_description() {
    // GIVEN
    AbstractObjectAssert<?, MyAnnotation> assertion = assertThat(AnnotatedClass.class).annotation(MyAnnotation.class);
    // WHEN
    ThrowingCallable code = () -> assertion.as("test description")
                                           .extracting(MyAnnotation::name)
                                           .isEqualTo("expected name");
    AssertionError assertionError = expectAssertionError(code);
    // THEN
    then(assertionError).hasMessageContaining("test description");
  }

  @Test
  void should_fail_according_to_requirements() {
    // GIVEN
    AbstractObjectAssert<?, MyAnnotation> assertion = assertThat(AnnotatedClass.class).annotation(MyAnnotation.class);
    // WHEN
    ThrowingCallable code = () -> assertion.extracting(MyAnnotation::name).isEqualTo("expected name");
    AssertionError assertionError = expectAssertionError(code);
    // THEN
    then(assertionError).hasMessageContainingAll("expected: \"expected name\"", "but was: \"annotation name\"");
  }

}
