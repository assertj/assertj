/*
 * Created on Jul 20,2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.internal.classes;

import static org.assertj.core.error.ShouldHaveAnnotation.shouldHaveAnnotation;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.lang.annotation.*;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ClassesBaseTest;
import org.assertj.core.util.Sets;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Classes#assertContainsAnnotation(org.assertj.core.api.AssertionInfo, Class, Class[])}</code>
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

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    classes.assertContainsAnnotation(someInfo(), actual, Override.class);
  }

  @Test
  public void should_fail_if_expected_has_null_value() {
    actual = AssertionInfo.class;
    thrown.expectNullPointerException("");
    classes.assertContainsAnnotation(someInfo(), actual, Override.class, null, Deprecated.class);
  }

  @Test
  public void should_pass_if_expected_is_empty() {
    actual = AssertionInfo.class;
    classes.assertContainsAnnotation(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_have_annotation() {
    actual = AnnotatedClass.class;
    classes.assertContainsAnnotation(someInfo(), actual, MyAnnotation.class);
  }

  @Test()
  public void should_fail_if_actual_does_not_contains_an_annotation() {
    AssertionInfo info = someInfo();
    actual = AnnotatedClass.class;
    Class<? extends Annotation> expected[] = new Class[] { Override.class, Deprecated.class, MyAnnotation.class };
    try {
      classes.assertContainsAnnotation(someInfo(), actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldHaveAnnotation(actual, Sets.<Class<? extends Annotation>> newLinkedHashSet(expected),
              Sets.<Class<? extends Annotation>> newLinkedHashSet(Override.class, Deprecated.class)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
