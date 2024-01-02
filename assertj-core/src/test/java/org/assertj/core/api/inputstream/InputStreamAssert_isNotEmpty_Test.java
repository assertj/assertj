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
package org.assertj.core.api.inputstream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import org.junit.jupiter.api.Test;

class InputStreamAssert_isNotEmpty_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    InputStream actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotEmpty());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(new byte[0]);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isNotEmpty());
    // THEN
    then(error).hasMessage(shouldNotBeEmpty().create());
  }

  @Test
  void should_pass_if_actual_is_not_empty() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(new byte[] { '1', '2' });
    // WHEN/THEN
    assertThat(actual).isNotEmpty();
  }

  @SuppressWarnings("resource")
  @Test
  void should_rethrow_IOException() throws IOException {
    // GIVEN
    InputStream actual = mock();
    IOException cause = new IOException();
    given(actual.read()).willThrow(cause);
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(actual).isNotEmpty());
    // THEN
    then(error).isInstanceOf(UncheckedIOException.class).hasCauseReference(cause);
  }

}
