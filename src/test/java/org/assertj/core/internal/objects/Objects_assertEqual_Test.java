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
package org.assertj.core.internal.objects;

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

/**
 * Tests for <code>{@link Objects#assertEqual(AssertionInfo, Object, Object)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Objects_assertEqual_Test extends ObjectsBaseTest {

  @Test
  public void should_pass_if_objects_are_equal() {
    objects.assertEqual(someInfo(), "Yoda", "Yoda");
  }

  @Test
  public void should_fail_if_objects_are_not_equal() {
    AssertionInfo info = someInfo();
    try {
      objects.assertEqual(info, "Luke", "Yoda");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual("Luke", "Yoda", info.representation()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_objects_are_equal_according_to_custom_comparison_strategy() {
    objectsWithCustomComparisonStrategy.assertEqual(someInfo(), "Yoda", "YODA");
  }

  @Test
  public void should_fail_if_objects_are_not_equal_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      objectsWithCustomComparisonStrategy.assertEqual(info, "Luke", "Yoda");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual("Luke", "Yoda", customComparisonStrategy,
          new StandardRepresentation()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }


  @Test
  public void should_fail_with_my_exception_if_compared_with_null() {
    try {
      objects.assertEqual(someInfo(), new MyObject(), null);
    } catch (MyObject.NullEqualsException e) {
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_with_my_exception_if_compared_with_other_object() {
    try {
      objects.assertEqual(someInfo(), new MyObject(), "Yoda");
    } catch (MyObject.DifferentClassesException e) {
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  private static class MyObject {
    private final int anInt = 0;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) throw new NullEqualsException();
      if (getClass() != o.getClass()) throw new DifferentClassesException();
      MyObject myObject = (MyObject) o;
      if (anInt != myObject.anInt) return false;
      return true;
    }

    private class NullEqualsException extends RuntimeException {
      private static final long serialVersionUID = 6906581676690444515L;
    }

    private class DifferentClassesException extends RuntimeException {
      private static final long serialVersionUID = -7330747471795712311L;
    }
  }
}
