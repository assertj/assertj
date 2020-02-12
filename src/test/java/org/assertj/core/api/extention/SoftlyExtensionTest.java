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
package org.assertj.core.api.extention;

import org.assertj.core.annotations.Softly;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.extention.SoftlyExtension;
import org.assertj.core.error.ShouldNotContainNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;

@ExtendWith(SoftlyExtension.class)
@DisplayName("SoftlyExtension hasInstance")
class SoftlyExtensionTest {

  private SoftAssertions softlyNotAnnotated1;
  private SoftAssertions softlyNotAnnotated2;

  @Softly
  private SoftAssertions softlyAnnotated1;

  @Softly
  private SoftAssertions softlyAnnotated2;


  @Test
  void should_pass_if_not_null() {
    //GIVEN
    List<SoftAssertions> softAssertions = new ArrayList<SoftAssertions>() {{
      add(softlyAnnotated1);
      add(softlyAnnotated2);
    }};

    //WHEN/THEN
    assertThat(softAssertions).doesNotContainNull();
  }

  @Test
  void should_fail_if_null() {
    //GIVEN
    List<SoftAssertions> softAssertions = new ArrayList<SoftAssertions>() {{
      add(softlyNotAnnotated1);
      add(softlyNotAnnotated2);
    }};

    //WHEN
    ThrowingCallable code = () -> assertThat(softAssertions).doesNotContainNull();

    //THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(ShouldNotContainNull.shouldNotContainNull(softAssertions).create());

  }


}
