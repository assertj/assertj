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


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Optional;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

public class OptionalAssert_hasValueSatisfying_Test extends BaseTest {

  @Test
  public void should_fail_when_optional_is_null() {
    thrown.expectAssertionError(actualIsNull());
    assertThat((Optional<String>) null).hasValueSatisfying(s -> {
    });
  }

  @Test
  public void should_fail_when_optional_is_empty() {
    thrown.expectAssertionError(shouldBePresent(Optional.empty()).create());
    assertThat(Optional.empty()).hasValueSatisfying(o -> {
    });
  }

  @Test
  public void should_pass_when_consumer_passes() {
    assertThat(Optional.of("something")).hasValueSatisfying(s -> {
      assertThat(s).isEqualTo("something")
                   .startsWith("some")
                   .endsWith("thing");
    });
    assertThat(Optional.of(10)).hasValueSatisfying(i -> {
      assertThat(i).isGreaterThan(9);
    });
  }

  @Test
  public void should_fail_from_consumer() {
    thrown.expectAssertionError("expected:<\"something[]\"> but was:<\"something[ else]\">");
    assertThat(Optional.of("something else")).hasValueSatisfying(s -> {
      assertThat(s).isEqualTo("something");
    });
  }
}
