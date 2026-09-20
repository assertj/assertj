/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.inputstream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.Test;

class InputStreamAssert_asStringUtf8_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    InputStream actual = null;
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).asStringUtf8());
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_return_string_assertions_resetting_actual_if_actual_supports_marking() throws Exception {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("Gerçek".getBytes(UTF_8));
    // WHEN
    AbstractStringAssert<?> result = assertThat(actual).asStringUtf8();
    // THEN
    result.isEqualTo("Gerçek");
    then(actual.read()).isEqualTo('G');
  }

  @Test
  void should_return_string_assertions_without_resetting_actual_if_actual_does_not_support_marking() throws Exception {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream("Gerçek".getBytes(UTF_8));
    // WHEN
    AbstractStringAssert<?> result = assertThat(actual).asStringUtf8();
    // THEN
    result.isEqualTo("Gerçek");
    then(actual).isEmpty();
  }

  @Test
  void should_rethrow_IOException() throws Exception {
    // GIVEN
    @SuppressWarnings("resource")
    InputStream actual = mock();
    IOException cause = new IOException();
    given(actual.read(any())).willThrow(cause);
    // WHEN
    Exception exception = catchException(() -> assertThat(actual).asStringUtf8());
    // THEN
    then(exception).isInstanceOf(UncheckedIOException.class)
                   .hasCause(cause);
  }

}
