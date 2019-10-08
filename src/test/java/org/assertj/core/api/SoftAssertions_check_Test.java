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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.IOException;
import java.util.List;

import org.example.test.MyProjectAssertions;
import org.example.test.MyProjectClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SoftAssertions")
public class SoftAssertions_check_Test {

  private SoftAssertions softly;

  @BeforeEach
  public void setup() {
    softly = new SoftAssertions();
  }

  @Test
  public void should_collect_errors_from_standard_and_custom_assertions() {
    // GIVEN
    MyProjectClass custom = new MyProjectClass("foo");
    // WHEN
    softly.check(() -> Assertions.assertThat(true).isFalse());
    softly.check(() -> Assertions.assertThat(true).isTrue());
    softly.check(() -> MyProjectAssertions.assertThat(custom).hasValue("bar"));
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(2);
    assertThat(errorsCollected.get(1)).hasMessageContainingAll("foo", "bar");
  }

  @Test
  public void should_rethrow_checked_exception_as_runtime_exception() {
    // GIVEN
    MyProjectClass custom = new MyProjectClass("bar");
    // WHEN
    Throwable throwable = catchThrowable(() -> softly.check(() -> MyProjectAssertions.assertThat(custom).hasValue(null)));
    // THEN
    assertThat(throwable).isInstanceOf(RuntimeException.class)
                         .hasCauseInstanceOf(IOException.class);
  }

  @Test
  public void should_rethrow_runtime_exception_as_is() {
    // GIVEN
    MyProjectClass custom = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> softly.check(() -> MyProjectAssertions.assertThat(custom).hasValue("")));
    // THEN
    assertThat(throwable).isInstanceOf(NullPointerException.class)
                         .hasNoCause();
  }

}
