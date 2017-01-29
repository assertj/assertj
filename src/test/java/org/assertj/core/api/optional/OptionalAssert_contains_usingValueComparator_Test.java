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
package org.assertj.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Comparator;
import java.util.Optional;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

public class OptionalAssert_contains_usingValueComparator_Test extends BaseTest {

  private static Comparator<Foo> FOO_COMPARATOR = (o1, o2) -> o1.getValue().toLowerCase()
                                                        .compareTo(o2.getValue().toLowerCase());

  @Test
  public void should_fail_when_optional_is_null() throws Exception {
    thrown.expectAssertionError(actualIsNull());

    assertThat((Optional<Foo>) null).usingValueComparator(FOO_COMPARATOR).contains(new Foo("something"));
  }

  @Test
  public void should_fail_if_expected_value_is_null() throws Exception {
    thrown.expectIllegalArgumentException("The expected value should not be <null>.");

    assertThat(Optional.of(new Foo("something"))).usingValueComparator(FOO_COMPARATOR).contains(null);
  }

  @Test
  public void should_pass_if_optional_contains_expected_value() throws Exception {
    assertThat(Optional.of(new Foo("something"))).usingValueComparator(FOO_COMPARATOR).contains(new Foo("SoMething"));
  }

  @Test
  public void should_fail_if_optional_does_not_contain_expected_value() throws Exception {
    Optional<Foo> actual = Optional.of(new Foo("something"));
    Foo expectedValue = new Foo("something else");

    thrown.expectAssertionError(shouldContain(actual, expectedValue).create());

    assertThat(actual).usingValueComparator(FOO_COMPARATOR).contains(expectedValue);
  }

  @Test
  public void should_fail_if_optional_is_empty() throws Exception {
    Foo expectedValue = new Foo("test");

    thrown.expectAssertionError(shouldContain(expectedValue).create());

    Optional<Foo> actual = Optional.empty();
    assertThat(actual).usingValueComparator(FOO_COMPARATOR).contains(expectedValue);
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
