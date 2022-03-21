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
package org.assertj.core.api.string_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.IllegalFormatException;

import org.assertj.core.api.StringAssert;
import org.assertj.core.api.StringAssertBaseTest;
import org.junit.jupiter.api.Test;

class StringAssert_isEqualTo_Test extends StringAssertBaseTest {

  @Override
  protected StringAssert invoke_api_method() {
    return assertions.isEqualTo("%s%d%s%d", 'R', 2, 'D', 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), "R2D2");
  }

  @Test
  void should_not_interpret_string_with_String_format_semantics_when_given_a_single_string() {
    // GIVEN
    String string = "12.3 %";
    // THEN
    assertThat(string).isEqualTo("12.3 %");
  }

  @Test
  void should_throw_IllegalFormatException_when_given_an_invalid_format() {
    // GIVEN
    String template = "%s %s";
    // WHEN
    Throwable exception = catchThrowable(() -> assertThat("foo").isEqualTo(template, 1));
    // THEN
    assertThat(exception).isInstanceOf(IllegalFormatException.class);
  }

  @Test
  void should_throw_NullPointerException_when_given_a_null_template() {
    // GIVEN
    String template = null;
    // WHEN
    Throwable exception = catchThrowable(() -> assertThat("foo").isEqualTo(template, 1));
    // THEN
    assertThat(exception).isInstanceOf(NullPointerException.class)
                         .hasMessageContaining("The expectedStringTemplate must not be null");
  }

  @Test
  void should_fail_if_actual_is_null_since_template_cant_be_null() {
    // GIVEN
    String actual = null;
    // THEN
    expectAssertionError(() -> assertThat(actual).isEqualTo("%s", "abc"));
  }

}
