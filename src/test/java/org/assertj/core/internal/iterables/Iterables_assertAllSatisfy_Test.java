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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfy;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.error.UnsatisfiedRequirement;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

class Iterables_assertAllSatisfy_Test extends IterablesBaseTest {

  private List<String> actual = newArrayList("Luke", "Leia", "Yoda");

  @Test
  void should_satisfy_single_requirement() {
    iterables.assertAllSatisfy(someInfo(), actual, s -> assertThat(s.length()).isEqualTo(4));
  }

  @Test
  void should_satisfy_multiple_requirements() {
    iterables.assertAllSatisfy(someInfo(), actual, s -> {
      assertThat(s.length()).isEqualTo(4);
      assertThat(s).doesNotContain("V");
    });
  }

  @Test
  void should_fail_according_to_requirements() {
    // GIVEN
    Consumer<String> restrictions = s -> {
      assertThat(s.length()).isEqualTo(4);
      assertThat(s).startsWith("L");
    };

    // WHEN
    Throwable error = catchThrowable(() -> iterables.assertAllSatisfy(someInfo(), actual, restrictions));

    // THEN
    assertThat(error).isNotNull();

    List<UnsatisfiedRequirement> errors = list(new UnsatisfiedRequirement("Yoda", format("%n" +
                                                                                         "Expecting actual:%n" +
                                                                                         "  \"Yoda\"%n" +
                                                                                         "to start with:%n" +
                                                                                         "  \"L\"%n")));
    verify(failures).failure(info, elementsShouldSatisfy(actual, errors, someInfo()));

  }

  @Test
  void should_fail_if_consumer_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(actual).allSatisfy(null))
                                    .withMessage("The Consumer<T> expressing the assertions requirements must not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      actual = null;
      assertThat(actual).allSatisfy(null);
    }).withMessage(actualIsNull());
  }
}
