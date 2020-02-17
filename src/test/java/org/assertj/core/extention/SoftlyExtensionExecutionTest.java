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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.extention;

import org.assertj.core.annotations.Softly;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;

@DisplayName("SoftlyExtension runs")
@ExtendWith({SoftlyExtension.class, MockitoExtension.class})
class SoftlyExtensionExecutionTest {

  @Softly(assertAfter = true)
  private SoftAssertions softlyAssertAfterTrue;

  @Softly(assertAfter = false)
  private SoftAssertions softlyAssertAfterFalse;


  @Test
  void should_pass_if_test_is_valid() {
    //GIVEN
    Integer ONE = 1;

    //WHEN/THEN
    softlyAssertAfterTrue.assertThat(ONE).isEqualTo(1);
  }

  @Test
  void should_fail_but_doesnt_because_assert_all_not_explicitly_called() {
    //GIVEN
    Integer ONE = 1;

    //WHEN
    ThrowingCallable code = () -> softlyAssertAfterFalse.assertThat(ONE).isEqualTo(2);

    //THEN
    Assertions.assertThatCode(code)
      .doesNotThrowAnyException();

  }

  @Test
  void should_fail_when_assert_all_is_called_explicitly() {
    //GIVEN
    Integer ONE = 1;
    Integer TWO = 2;

    //WHEN
    ThrowingCallable code = () -> {
      softlyAssertAfterFalse.assertThat(ONE).isEqualTo(TWO);
      softlyAssertAfterFalse.assertAll();
    };

    assertThatAssertionErrorIsThrownBy(code);
  }
}
