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
package org.assertj.core.internal.classes;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBeFinal;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Classes#assertIsFinal(AssertionInfo, Class)}</code>.
 *
 * @author Michal Kordas
 */
public class Classes_assertIsFinal_Test extends ClassesBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertIsFinal(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_is_a_final_class() {
    classes.assertIsFinal(someInfo(), Math.class);
  }

  @Test
  public void should_fail_if_actual_is_not_a_final_class() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertIsFinal(someInfo(), Object.class))
                                                   .withMessage(shouldBeFinal(Object.class).create());
  }
}
