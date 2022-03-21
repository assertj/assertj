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
package org.assertj.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Comparator;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class OptionalAssert_contains_usingValueComparator_Test {

  private static Comparator<Foo> FOO_COMPARATOR = (o1, o2) -> o1.getValue().toLowerCase()
                                                                .compareTo(o2.getValue().toLowerCase());

  @Test
  void should_fail_when_optional_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((Optional<Foo>) null).usingValueComparator(FOO_COMPARATOR)
                                                                                                     .contains(new Foo("something")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_value_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(Optional.of(new Foo("something"))).usingValueComparator(FOO_COMPARATOR)
                                                                                                       .contains(null))
                                        .withMessage("The expected value should not be <null>.");
  }

  @Test
  void should_pass_if_optional_contains_expected_value() {
    assertThat(Optional.of(new Foo("something"))).usingValueComparator(FOO_COMPARATOR).contains(new Foo("SoMething"));
  }

  @Test
  void should_fail_if_optional_does_not_contain_expected_value() {
    Optional<Foo> actual = Optional.of(new Foo("something"));
    Foo expectedValue = new Foo("something else");

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).usingValueComparator(FOO_COMPARATOR)
                                                                                       .contains(expectedValue))
                                                   .withMessage(shouldContain(actual, expectedValue).create());
  }

  @Test
  void should_fail_if_optional_is_empty() {
    Foo expectedValue = new Foo("test");

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      Optional<Foo> actual = Optional.empty();
      assertThat(actual).usingValueComparator(FOO_COMPARATOR).contains(expectedValue);
    }).withMessage(shouldContain(expectedValue).create());
  }

  private static class Foo {

    private final String value;

    public Foo(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "Foo{value='" + value + "'}";
    }
  }
}
