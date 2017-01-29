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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.error.OptionalShouldContainInstanceOf.shouldContainInstanceOf;

import java.util.Optional;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

public class OptionalAssert_containsInstanceOf_Test extends BaseTest {

  @Test
  public void should_fail_if_optional_is_empty() {
    Optional<Object> actual = Optional.empty();

    Throwable thrown = catchThrowable(() -> {
      assertThat(actual).containsInstanceOf(Object.class);
    });

    assertThat(thrown).isInstanceOf(AssertionError.class)
                      .hasMessage(shouldBePresent(actual).create());
  }

  @Test
  public void should_pass_if_optional_contains_required_type() {
    assertThat(Optional.of("something")).containsInstanceOf(String.class)
                                        .containsInstanceOf(Object.class);
  }

  @Test
  public void should_pass_if_optional_contains_required_type_subclass() {
    assertThat(Optional.of(new SubClass())).containsInstanceOf(ParentClass.class);
  }

  @Test
  public void should_fail_if_optional_contains_other_type_than_required() {
    Optional<ParentClass> actual = Optional.of(new ParentClass());

    Throwable thrown = catchThrowable(() -> {
      assertThat(actual).containsInstanceOf(OtherClass.class);
    });

    assertThat(thrown).isInstanceOf(AssertionError.class)
                      .hasMessage(shouldContainInstanceOf(actual, OtherClass.class).create());
  }

  private static class ParentClass {
  }
  private static class SubClass extends ParentClass {
  }
  private static class OtherClass {
  }
}