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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.guava.testkit.AssertionErrors.expectAssertionError;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.google.common.io.ByteSource;

/**
 * @author Andrew Gaul
 */
class ByteSourceAssert_hasSameContentAs_Test {

  @Test
  void should_pass_if_size_of_actual_is_equal_to_expected_size() throws IOException {
    // GIVEN
    ByteSource actual = ByteSource.wrap(new byte[1]);
    ByteSource other = ByteSource.wrap(new byte[1]);
    // WHEN/THEN
    assertThat(actual).hasSameContentAs(other);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    ByteSource actual = null;
    ByteSource other = ByteSource.wrap(new byte[1]);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasSameContentAs(other));
    // THEN
    then(error).isInstanceOf(AssertionError.class)
               .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_content_of_actual_is_not_equal_to_expected_content() {
    // GIVEN
    ByteSource actual = ByteSource.wrap(new byte[1]);
    ByteSource other = ByteSource.wrap(new byte[] { (byte) 1 });
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasSameContentAs(other));
    // THEN
    then(error).hasMessage(shouldHaveSameContent(actual, other).create());
  }

}
