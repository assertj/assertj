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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.google.common.io.ByteSource;

/**
 * Tests for <code>{@link org.assertj.guava.api.ByteSourceAssert#hasSize(long)}</code>.
 *
 * @author Andrew Gaul
 */
public class ByteSourceAssert_hasSize_Test {

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() throws IOException {
    ByteSource actual = ByteSource.wrap(new byte[9]);
    assertThat(actual).hasSize(9);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    ByteSource actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).hasSize(2));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    // GIVEN
    ByteSource actual = ByteSource.wrap(new byte[9]);
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).hasSize(3));
    // THEN
    assertThat(throwable).hasMessage(format("%n" +
                                            "Expected size: 3 but was: 9 in:%n" +
                                            "ByteSource.wrap(000000000000000000)"));
  }
}
