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
 * Copyright 2012-2013 the original author or authors.
 */
package org.fest.assertions.api;

import static org.fest.assertions.api.GUAVA.assertThat;
import static org.fest.util.FailureMessages.actualIsNull;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * @author Kornel
 * @author Joel Costigliola
 */
public class OptionalAssert_isAbsent_Test extends BaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // given
    Optional<String> actual = null;
    // expect
    expectException(AssertionError.class, actualIsNull());
    // when
    assertThat(actual).isAbsent();
  }

  @Test
  public void should_fail_when_expected_present_optional_is_absent() {
    // given
    final Optional<String> testedOptional = Optional.of("X");
    // expect
    expectException(AssertionError.class, "Expecting Optional to contain nothing (absent Optional) but contained <'X'>");
    // when
    assertThat(testedOptional).isAbsent();
  }

  @Test
  public void should_pass_when_optional_is_absent() {
    // given
    final Optional<String> testedOptional = Optional.absent();
    // when
    assertThat(testedOptional).isAbsent();
    // then
    // pass
  }

}
