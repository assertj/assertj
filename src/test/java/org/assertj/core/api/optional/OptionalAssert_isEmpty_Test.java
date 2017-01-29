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
import static org.assertj.core.error.OptionalShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Optional;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

public class OptionalAssert_isEmpty_Test extends BaseTest {

  @Test
  public void should_pass_if_optional_is_empty() throws Exception {
	assertThat(Optional.empty()).isEmpty();
  }

  @Test
  public void should_fail_when_optional_is_null() throws Exception {
	thrown.expectAssertionError(actualIsNull());

	assertThat((Optional<String>) null).isEmpty();
  }

  @Test
  public void should_fail_if_optional_is_present() throws Exception {
	Optional<String> actual = Optional.of("not-empty");

    thrown.expectAssertionError(shouldBeEmpty(actual).create());

	assertThat(actual).isEmpty();
  }
}
