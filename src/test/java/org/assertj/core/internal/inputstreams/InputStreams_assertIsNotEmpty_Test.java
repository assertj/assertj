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
package org.assertj.core.internal.inputstreams;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.InputStreams;
import org.assertj.core.internal.InputStreamsBaseTest;
import org.assertj.core.internal.InputStreamsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link InputStreams#assertIsNotEmpty(AssertionInfo, InputStream)} </code>.
 *
 * @author Peng Weiyuan
 */
@DisplayName("InputStreams assertIsNotEmpty")
public class InputStreams_assertIsNotEmpty_Test extends InputStreamsBaseTest {

  @Test
  public void should_throw_error_if_expected_is_null() {
    // GIVEN
    InputStream actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> inputStreams.assertIsNotEmpty(someInfo(), actual));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  public void should_throw_error_wrapping_caught_IOException() throws IOException {
    // GIVEN
    InputStream actual = mock(InputStream.class);
    IOException cause = new IOException();
    when(actual.read()).thenThrow(cause);
    // WHEN
    Throwable error = catchThrowable(() -> inputStreams.assertIsEmpty(someInfo(), actual));
    // THEN
    then(error).isInstanceOf(InputStreamsException.class)
               .hasCause(cause);
  }

  @Test
  public void should_pass_if_actual_is_not_empty() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(new byte[] { '1', '2' });
    // WHEN/THEN
    inputStreams.assertIsNotEmpty(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(new byte[0]);
    // WHEN
    AssertionError error = expectAssertionError(() -> inputStreams.assertIsNotEmpty(someInfo(), actual));
    // THEN
    then(error).hasMessage(shouldNotBeEmpty().create());
  }

}
