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
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for <code>{@link Objects#assertEqual(AssertionInfo, Object, Object)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Objects_assertEqual_Test extends ObjectsBaseTest {

  private static final Objects OBJECTS_WITH_ALWAY_EQUALS_COMPARATOR = new Objects(new ComparatorBasedComparisonStrategy(ALWAYS_EQUALS));

  @Test
  void should_pass_if_objects_are_equal() {
    objects.assertEqual(someInfo(), "Yoda", "Yoda");
  }

  @Test
  void should_fail_if_objects_are_not_equal() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> objects.assertEqual(info, "Luke", "Yoda"));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual("Luke", "Yoda", info.representation()));
  }

  @Test
  void should_pass_if_objects_are_equal_according_to_custom_comparison_strategy() {
    objectsWithCustomComparisonStrategy.assertEqual(someInfo(), "Yoda", "YODA");
  }

  @ParameterizedTest
  @CsvSource({
      "foo, bar",
      "null, foo",
      "null, bar",
      "null, null"
  })
  void should_not_check_actual_or_expected_before_applying_a_custom_comparator(String actual, String expected) {
    OBJECTS_WITH_ALWAY_EQUALS_COMPARATOR.assertEqual(someInfo(), actual, expected);
  }

  @Test
  void should_fail_if_objects_are_not_equal_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> objectsWithCustomComparisonStrategy.assertEqual(info, "Luke", "Yoda"));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual("Luke", "Yoda", customComparisonStrategy, STANDARD_REPRESENTATION));
  }

  @Test
  void should_fail_if_compared_with_null() {
    Throwable error = catchThrowable(() -> objects.assertEqual(someInfo(), new MyObject(), null));

    assertThat(error).isInstanceOf(MyObject.NullEqualsException.class);
  }

  @Test
  void should_fail_with_my_exception_if_compared_with_other_object() {
    Throwable error = catchThrowable(() -> objects.assertEqual(someInfo(), new MyObject(), "Yoda"));

    assertThat(error).isInstanceOf(MyObject.DifferentClassesException.class);
  }

  private static class MyObject {

    private final int anInt = 0;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) throw new NullEqualsException();
      if (getClass() != o.getClass()) throw new DifferentClassesException();
      MyObject myObject = (MyObject) o;
      return anInt == myObject.anInt;
    }

    private static class NullEqualsException extends RuntimeException {
      private static final long serialVersionUID = 6906581676690444515L;
    }

    private static class DifferentClassesException extends RuntimeException {
      private static final long serialVersionUID = -7330747471795712311L;
    }

  }

}
