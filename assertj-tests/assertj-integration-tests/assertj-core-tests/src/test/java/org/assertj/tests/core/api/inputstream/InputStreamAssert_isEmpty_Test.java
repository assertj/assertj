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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.api.inputstream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import org.junit.jupiter.api.Test;

class InputStreamAssert_isEmpty_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    InputStream actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEmpty());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_resetting_actual_if_actual_is_not_empty_and_supports_marking() throws Exception {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(new byte[] { '1', '2' });
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isEmpty());
    // THEN
    then(error).hasMessage(shouldBeEmpty(actual).create());
    then(actual.read()).isEqualTo('1');
  }

  @Test
  void should_fail_without_resetting_actual_if_actual_is_not_empty_and_does_not_support_marking() throws Exception {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream(new byte[] { '1', '2' });
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isEmpty());
    // THEN
    then(error).hasMessage(shouldBeEmpty(actual).create());
    then(actual.read()).isEqualTo('2');
  }

  @Test
  void should_pass_if_actual_is_empty() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(new byte[0]);
    // WHEN/THEN
    assertThat(actual).isEmpty();
  }

  @Test
  void should_rethrow_IOException() throws Exception {
    // GIVEN
    @SuppressWarnings("resource")
    InputStream actual = mock();
    IOException cause = new IOException();
    given(actual.read()).willThrow(cause);
    // WHEN
    Exception exception = catchException(() -> assertThat(actual).isEmpty());
    // THEN
    then(exception).isInstanceOf(UncheckedIOException.class)
                   .hasCause(cause);
  }

}
