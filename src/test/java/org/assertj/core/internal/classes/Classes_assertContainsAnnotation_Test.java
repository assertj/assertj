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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.classes;

import static org.assertj.core.error.ShouldHaveAnnotations.shouldHaveAnnotations;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ClassesBaseTest;
import org.assertj.core.util.Sets;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Classes#assertContainsAnnotations(org.assertj.core.api.AssertionInfo, Class, Class[])}</code>
 * .
 * 
 * @author William Delanoue
 */
public class Classes_assertContainsAnnotation_Test extends ClassesBaseTest {

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  private static @interface MyAnnotation {

  }

  @MyAnnotation
  private static class AnnotatedClass {
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    classes.assertContainsAnnotations(someInfo(), actual, Override.class);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_fail_if_expected_has_null_value() {
    actual = AssertionInfo.class;
    thrown.expectNullPointerException("The class to compare actual with should not be null");
    classes.assertContainsAnnotations(someInfo(), actual, Override.class, null, Deprecated.class);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_pass_if_expected_is_empty() {
    actual = AssertionInfo.class;
    classes.assertContainsAnnotations(someInfo(), actual);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_pass_if_actual_have_annotation() {
    actual = AnnotatedClass.class;
    classes.assertContainsAnnotations(someInfo(), actual, MyAnnotation.class);
  }

  @SuppressWarnings("unchecked")
  @Test()
  public void should_fail_if_actual_does_not_contains_an_annotation() {
    actual = AnnotatedClass.class;
    Class<Annotation> expected[] = new Class[] { Override.class, Deprecated.class, MyAnnotation.class };
    thrown.expectAssertionError(shouldHaveAnnotations(actual,
                                                      Sets.<Class<? extends Annotation>> newLinkedHashSet(expected),
                                                      Sets.<Class<? extends Annotation>> newLinkedHashSet(Override.class,
                                                                                                          Deprecated.class)));
    classes.assertContainsAnnotations(someInfo(), actual, expected);
  }
}
