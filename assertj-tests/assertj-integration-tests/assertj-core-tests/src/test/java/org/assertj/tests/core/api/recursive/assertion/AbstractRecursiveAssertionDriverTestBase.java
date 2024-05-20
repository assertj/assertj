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
package org.assertj.tests.core.api.recursive.assertion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Predicate;

import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionDriver;
import org.junit.jupiter.api.BeforeEach;

abstract class AbstractRecursiveAssertionDriverTestBase {

  protected Predicate<Object> succeedingMockPredicate;
  protected Predicate<Object> failingMockPredicate;

  Object emptyTestObject() {
    return new Object();
  }

  @SuppressWarnings("unchecked")
  @BeforeEach
  public void prepareMockPredicates() {
    succeedingMockPredicate = mock(Predicate.class, "call-verification-predicate-succeeding");
    when(succeedingMockPredicate.test(any())).thenReturn(true);
    failingMockPredicate = mock(Predicate.class, "call-verification-predicate-failing");
    when(failingMockPredicate.test(any())).thenReturn(false);
  }

  protected RecursiveAssertionDriver testSubjectWithDefaultConfiguration() {
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder().build();
    return new RecursiveAssertionDriver(configuration);
  }

  protected Object objectWithNullField() {
    return new ClassWithNullField();
  }

  Top simpleCycleStructure() {
    Top top = new Top();
    Middle middle = new Middle();
    Bottom bottom = new Bottom();

    top.linkToMiddle = middle;
    middle.linkToBottom = bottom;
    bottom.loopBackToTop = top;

    return top;
  }

  class ClassWithNullField {
    @SuppressWarnings("unused")
    private Object nullField = null;
  }

  class Top {
    Middle linkToMiddle;
  }

  class Middle {
    Bottom linkToBottom;
  }

  class Bottom {
    Top loopBackToTop;
  }
}
