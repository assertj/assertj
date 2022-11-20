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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Joel Costigliola
 */
class MultimapAssert_hasSize_Test extends MultimapAssertBaseTest {

  @Test
  void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    // WHEN/THEN
    assertThat(actual).hasSize(9);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).hasSize(2));
    // THEN
    then(throwable).isInstanceOf(AssertionError.class)
                   .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).hasSize(3));
    // THEN
    then(throwable).isInstanceOf(AssertionError.class)
                   .hasMessage(format("%n" +
                                      "Expected size: 3 but was: 9 in:%n" +
                                      "{Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, " +
                                      "Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}"));
  }

}
