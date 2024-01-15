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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.guava.api;

import com.google.common.collect.LinkedHashMultimap;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

public class MultimapAssert_doesNotContainKeys_Test extends MultimapAssertBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContainKeys("Nets", "Bulls", "Knicks"));
    // THEN
    org.assertj.core.api.Assertions.assertThat(throwable).isInstanceOf(AssertionError.class)
                                   .hasMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_does_not_contains_given_key() {
    assertThat(actual).doesNotContainKeys("apples");
  }

  @Test
  public void should_pass_if_actual_does_not_contains_given_keys() {
    assertThat(actual).doesNotContainKeys("apples", "oranges");
  }

  @Test
  void should_fail_with_null_key() {
    // GIVEN
    actual = LinkedHashMultimap.create();
    actual.put(null, "apples");

    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContainKey(null));

    // THEN
    org.assertj.core.api.Assertions.assertThat(throwable).isInstanceOf(AssertionError.class)
                                   .hasMessage(format("\n" +
                                                      "Expecting actual:\n" +
                                                      "  {null=[apples]}\n" +
                                                      "not to contain key:\n" +
                                                      "  null"));
  }

  @Test
  void should_fail_with_null_key_in_array() {
    // GIVEN
    actual = LinkedHashMultimap.create();
    actual.put(null, "apples");

    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContainKeys("cheese", null));

    org.assertj.core.api.Assertions.assertThat(throwable).isInstanceOf(AssertionError.class)
                                   .hasMessage(format("\n" +
                                                      "Expecting actual:\n" +
                                                      "  {null=[apples]}\n" +
                                                      "not to contain key:\n" +
                                                      "  null"));
  }

  @Test
  void should_fail_with_null_array() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContainKeys(null));

    // THEN
    org.assertj.core.api.Assertions.assertThat(throwable).isInstanceOf(NullPointerException.class)
                                   .hasMessage("The array of keys to look for should not be null");
  }

  @Test
  void should_fail_if_single_key_is_present() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContainKey("Bulls"));

    // THEN
    org.assertj.core.api.Assertions.assertThat(throwable).isInstanceOf(AssertionError.class)
                                   .hasMessage(format("\n" +
                                                      "Expecting actual:\n" +
                                                      "  {Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}\n"
                                                      +
                                                      "not to contain key:\n" +
                                                      "  \"Bulls\""));
  }

  @Test
  void should_fail_if_one_key_is_present() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContainKeys("Bulls", "Knicks"));

    // THEN
    org.assertj.core.api.Assertions.assertThat(throwable).isInstanceOf(AssertionError.class)
                                   .hasMessage(format("\n" +
                                                      "Expecting actual:\n" +
                                                      "  {Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}\n"
                                                      +
                                                      "not to contain key:\n" +
                                                      "  \"Bulls\""));
  }

  @Test
  void should_fail_if_multiple_keys_are_present() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContainKeys("Bulls", "Knicks", "Spurs"));

    // THEN
    org.assertj.core.api.Assertions.assertThat(throwable).isInstanceOf(AssertionError.class)
                                   .hasMessage(format("\n" +
                                                      "Expecting actual:\n" +
                                                      "  {Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}\n"
                                                      +
                                                      "not to contain keys:\n" +
                                                      "  [\"Bulls\", \"Spurs\"]"));
  }
}
