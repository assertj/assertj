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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Optional;

import org.assertj.core.api.BaseTest;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.data.TolkienCharacter.Race;
import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
public class OptionalAssert_get_Test extends BaseTest {

  @Test
  public void should_fail_when_optional_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((Optional<String>) null).get())
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_when_optional_is_empty() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(Optional.<String>empty()).get())
                                                   .withMessage(shouldBePresent(Optional.<String>empty()).create());
  }

  @Test
  public void should_pass_when_optional_contains_a_value() {
    TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, Race.HOBBIT);
    assertThat(Optional.of(frodo)).get().hasNoNullFieldsOrProperties();
  }
}
