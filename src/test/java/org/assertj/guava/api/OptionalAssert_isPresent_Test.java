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
package org.assertj.guava.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.google.common.base.Optional;

/**
 * @author Kornel
 * @author Joel Costigliola
 */
public class OptionalAssert_isPresent_Test {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    Optional<String> actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).isPresent());
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_when_expecting_absent_optional_to_be_present() {
    // GIVEN
    final Optional<Object> actual = Optional.absent();
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).isPresent());
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage("Expecting Optional to contain a non-null instance but contained nothing (absent Optional)");
  }

  @Test
  public void should_pass_when_optional_is_present() {
    // GIVEN
    final Optional<Object> testedOptional = Optional.of(new Object());
    // THEN
    assertThat(testedOptional).isPresent();
  }

}
