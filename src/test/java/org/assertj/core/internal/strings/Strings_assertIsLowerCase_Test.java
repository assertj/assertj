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
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeLowerCase.shouldBeLowerCase;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertLowerCase(org.assertj.core.api.AssertionInfo, CharSequence)} </code>.
 *
 * @author Marcel Overdijk
 */
public class Strings_assertIsLowerCase_Test extends StringsBaseTest {

  @Test
  public void should_pass_if_actual_is_lowercase() {
    strings.assertLowerCase(someInfo(), "lego");
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    strings.assertLowerCase(someInfo(), "");
  }

  @Test
  public void should_fail_if_actual_is_not_fully_lowercase() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertLowerCase(someInfo(), "Lego"))
                                                   .withMessage(shouldBeLowerCase("Lego").create());
  }

  @Test
  public void should_fail_if_actual_is_uppercase() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertLowerCase(someInfo(), "LEGO"))
                                                   .withMessage(shouldBeLowerCase("LEGO").create());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertLowerCase(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

}
